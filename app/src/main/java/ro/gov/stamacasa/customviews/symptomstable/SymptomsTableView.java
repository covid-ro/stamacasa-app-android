package ro.gov.stamacasa.customviews.symptomstable;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.evrencoskun.tableview.TableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.symptomstable.adapter.TableViewAdapter;
import ro.gov.stamacasa.customviews.symptomstable.data.SymptomCell;
import ro.gov.stamacasa.customviews.symptomstable.data.SymptomColumn;
import ro.gov.stamacasa.customviews.symptomstable.data.SymptomRow;
import ro.gov.stamacasa.data.FormsRepository;
import ro.gov.stamacasa.data.pojo.forminput.EvaluationFormInput;
import ro.gov.stamacasa.data.pojo.formsections.Answer;
import ro.gov.stamacasa.ui.DataLoadListener;
import ro.gov.stamacasa.ui.tools.TimestampConverter;

public class SymptomsTableView extends TableView {

    private TableViewAdapter mTableViewAdapter;
    private CompositeDisposable mDisposable;
    private TextView title;
    private DataLoadListener mDataLoadListener;
    private boolean dataLoaded;

    private List<SymptomColumn> mSymptomColumns = new ArrayList<>();
    private List<List<SymptomCell>> mSymptomCells = new ArrayList<>();
    private List<List<SymptomCell>> reversedCells = new ArrayList<>();
    private List<SymptomRow> mSymptomRows = new ArrayList<>();
    private List<Answer> mAnswers = new ArrayList<>();

    private long userId;
    private int previewColumnsAmount = 3;

