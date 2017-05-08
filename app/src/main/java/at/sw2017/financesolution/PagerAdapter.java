package at.sw2017.financesolution;

import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by JK on 26.04.17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private final static String LOG_INFO_TAG = "PagerAdapter";

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                Log.i(LOG_INFO_TAG, "Switched to home fragment.");
                return homeFragment;
            case 1:
                TransactionFragment transactionFragment = new TransactionFragment();
                Log.i(LOG_INFO_TAG, "Switched to transaction fragment.");
                return transactionFragment;
            case 2:
                ReportsFragment reportsFragment = new ReportsFragment();
                Log.i(LOG_INFO_TAG, "Switched to reports fragment.");
                return reportsFragment;
            default:
                HomeFragment defaultFragment = new HomeFragment();
                Log.i(LOG_INFO_TAG, "Switched to default fragment.");
                return defaultFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Transactions";
            case 2:
                return "Reports";
            default:
                return null;
        }

    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
