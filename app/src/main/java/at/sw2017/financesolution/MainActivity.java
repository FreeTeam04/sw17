package at.sw2017.financesolution;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
        TransactionFragment.OnFragmentInteractionListener,
        ReportsFragment.OnFragmentInteractionListener {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        TabLayout.Tab homeTab = tabLayout.newTab();
        homeTab.setText("Home");
        tabLayout.addTab(homeTab);

        TabLayout.Tab transactionsTab = tabLayout.newTab();
        transactionsTab.setText("Transactions");
        tabLayout.addTab(transactionsTab);

        TabLayout.Tab reportsTab = tabLayout.newTab();
        reportsTab.setText("Reports");
        tabLayout.addTab(reportsTab);

        viewPager = (ViewPager) findViewById(R.id.pager);

        PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // initialize data connection singleton by calling getInstance once
        FinanceDataConnector dataConnector =  FinanceDataConnectorImpl.getInstance(this.getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
