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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
        TransactionFragment.OnFragmentInteractionListener,
        ReportsFragment.OnFragmentInteractionListener {

    private static final int ADD_REMINDER_ACTIVITY = 2;

    private ViewPager viewPager;
    private com.github.clans.fab.FloatingActionMenu floatingActionButton;
    private com.github.clans.fab.FloatingActionButton floatingActionButtonTransactions,
            floatingActionButtonReminders, floatingActionButtonCategories;

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
                Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);
                startActivityForResult(intent, ADD_REMINDER_ACTIVITY);
            }
        });

        floatingActionButtonCategories = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.floating_action_button_categories);
        floatingActionButtonCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: create AddCategoryActivity
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.settings) {
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settings);

            return true;
        }

        if(id == R.id.reminders) {
            Intent reminders = new Intent(MainActivity.this, ReminderActivity.class);
            startActivity(reminders);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onResume() {
        super.onResume();
        floatingActionButton = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.floating_action_button);
        if(floatingActionButton.isOpened()) {
            floatingActionButton.toggle(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_REMINDER_ACTIVITY) {
            Toast.makeText(this, "Reminder was added.", Toast.LENGTH_LONG).show();
        }
    }
}
