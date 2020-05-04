package ro.gov.stamacasa.ui.createprofile.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import ro.gov.stamacasa.R;

public class LocationRvAdapter extends RecyclerView.Adapter<LocationRvAdapter.ViewHolder> implements Filterable {

    private List<String> locations;
    private List<String> filteredLocationList;
    private ClickListener listener;
    private Location locationSection;

    public LocationRvAdapter(List<String> locations, ClickListener listener, Location location) {
        this.locations = locations;
        this.listener = listener;
        this.locationSection = location;
        this.filteredLocationList = locations;
    }

    public interface ClickListener {
        void onLocationClick(String location, Location locationSection);
    }

    @NonNull
    @Override
    public LocationRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_location_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationRvAdapter.ViewHolder holder, int position) {
        String location = filteredLocationList.get(position);
        holder.bindView(location);
        holder.locationTv.setOnClickListener(view -> listener.onLocationClick(location, locationSection));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredLocationList = new ArrayList<>(locations);
                } else {
                   filterList(charSequenceString);
                }
                FilterResults results = new FilterResults();
                results.values = filteredLocationList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredLocationList = (List<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private void filterList(String charSequenceString) {
        List<String> filteredList = new ArrayList<>();
        for (String location : locations) {
            if (location.toLowerCase().startsWith(charSequenceString.toLowerCase())) {
                filteredList.add(location);
            }
            filteredLocationList = filteredList;
        }
    }

    @Override
    public int getItemCount() {
        return filteredLocationList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView locationTv;

        ViewHolder(View itemView) {
            super(itemView);
            locationTv = itemView.findViewById(R.id.locationTv);
        }

        //without handler the text is not set..
        void bindView(String location) {
            final Runnable r = () -> locationTv.setText(location);
            final Handler handler = new Handler();
            handler.postDelayed(r, 100);
        }
    }
}
