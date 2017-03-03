package pinklady.cartedorandroid;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

/**
 * Created by ETCHELECOU on 03/03/2017.
 */
public class CurrentPlaceFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private static final String TAG = "CurrentPlaceFragment";
    private int mPage;

    private static GoogleApiClient mGoogleApiClient;

    public static CurrentPlaceFragment newInstance(int page, GoogleApiClient googleApiClient) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        mGoogleApiClient = googleApiClient;
        CurrentPlaceFragment fragment = new CurrentPlaceFragment();
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
        View view = inflater.inflate(R.layout.fragment_current_place, container, false);

        Button currentPlaceButton = (Button) view.findViewById(R.id.currentPlaceButton);
        currentPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ){
                    callPlaceDetectionApi();
                }
            }
        });
        return view;
    }

    private void callPlaceDetectionApi() throws SecurityException {

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i(TAG, String.format("Place '%s' with " +
                                    "likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                    for (Integer type:
                            placeLikelihood.getPlace().getPlaceTypes()) {
                        Log.i(TAG, String.format("Type : %d", type));
                    }
                }
                likelyPlaces.release();
            }
        });
    }
}