package pinklady.cartedorandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;

/**
 * Created by ETCHELECOU on 03/03/2017.
 */
public class AutoCompleteFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private static GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mAdapter;

    public static AutoCompleteFragment newInstance(int page, GoogleApiClient googleApiClient) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        mGoogleApiClient = googleApiClient;
        AutoCompleteFragment fragment = new AutoCompleteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_autocomplete, container, false);
        AutoCompleteTextView autoCompView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("FR")
                .build();

        mAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, null,
                autocompleteFilter);

        autoCompView.setAdapter(mAdapter);
        autoCompView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        return view;
    }
}