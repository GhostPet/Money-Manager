package com.alitiryaki.moneymanager.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alitiryaki.moneymanager.classes.Transaction;
import com.alitiryaki.moneymanager.classes.Wallet;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moneymanager2.db";
    private static final String USER_TABLE_NAME = "user_table";
    private static final String WALLET_TABLE_NAME = "wallet_table";
    private static final String TRANSACTION_TABLE_NAME = "transaction_table";


    // Kolonlar için user_table
    private static final String COL_USER_ID = "ID";
    private static final String COL_USERNAME = "USERNAME";
    private static final String COL_PASSWORD = "PASSWORD";

    // Kolonlar için wallet_table
    private static final String COL_WALLET_ID = "WALLET_ID";
    private static final String COL_WALLET_NAME = "WALLET_NAME";
    private static final String COL_USER_FK = "USER_ID";

    // Kolonlar için transaction_table
    private static final String COL_TRANSACTION_ID = "TRANSACTION_ID";
    private static final String COL_TRANSACTION_NAME = "NAME";
    private static final String COL_AMOUNT = "AMOUNT";
    private static final String COL_CATEGORY = "CATEGORY";
    private static final String COL_DESCRIPTION = "DESCRIPTION";
    private static final String COL_DATE = "DATE";
    private static final String COL_WALLET_FK = "WALLET_ID";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Kullanıcı tablosu
        db.execSQL("CREATE TABLE " + USER_TABLE_NAME + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT)");

        // Cüzdan tablosu
        db.execSQL("CREATE TABLE " + WALLET_TABLE_NAME + " (" +
                COL_WALLET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_WALLET_NAME + " TEXT, " +
                COL_USER_FK + " INTEGER, " +
                "FOREIGN KEY(" + COL_USER_FK + ") REFERENCES " + USER_TABLE_NAME + "(" + COL_USER_ID + "))");

        // İşlem tablosu
        db.execSQL("CREATE TABLE " + TRANSACTION_TABLE_NAME + " (" +
                COL_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TRANSACTION_NAME + " TEXT, " +
                COL_AMOUNT + " REAL, " +
                COL_CATEGORY + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_WALLET_FK + " INTEGER, " +
                "FOREIGN KEY(" + COL_WALLET_FK + ") REFERENCES " + WALLET_TABLE_NAME + "(" + COL_WALLET_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Tabloları sil
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WALLET_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);

        // Yeniden oluştur
        onCreate(db);
    }


    // ### KULLANICI İŞLEMLERİ ###


    // DB'ye yeni kullanıcı ekle
    public void UserRegister(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_PASSWORD, password);
        db.insert(USER_TABLE_NAME, null, contentValues);
        db.close();
    }

    // DB'den kullanıcı adı kontrolü yap
    public boolean UserUsernameCheck(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " + COL_USERNAME + " = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    // DB'den şifre kontrolü yap
    public boolean UserPasswordCheck(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    // DB'den kullanıcı adına göre kullanıcı id'sini getir
    public int UserGetId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " + COL_USERNAME + " = ?", new String[]{username});
        cursor.moveToFirst();
        @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex(COL_USER_ID));
        cursor.close();
        return userId;
    }

    // DB'de kullanıcı şifresini değiştir
    public void UserChangePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PASSWORD, newPassword);
        db.update(USER_TABLE_NAME, contentValues, COL_USERNAME + " = ?", new String[]{username});
        db.close();
    }

    // DB'den kullanıcı sil
    public void UserDelete(long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE_NAME, COL_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }


    // ### CÜZDAN İŞLEMLERİ ###


    // DB'ye yeni cüzdan ekle
    public void WalletAdd(long userId, String walletName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_WALLET_NAME, walletName);
        contentValues.put(COL_USER_FK, userId);
        db.insert(WALLET_TABLE_NAME, null, contentValues);
        db.close();
    }

    // DB'den kullanıcı id'sine göre cüzdanları getir
    public ArrayList<Wallet> WalletGet(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WALLET_TABLE_NAME + " WHERE " + COL_USER_FK + " = ?", new String[]{String.valueOf(userId)});

        ArrayList<Wallet> wallets = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") long walletId = cursor.getLong(cursor.getColumnIndex(COL_WALLET_ID));
            @SuppressLint("Range") String walletName = cursor.getString(cursor.getColumnIndex(COL_WALLET_NAME));
            wallets.add(new Wallet(walletId, walletName, userId));
        }

        cursor.close();
        return wallets;
    }

    // DB'den cüzdan sil
    public void WalletDelete(long walletId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(WALLET_TABLE_NAME, COL_WALLET_ID + " = ?", new String[]{String.valueOf(walletId)});
        db.close();
    }


    // ### İŞLEM İŞLEMLERİ ###


    // DB'ye yeni işlem ekle
    public void TransactionAdd(String name, double amount, String category, String description, String date, long walletId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TRANSACTION_NAME, name);
        contentValues.put(COL_AMOUNT, amount);
        contentValues.put(COL_CATEGORY, category);
        contentValues.put(COL_DESCRIPTION, description);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_WALLET_FK, walletId);
        db.insert(TRANSACTION_TABLE_NAME, null, contentValues);
        db.close();
    }

    // DB'den cüzdan id'sine göre işlemleri getir
    public ArrayList<Transaction> TransactionGet(long walletId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TRANSACTION_TABLE_NAME + " WHERE " + COL_WALLET_FK + " = ?", new String[]{String.valueOf(walletId)});

        ArrayList<Transaction> transactions = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") long transactionId = cursor.getLong(cursor.getColumnIndex(COL_TRANSACTION_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COL_TRANSACTION_NAME));
            @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COL_AMOUNT));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COL_CATEGORY));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COL_DATE));
            transactions.add(new Transaction(transactionId, name, amount, category, description, date, walletId));
        }

        cursor.close();
        return transactions;
    }

    // DB'den cüzdan id'sine göre bakiyeyi getir
    public double TransactionGetBalance(long walletId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_AMOUNT + ") FROM " + TRANSACTION_TABLE_NAME + " WHERE " + COL_WALLET_FK + " = ?", new String[]{String.valueOf(walletId)});
        cursor.moveToFirst();
        @SuppressLint("Range") double balance = cursor.getDouble(0);
        cursor.close();
        return balance;
    }

    // DB'den işlem sil
    public void TransactionDelete(long transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TRANSACTION_TABLE_NAME, COL_TRANSACTION_ID + " = ?", new String[]{String.valueOf(transactionId)});
        db.close();
    }
}
