package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class EmbeddedTransactionDAO implements TransactionDAO {

    SQLiteDatabase database;

    public EmbeddedTransactionDAO(SQLiteDatabase database){

        this.database=database;
    }


    @Override
    //insert values into transaction table
    public void logTransaction(Date date_, String accountNo, ExpenseType expenseType_, double amount_){

        SQLiteStatement sqlst = database.compileStatement("INSERT INTO Account_Transaction " +
                "(accountNo,expenseType,amount,date) VALUES (?,?,?,?)");

        sqlst.bindString(1,accountNo);
        sqlst.bindLong(2,(expenseType_ == ExpenseType.EXPENSE) ? 0 : 1);
        sqlst.bindDouble(3,amount_);
        sqlst.bindLong(4,date_.getTime());

        sqlst.executeInsert();

    }

    @Override
    //get all transactions
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM Account_Transaction", null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Transaction transaction=new Transaction(
                            new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                            cursor.getString(cursor.getColumnIndex("accountNo")),
                            (cursor.getInt(cursor.getColumnIndex("expenseType")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                            cursor.getDouble(cursor.getColumnIndex("amount")));

                    transactions.add(transaction);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> TransDetail = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM Account_Transaction LIMIT"+limit, null);


        if (cursor.moveToFirst()) {
            do {
                Transaction transaction=new Transaction(
                        new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                        cursor.getString(cursor.getColumnIndex("accountNo")),
                        (cursor.getInt(cursor.getColumnIndex("expenseType")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        cursor.getDouble(cursor.getColumnIndex("amount")));


                TransDetail.add(transaction);

            } while (cursor.moveToNext());
        }return  TransDetail;
    }





}

