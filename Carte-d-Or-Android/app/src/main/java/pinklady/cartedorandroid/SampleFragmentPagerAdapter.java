package pinklady.cartedorandroid;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by ETCHELECOU on 03/03/2017.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "PlacePicker", "CurrentPlace", "AutoComplete" };
    private Context context;
    private GoogleApiClient mGoogleApiClient;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context, GoogleApiClient googleApiClient) {
        super(fm);
        this.context = context;
        this.mGoogleApiClient = googleApiClient;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position)
        {
            case 0:
                fragment = PlacePickerFragment.newInstance(position + 1, mGoogleApiClient);
                break;
            case 1:
                fragment = CurrentPlaceFragment.newInstance(position + 1, mGoogleApiClient);
                break;
            case 2:
                fragment = AutoCompleteFragment.newInstance(position + 1, mGoogleApiClient);
                break;
            default:
                fragment = PlacePickerFragment.newInstance(position + 1, mGoogleApiClient);
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}