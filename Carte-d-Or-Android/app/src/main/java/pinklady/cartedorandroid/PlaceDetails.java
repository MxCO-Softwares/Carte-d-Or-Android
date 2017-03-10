package pinklady.cartedorandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.util.Locale;

public class PlaceDetails extends Activity {
    private static final String TAG = "PlaceDetails";
    private TextView NameTextView;
    private TextView AddressTextView;
    private TextView IDTextView;
    private TextView PhoneNumberTextView;
    private TextView WebsiteTextView;
    private TextView PlaceTypesTextView;
    private TextView PriceLevelTextView;
    private TextView RatingTextView;
    private ImageView PictureImageView;

    private String ID;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        activity = this;

        NameTextView = (TextView) findViewById(R.id.NameTextView);
        AddressTextView = (TextView) findViewById(R.id.AddressTextView);
        IDTextView = (TextView) findViewById(R.id.IDTextView);
        PhoneNumberTextView = (TextView) findViewById(R.id.PhoneNumberTextView);
        WebsiteTextView = (TextView) findViewById(R.id.WebsiteTextView);
        PlaceTypesTextView = (TextView) findViewById(R.id.PlaceTypesTextView);
        PriceLevelTextView = (TextView) findViewById(R.id.PriceLevelTextView);
        RatingTextView = (TextView) findViewById(R.id.RatingTextView);
        PictureImageView = (ImageView) findViewById(R.id.PictureImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ID = getIntent().getStringExtra("ID");
        Places.GeoDataApi.getPlaceById(MainActivity.getmGoogleApiClient(), ID)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            final Place myPlace = places.get(0);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    NameTextView.setText(myPlace.getName().toString());
                                    AddressTextView.setText(myPlace.getAddress());
                                    IDTextView.setText(myPlace.getId());
                                    PhoneNumberTextView.setText(myPlace.getPhoneNumber());
                                    if (myPlace.getWebsiteUri() != null)
                                    {
                                        WebsiteTextView.setText(myPlace.getWebsiteUri().toString());
                                    }

                                    StringBuilder sb = new StringBuilder();
                                    boolean appendSeparator = false;
                                    for (Integer type:
                                            myPlace.getPlaceTypes()) {
                                        if (appendSeparator)
                                            sb.append(','); // a comma
                                        appendSeparator = true;

                                        sb.append(type);
                                    }
                                    PlaceTypesTextView.setText(sb.toString());
                                    PriceLevelTextView.setText(String.format(Locale.getDefault(), "%d", myPlace.getPriceLevel()));
                                    RatingTextView.setText(String.format(Locale.getDefault(), "%f", myPlace.getRating()));
                                }
                            });
                        } else {
                            Log.e(TAG, "Place not found");
                        }
                        places.release();
                    }
                });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            placePhotosTask();
        }
    }

    private void placePhotosTask() {
        final String placeId = ID;

        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
        new PhotoTask(PictureImageView.getWidth(), PictureImageView.getHeight()) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
//                PictureImageView.setVisibility(View.INVISIBLE);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
//                    PictureImageView.setVisibility(View.VISIBLE);
                    // Photo has been loaded, display it.
                    PictureImageView.setImageBitmap(attributedPhoto.bitmap);

                    // Display the attribution as HTML content if set.
//                    if (attributedPhoto.attribution == null) {
//                        mText.setVisibility(View.GONE);
//                    } else {
//                        mText.setVisibility(View.VISIBLE);
//                        mText.setText(Html.fromHtml(attributedPhoto.attribution.toString()));
//                    }

                }
            }
        }.execute(placeId);
    }

}
