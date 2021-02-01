package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.EmbeddedAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.EmbeddedTransactionDAO;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class EmbeddedDemoExpenseManager extends ExpenseManager{

    Context context;
    public EmbeddedDemoExpenseManager(Context con)  {
        context=con;
        try {
            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setup() throws ExpenseManagerException {

        //create db
        SQLiteDatabase database = context.openOrCreateDatabase("180475H", context.MODE_PRIVATE, null);
        //create Account Table
        database.execSQL("CREATE TABLE IF NOT EXISTS Account(" + "accountNo VARCHAR(50) PRIMARY KEY," + "bankName VARCHAR(50),"
                +"accountHolderName VARCHAR(50)," + "balance REAL" + " );");

        //create Transation Table
        database.execSQL("CREATE TABLE IF NOT EXISTS Account_Transaction(" +"Transaction_id INTEGER PRIMARY KEY," +
                "accountNo VARCHAR(50)," + "expenseType INT," + "amount REAL," + "date DATE," + "FOREIGN KEY (accountNo) REFERENCES Account(accountNo)" + ");");

        //Embeded DB
        EmbeddedAccountDAO accountDAO = new EmbeddedAccountDAO(database);

        setAccountsDAO(accountDAO);

        setTransactionsDAO(new EmbeddedTransactionDAO(database));

    }
}
