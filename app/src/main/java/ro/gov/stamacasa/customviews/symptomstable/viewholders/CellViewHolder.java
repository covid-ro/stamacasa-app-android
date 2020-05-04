package ro.gov.stamacasa.customviews.symptomstable.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.symptomstable.data.SymptomCell;

public class CellViewHolder extends AbstractViewHolder {

    @NonNull
    private final TextView cellTv;
    @NonNull
    public final ConstraintLayout cellCl;

    public CellViewHolder(@NonNull View itemView) {
        super(itemView);
        cellTv = itemView.findViewById(R.id.symptomCellTv);
        cellCl = itemView.findViewById(R.id.cellCl);
    }

    public void setCell(@Nullable SymptomCell cell) {
        if (cell != null){
            if (cell.getHadSymptom()){
                cellTv.setTextColor(cellTv.getContext().getResources().getColor(R.color.errorColor));
                cellTv.setText(R.string.yes);
            } else {
                cellTv.setTextColor(cellTv.getContext().getResources().getColor(R.color.bodyTextColor));
                cellTv.setText(R.string.no);
            }
        }
    }
}