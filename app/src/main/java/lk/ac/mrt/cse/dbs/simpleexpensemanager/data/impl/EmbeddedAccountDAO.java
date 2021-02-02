package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import java.util.ArrayList;
import java.util.List;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
//pts
// I implement
public class EmbeddedAccountDAO  implements AccountDAO {
    SQLiteDatabase database;
    public EmbeddedAccountDAO(SQLiteDatabase database){

        this.database = database;
    }

    @Override
    //account Numbs
    public List<String> getAccountNumbersList() {
        List<String> AccNumbs = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT accountNo FROM Account", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String test=cursor.getString(cursor
                            .getColumnIndex("accountNo"));
                    AccNumbs.add(test);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != (null) && !cursor.isClosed()) {
                cursor.close();
            }
        }return AccNumbs;
    }
    @Override

    //  get All account details from embeded DB
    public List<Account> getAccountsList() {
        List<Account> AccountDetails = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM Account", null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Account account=new Account(
                            cursor.getString(cursor.getColumnIndex("accountNo")),
                            cursor.getString(cursor.getColumnIndex("bankName")),
                            cursor.getString(cursor.getColumnIndex("accountHolderName")),
                            cursor.getDouble(cursor.getColumnIndex("balance")));
                    AccountDetails.add(account);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }return AccountDetails;

    }



    //particular account number details
    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor = database.rawQuery("SELECT * FROM Account where accountNo = "+accountNo, null);
        if (cursor != null)
            cursor.moveToFirst();

        Account par_Acc = new Account(
                cursor.getString(cursor.getColumnIndex("accountNo")),
                cursor.getString(cursor.getColumnIndex("bankName")),
                cursor.getString(cursor.getColumnIndex("accountHolderName")),
                cursor.getDouble(cursor.getColumnIndex("balance")));


        return par_Acc;

    }

    //add new account to Db
    @Override
    public void addAccount(Account account) {
        String sql = "INSERT INTO Account (accountNo,bankName,accountHolderName,balance) VALUES (?,?,?,?)";
        SQLiteStatement SQLst = database.compileStatement(sql);

        SQLst.bindString(1, account.getAccountNo());
        SQLst.bindString(2, account.getBankName());
        SQLst.bindString(3, account.getAccountHolderName());
        SQLst.bindDouble(4, account.getBalance());

        SQLst.executeInsert();
    }




    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {


        SQLiteStatement sqlst = database.compileStatement("DELETE FROM Account WHERE accountNo = ?");

        sqlst.bindString(1,accountNo);

        sqlst.executeUpdateDelete();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void updateBalance(String accountNo, ExpenseType expense_Type, double _amount) throws InvalidAccountException {

        SQLiteStatement statement = database.compileStatement("UPDATE Account SET balance = balance + ?");
        if(expense_Type == ExpenseType.EXPENSE){
            statement.bindDouble(1,-_amount);
        }else{
            statement.bindDouble(1,_amount);
        }

        statement.executeUpdateDelete();
    }
}

