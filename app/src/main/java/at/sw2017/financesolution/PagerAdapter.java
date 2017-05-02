package at.sw2017.financesolution;

import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by JK on 26.04.17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                TransactionFragment transactionFragment = new TransactionFragment();
                return transactionFragment;
            case 2:
                ReportsFragment reportsFragment = new ReportsFragment();
                return reportsFragment;
            default:
                HomeFragment defaultFragment = new HomeFragment();
                return defaultFragment;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
