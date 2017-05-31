package at.sw2017.financesolution;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
        TransactionFragment.OnFragmentInteractionListener,
        ReportsFragment.OnFragmentInteractionListener {

    private ViewPager viewPager;
    private com.github.clans.fab.FloatingActionMenu floatingActionButton;
    private com.github.clans.fab.FloatingActionButton floatingActionButtonTransactions, floatingActionButtonReminders;

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

        floatingActionButton = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionButtonTransactions.setVisibility(View.VISIBLE);
                floatingActionButtonReminders.setVisibility(View.VISIBLE);
            }
        });

        floatingActionButtonTransactions = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.floating_action_button_transactions);
        floatingActionButtonTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
                startActivityForResult(intent, 0xADD);
            }
        });

        floatingActionButtonReminders = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.floating_action_button_reminders);
        floatingActionButtonReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: create AddReminderActivity
                /*Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);
                startActivityForResult(intent, 0xADD);*/
            }
        });

        viewPager = (ViewPager) findViewById(R.id.pager);

        PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // use this to make it work with swiping AND clicking tabs
        tabLayout.setupWithViewPager(viewPager);

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
