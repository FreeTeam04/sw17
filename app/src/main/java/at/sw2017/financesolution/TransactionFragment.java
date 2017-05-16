package at.sw2017.financesolution;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;
import at.sw2017.financesolution.models.Transaction;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FinanceDataConnector financeDataConnector;

    public EditText transactionSearchField;
    public ListView transactionListView;

    private TransactionListViewAdapter transactionListViewAdapter;

    private OnFragmentInteractionListener mListener;

    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance(String param1, String param2) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_transaction, container, false);

        financeDataConnector = FinanceDataConnectorImpl.getInstance(getActivity().getApplicationContext());

        transactionListView = (ListView) fragmentView.findViewById(R.id.transaction_list_view);
        final ArrayList<Transaction> transactionList = financeDataConnector.getAllTransactions();

        transactionListViewAdapter = new TransactionListViewAdapter(getActivity(), transactionList);
        transactionListView.setAdapter(transactionListViewAdapter);

        transactionSearchField = (EditText) fragmentView.findViewById(R.id.transaction_search_field);

        transactionSearchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String filterText = transactionSearchField.getText().toString();
                transactionListViewAdapter.filter(filterText);
                transactionListView.setAdapter(transactionListViewAdapter);
                //transactionListView.invalidate();
                //transactionListViewAdapter.notifyDataSetChanged();
            }
        });

        return fragmentView;
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

    public void refreshAdapter() {
        ArrayList<Transaction> transactionList = financeDataConnector.getAllTransactions();
        transactionListViewAdapter = new TransactionListViewAdapter(getActivity(), transactionList);
        transactionListView.setAdapter(transactionListViewAdapter);
        transactionListView.invalidate();
        this.transactionListViewAdapter.notifyDataSetChanged();
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
        Log.i("TransactionFragment", "onResume() called.");
        this.refreshAdapter();
    }
}
