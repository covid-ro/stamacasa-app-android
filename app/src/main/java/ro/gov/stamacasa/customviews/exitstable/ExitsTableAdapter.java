package ro.gov.stamacasa.customviews.exitstable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.data.pojo.exit.ExitForm;

public class ExitsTableAdapter extends RecyclerView.Adapter<ExitsTableAdapter.ViewHolder> {

    private List<ExitForm> forms = new ArrayList<>();
    private ExitsTableAdapter.ClickListener listener;

    ExitsTableAdapter(ExitsTableAdapter.ClickListener listener) {
        this.listener = listener;
    }

    public interface ClickListener {
        void onFormClick(ExitForm exitForm);
    }

    public void setData(List<ExitForm> forms){
        this.forms = forms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExitsTableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExitsTableAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.exits_history_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExitsTableAdapter.ViewHolder holder, int position) {
        ExitForm exitForm = forms.get(position);
        holder.bindView(exitForm);
        holder.mConstraintLayout.setOnClickListener(view -> listener.onFormClick(exitForm));
    }

    @Override
    public int getItemCount() {
        return forms.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mConstraintLayout;
        TextView date;
        TextView motive;
        TextView contact;
        TextView timeStart;
        TextView timeEnd;

        ViewHolder(View itemView) {
            super(itemView);
            mConstraintLayout = itemView.findViewById(R.id.layout);
            date = itemView.findViewById(R.id.dateTv);
            motive = itemView.findViewById(R.id.motiv);
            timeEnd = itemView.findViewById(R.id.endTimeTv);
            timeStart = itemView.findViewById(R.id.startTimeTv);
            contact = itemView.findViewById(R.id.contact);
        }

        void bindView(ExitForm exitForm) {
            date.setText(exitForm.getDate());

            if (exitForm.isWasInDanger()) {
                contact.setText(R.string.yes);
            } else {
                contact.setText(R.string.no);
            }

            timeStart.setText(exitForm.getStartingHour());
            timeEnd.setText(exitForm.getEndingHour());
            motive.setText(exitForm.getReason());
        }
    }
}