package ro.gov.stamacasa.customviews.symptomstable.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.symptomstable.data.SymptomColumn;

public class ColumnViewHolder extends AbstractViewHolder {

    @NonNull
    public final TextView columnTv;

    public ColumnViewHolder(@NonNull View itemView) {
        super(itemView);
        columnTv = itemView.findViewById(R.id.symptomColumnTv);
    }


    public void setColumn(@Nullable SymptomColumn column) {
        if (column != null)
            columnTv.setText(String.valueOf(column.getDate()));

//        // If your TableView should have auto resize for cells & columns.
//        // Then you should consider the below lines. Otherwise, you can ignore them.
//
//        // It is necessary to remeasure itself.
//        cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
//        cell_textview.requestLayout();
    }
}