package at.sw2017.financesolution;

import java.util.ArrayList;

/**
 * Created by hannes on 26.04.17.
 */

public class FinanceDataConnectorImpl implements FinanceDataConnector {

    private static FinanceDataConnector instance;

    private FinanceDataConnectorImpl() {
    }

    public static FinanceDataConnector getInstance() {
        if (instance == null) {
            instance = new FinanceDataConnectorImpl();
        }

        return instance;
    }

    @Override
    public ArrayList<Transaction> getAllTransactions() {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
