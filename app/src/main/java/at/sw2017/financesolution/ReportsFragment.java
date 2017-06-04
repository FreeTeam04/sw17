package at.sw2017.financesolution;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;
import at.sw2017.financesolution.helper.ValueFormatterCharts;
import at.sw2017.financesolution.models.Category;

public class ReportsFragment extends Fragment {

    private FinanceDataConnector financeDataConnector;

    private OnFragmentInteractionListener mListener;

    public ReportsFragment() {
        // Required empty public constructor
    }
    
    private Float calcSumOfExpenses(Map<String, Float> spendingByCategory)
    {
        Iterator it = spendingByCategory.entrySet().iterator();
        Float sumOfExpenses = 0.0f;
        while (it.hasNext()) {
            Map.Entry<String, Float> pair = (Map.Entry)it.next();
            sumOfExpenses = sumOfExpenses + pair.getValue();
        }
        return sumOfExpenses;
    }

    private void createPieChartForYearOverview(View view) {
        Map<String, Float> spendingByCategory = financeDataConnector.getSpendingPerCategoryForCurrentYear();

        PieChart pieChart = (PieChart) view.findViewById(R.id.chartExpensesPie);
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterTextOffset(0,50);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        pieChart.setTransparentCircleRadius(0.0f);
        pieChart.setHoleRadius(20.0f);
        pieChart.setUsePercentValues(false);
        pieChart.setDescription(null);

        List<PieEntry> entries = new ArrayList<>();

        Iterator it = spendingByCategory.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Float> pair = (Map.Entry)it.next();
            float value = Math.abs(pair.getValue());
            PieEntry entry = new PieEntry(value, pair.getKey());

            entries.add(entry);
        }

        Legend legend = pieChart.getLegend();
        legend.setTextSize(12.0f);
        legend.setTextColor(Color.argb(255, 60, 60, 60));
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(ColorTemplate.MATERIAL_COLORS);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String currencySymbol = sharedPref.getString("currency_symbol", "€");
        ValueFormatterCharts valFormat = new ValueFormatterCharts(currencySymbol);

        PieData data = new PieData(set);
        data.setValueTextSize(14.0f);
        data.setValueTextColor(Color.argb(255, 60, 60, 60));
        data.setValueFormatter(valFormat);

        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }

    private void createBarChartForMonthlyBudgetOverview(View view) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        Float monthlyDummyBudget = Float.valueOf(sharedPref.getString("budget", "0.00"));

        Map<String, Float> spendingList = financeDataConnector.getSpendingPerCategoryForCurrentMonth();
        Float spendingSumThisMonth = calcSumOfExpenses(spendingList);

        BarChart barChart = (BarChart) view.findViewById(R.id.chartMonthlyBudgetChart);
        barChart.setDescription(null);
        barChart.setTouchEnabled(false);

        YAxis yAxis = barChart.getAxisRight();
        yAxis.setEnabled(false);

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0.0f);
        yAxisLeft.setAxisMaximum(monthlyDummyBudget + 100.0f);
        yAxisLeft.setEnabled(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1.0f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, Math.abs(spendingSumThisMonth)));
        BarDataSet set = new BarDataSet(entries, "Expenses");
        // set.setValueTextSize();
        set.setColors(ColorTemplate.MATERIAL_COLORS);

        List<BarEntry> entries2 = new ArrayList<>();
        entries2.add(new BarEntry(1f, monthlyDummyBudget));
        BarDataSet set2 = new BarDataSet(entries2, "Budget");

        set.setColor(Color.rgb(231,76,60));     // MPAndroidChart red
        set2.setColor(Color.rgb(46,204,113));   // MPAndroidChart green

        BarData data = new BarData(set, set2);
        data.setValueTextSize(14.0f);
        data.setValueTextColor(Color.argb(255, 60, 60, 60));

        ValueFormatterCharts valFormat = new ValueFormatterCharts(sharedPref.getString("currency_symbol", "€"));
        data.setValueFormatter(valFormat);

        data.setBarWidth(0.9f);     // set custom bar width
        barChart.setData(data);

        Legend legend = barChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setTextSize(12.0f);
        legend.setTextColor(Color.argb(255, 60, 60, 60));

        barChart.setFitBars(true);  // make the x-axis fit exactly all bars
        barChart.invalidate();      // refresh
    }

    private void updateData(View view) {
        createPieChartForYearOverview(view);
        createBarChartForMonthlyBudgetOverview(view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        financeDataConnector = FinanceDataConnectorImpl.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        updateData(view);
        return view;
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
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);

        if (visible) {
            if(getView() == null)
                return;

            updateData(getView());
        }
    }
}


