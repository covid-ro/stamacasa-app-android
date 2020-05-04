package ro.gov.stamacasa.customviews.symptomstable.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.symptomstable.data.SymptomRow;

public class RowViewHolder extends AbstractViewHolder {

    @NonNull
    public final TextView rowTv;
    @NonNull
    public final ConstraintLayout mConstraintLayout;

    public RowViewHolder(@NonNull View itemView) {
        super(itemView);
        rowTv = itemView.findViewById(R.id.symptomRowTv);
        mConstraintLayout = itemView.findViewById(R.id.rowCl);
    }



    public void setRow(@Nullable SymptomRow row) {
        if (row != null)
            rowTv.setText(String.valueOf(row.getSymptomName()));


//        // If your TableView should have auto resize for cells & columns.
//        // Then you should consider the below lines. Otherwise, you can ignore them.
//
//        // It is necessary to remeasure itself.
//        cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
//        cell_textview.requestLayout();
    }
}
