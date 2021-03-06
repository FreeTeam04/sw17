package at.sw2017.financesolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;
import at.sw2017.financesolution.models.Transaction;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_HOME_FRAGMENT = "HomeFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View _view;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public HomeFragment newInstance(String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            refreshView();
        }
    }

    private void refreshView() {
        final ListView transactionListView = (ListView) _view.findViewById(R.id.frag_home_transaction_listview);
        ArrayList<Transaction> lastTransactionsList = FinanceDataConnectorImpl.getInstance(getContext()).getLastTransactions(5);

        TransactionListViewAdapter transactionListViewAdapter = new TransactionListViewAdapter(getActivity(), lastTransactionsList);
        transactionListView.setAdapter(transactionListViewAdapter);
        transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Transaction transaction = (Transaction) parent.getItemAtPosition(position);
                Log.i(LOG_HOME_FRAGMENT, "Going to edit Transaction (id = " + transaction.getId() + ", amount = "+ transaction.getAmount() +").");
                Intent editTransactionIntent = new Intent(getContext(), AddTransactionActivity.class);
                editTransactionIntent.putExtra("EDIT", transaction.getId());
                // At this point requestcode is 0xADD, but later it should be something else like 0xED17
                startActivityForResult(editTransactionIntent, 0xADD);
            }
        });

        ArrayList<Transaction> transactionsList = FinanceDataConnectorImpl.getInstance(getContext()).getAllTransactions();
        double balance = 0;
        for(Transaction transaction : transactionsList) {
            balance += transaction.getAmount();
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String currencySymbol = sharedPref.getString("currency_symbol", "€");
        double budget = Double.valueOf(sharedPref.getString("budget", "0.00"));
        final TextView balanceView = (TextView) _view.findViewById(R.id.frag_home_balance);

        balanceView.setText(String.format("%.2f", balance) + " " + currencySymbol + "\nBudget: " + String.format("%.2f", budget) + " " + currencySymbol);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_home, container, false);

        // Add button event for AddTransaction button and start Add Transaction-Activity if clicked
        /*final Button button = (Button) _view.findViewById(R.id.frag_home_btn_add_transaction);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddTransactionActivity.class), 0xADD);
            }
        });*/

        refreshView();

        return _view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_HOME_FRAGMENT, "onResume() called.");
        refreshView();
    }
}
