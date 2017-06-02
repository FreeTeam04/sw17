package at.sw2017.financesolution.helper;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class ValueFormatterCharts implements IValueFormatter {
    private DecimalFormat mFormat;
    private String currency = "€";

    public ValueFormatterCharts(String currencySign) {
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        currency = currencySign;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return mFormat.format(value) + " " + currency; // e.g. append a dollar-sign
    }

}