    public SymptomsTableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    private void initLayout(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.cv_symptoms_table_view, this);
        title = view.findViewById(R.id.title);
    }

    public void displayData(int previewColumnsAmount, TableViewAdapter adapter, long userId) {
        this.mTableViewAdapter = adapter;
        this.userId = userId;
        this.previewColumnsAmount = previewColumnsAmount;
        setSymptomsTableData();
    }

    public void setDataLoadListener(DataLoadListener loadListener) {
        this.mDataLoadListener = loadListener;
    }

    void setSymptomsTableData() {
        populateRows();
        populateColumns();
    }

    private void setItems() {
        if (mTableViewAdapter != null && mSymptomColumns.size() > 0 &&
                mSymptomRows.size() > 0 && reversedCells.size() > 0) {
            mTableViewAdapter.setAllItems(mSymptomColumns, mSymptomRows, reversedCells);
            if (title != null) {
                title.setVisibility(VISIBLE);
            }
            dataLoaded = true;
        } else {
            dataLoaded = false;
        }
        announceListeners();
    }

    private void announceListeners() {
        if (mDataLoadListener != null) {
            mDataLoadListener.symptomsLoadComplete(dataLoaded);
        }
    }

    private void populateRows() {
        mSymptomRows.clear();
        mAnswers = FormsRepository.getInstance(getContext()).getSymptoms();
        for (Answer answer : mAnswers) {
            mSymptomRows.add(new SymptomRow(answer.getAnswer_text()));
        }
    }

    private void populateColumns() {
        mDisposable.add(FormsRepository.getInstance(getContext())
                .getLatestEvaluationTimestampsById(userId, previewColumnsAmount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::transformTimestamps)
                .subscribe(this::setColumns, this::onError));
    }

    private void populateCells() {
        mSymptomCells.clear();
        mDisposable.add(FormsRepository.getInstance(getContext())
                .getLatestEvaluationForUser(userId, previewColumnsAmount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::transformCells)
                .subscribe(this::addCells, this::onError));
    }

    private void addCells(List<List<SymptomCell>> cells) {
        if (cells != null) {
            mSymptomCells.addAll(cells);
            mDisposable.add(reverseRowsAndColumns()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::reverseComplete, this::onError));
        } else {
            announceListeners();
        }
    }

    private void onError(Throwable e) {
        e.printStackTrace();
    }

    private void setColumns(List<String> columns) {
        mSymptomColumns.clear();
        if (columns.size() > 0){
            for (String column : columns) {
                mSymptomColumns.add(new SymptomColumn(column));
            }
        } else {
            announceListeners();
        }
    }

    private void reverseComplete(List<List<SymptomCell>> reversedCells) {
        this.reversedCells.clear();
        this.reversedCells.addAll(reversedCells);
        setItems();
    }

    private List<String> transformTimestamps(List<Long> timestamps) {
        ArrayList<String> dates = new ArrayList<>();
        populateCells();

        TimestampConverter converter = new TimestampConverter();

        for (Long stamp : timestamps) {
            dates.add(converter.getMonthAndDay(stamp));
        }
        return dates;
    }

    public void pauseDisposables() {
        this.mDisposable.clear();
    }

    public void startDisposables() {
        this.mDisposable = new CompositeDisposable();
    }

    @NonNull
    @Override
    public DividerItemDecoration getVerticalItemDecoration() {
        return createItemDecoration(DividerItemDecoration.VERTICAL);
    }

    @NonNull
    protected DividerItemDecoration createItemDecoration(int orientation) {
        Drawable divider = ContextCompat.getDrawable(getContext(), R.drawable.cell_line_divider);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), orientation);
        if (divider != null) {
            divider.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            itemDecoration.setDrawable(divider);
        }
        return itemDecoration;
    }


    //because the library displays the data based on the rows (horizontally), and the
    //evaluation forms should be displayed vertically (based on the timestamp column)
    //we need to reverse the rows and columns so that the data is displayed accordingly.
    //CAUTION --> this only works if the rows and columns have the same number of items when interchanged
    private Single<List<List<SymptomCell>>> reverseRowsAndColumns() {
        return Single.create(e -> {

            List<List<SymptomCell>> symptomRows = new ArrayList<>();

            //when the columns and rows reach the same size,
            //we can begin the operations.
            if (mSymptomColumns.size() == mSymptomCells.size()) {
                if (mSymptomCells.size() > 0) {

                    //the number of cells in a row represents the amount of rows there should
                    //be in total, therefore, we want to iterate the loop as many times as there
                    //are cells in a row (since all rows have the same amount of cells, we can just
                    //retrieve the size of the first one and use it as a threshold)
                    for (int i = 0; i < this.mSymptomCells.get(0).size(); i++) {

                        List<SymptomCell> symptomRow = new ArrayList<>();

                        //we want to interchange each row number with its respective column number
                        for (List<SymptomCell> symptomCells : this.mSymptomCells) {
                            for (SymptomCell cell : symptomCells) {
                                if (symptomCells.indexOf(cell) == i) {
                                    symptomRow.add(cell);
                                    break;
                                }
                            }
                        }
                        symptomRows.add(symptomRow);
                    }
                    e.onSuccess(symptomRows);
                }
            }
        });
    }

    //transform evaluation input into the data that needs to be displayed --> "DA/NU" based on
    //whether or not the user has had the symptom on the respective date.
    private List<List<SymptomCell>> transformCells(List<EvaluationFormInput> evaluationFormInput) {

        List<List<SymptomCell>> cells = new ArrayList<>();

        //for each form we need to create a new list that will hold the data that will be displayed.
        for (EvaluationFormInput input : evaluationFormInput) {
            List<SymptomCell> hadSymptoms = new ArrayList<>();
            Log.d("SymptomsTableCv", "evaluation form to string " + evaluationFormInput);
            //here we have all the answers from the evaluation form, so we need to filter them
            //based on the symptoms question id, which is known by the repository.

            if (!input.getAnswersMap().containsKey(FormsRepository.symptomsQuestionId)) {
                for (Answer answer : mAnswers) {
                    hadSymptoms.add(new SymptomCell(false));
                }
            } else {
                for (Map.Entry<Integer, List<Integer>> entry : input.getAnswersMap().entrySet()) {
                    if (entry.getKey().equals(FormsRepository.symptomsQuestionId)) {
                        //based on the ids of the answers that are present in the map
                        //we can establish whether or not the user has had that symptom
                        //when we compare to all the answers that are possible for this question
                        //(Cough/Fever/ETC)
                        for (Answer answer : mAnswers) {
                            if (entry.getValue().contains(answer.getAnswer_id())) {
                                hadSymptoms.add(new SymptomCell(true));
                            } else {
                                hadSymptoms.add(new SymptomCell(false));
                            }
                        }
                    }
                }
            }
            cells.add(hadSymptoms);
        }
        return cells;
    }
}
