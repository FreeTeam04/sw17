package at.sw2017.financesolution;

/**
 * Created by hannes on 26.04.17.
 */

public class FinanceSolutionsCalculator {

    private FinanceDataConnector dataConnector = null;

    public FinanceSolutionsCalculator(FinanceDataConnector dataConnector)
    {
        this.dataConnector = dataConnector;

    }

    public static double getCurrentBalance()
    {
        throw new UnsupportedOperationException("Not implemented.");

    }


}
