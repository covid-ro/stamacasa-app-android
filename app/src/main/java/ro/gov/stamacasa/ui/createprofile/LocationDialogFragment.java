package ro.gov.stamacasa.ui.createprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.data.LocationsRepository;
import ro.gov.stamacasa.ui.createprofile.adapter.Location;
import ro.gov.stamacasa.ui.createprofile.adapter.LocationRvAdapter;


public class LocationDialogFragment extends DialogFragment implements LocationRvAdapter.ClickListener {

    private LocationListener locationListener;
    private String countyName;
    private View view;

    LocationRvAdapter adapter;
    private Location location;
    private SearchView searchView;
    private RecyclerView locationsRv;

    private List<String> locationsList = new ArrayList<>();

    public LocationDialogFragment(LocationListener locationListener) {
        this.locationListener = locationListener;
        location = Location.COUNTY;
    }

    public LocationDialogFragment(LocationListener locationListener, String countyName) {
        this.countyName = countyName;
        this.locationListener = locationListener;
        location = Location.CITY;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_location, container, false);

        initList();
        initViews();

        return view;
    }

    private void initViews() {
        locationsRv = view.findViewById(R.id.locationList);
        searchView = view.findViewById(R.id.searchView);

        adapter = new LocationRvAdapter(locationsList, this, location);
        locationsRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        locationsRv.setAdapter(adapter);

        setUpSearchView();
    }

    private void initList() {
        switch (location) {
            case CITY:
                prepareListCities();
                break;

            case COUNTY:
                prepareListCounty();
                break;
        }
    }

    @Override
    public void onLocationClick(String location, Location locationSection) {
        if (locationListener != null) {
            locationListener.locationSelected(location, locationSection);
            dismiss();
        }
    }

    private void setUpSearchView() {
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return true;
            }
        });
    }

    private void prepareListCounty() {
        locationsList = LocationsRepository.getInstance(getContext()).getCountiesList();
    }

    private void prepareListCities() {
        locationsList = LocationsRepository.getInstance(getContext()).getCityList(countyName);
    }
}
