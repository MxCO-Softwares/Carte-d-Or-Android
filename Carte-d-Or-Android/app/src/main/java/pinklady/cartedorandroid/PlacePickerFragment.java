package pinklady.cartedorandroid;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by ETCHELECOU on 03/03/2017.
 */
public class PlacePickerFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private static final int PLACE_PICKER_REQUEST = 200;
    private static final String TAG = "PlacePickerFragment";
    private int mPage;

    private static GoogleApiClient mGoogleApiClient;

    public static PlacePickerFragment newInstance(int page, GoogleApiClient googleApiClient) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        mGoogleApiClient = googleApiClient;
        PlacePickerFragment fragment = new PlacePickerFragment();
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
        View view = inflater.inflate(R.layout.fragment_place_picker, container, false);

        Button currentPlaceButton = (Button) view.findViewById(R.id.placePickerButton);
        currentPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ){
                    displayPlacePicker();
                }
            }
        });
        return view;
    }

    private void displayPlacePicker() {
        if( mGoogleApiClient == null || !mGoogleApiClient.isConnected() )
            return;

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult( builder.build(getActivity()), PLACE_PICKER_REQUEST );
        } catch ( GooglePlayServicesRepairableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesRepairableException thrown" );
        } catch ( GooglePlayServicesNotAvailableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown" );
        }
    }

    public void onActivityResult( int requestCode, int resultCode, Intent data ) {
        if( requestCode == PLACE_PICKER_REQUEST && resultCode == getActivity().RESULT_OK ) {
            displayPlace( PlacePicker.getPlace( getActivity(),data) );
        }
    }

    private void displayPlace( Place place ) {
        if( place == null )
            return;

        String content = "";
        if( !TextUtils.isEmpty( place.getName() ) ) {
            content += "Name: " + place.getName() + "\n";
        }
        if( !TextUtils.isEmpty( place.getAddress() ) ) {
            content += "Address: " + place.getAddress() + "\n";
        }
        if( !TextUtils.isEmpty( place.getPhoneNumber() ) ) {
            content += "Phone: " + place.getPhoneNumber();
        }
        Log.d(TAG, content);
//        mTextView.setText( content );
    }
}