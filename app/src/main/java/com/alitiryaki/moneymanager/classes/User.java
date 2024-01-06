package com.alitiryaki.moneymanager.classes;

import android.content.SharedPreferences;

import com.alitiryaki.moneymanager.database.DatabaseHelper;

import java.util.ArrayList;

public class User {

    private static String username;
    private static String password;
    private static DatabaseHelper databaseHelper;
    private static SharedPreferences sharedPreferences;

    public static void setSettings(DatabaseHelper dbh, SharedPreferences sp) {
        databaseHelper = dbh;
        sharedPreferences = sp;
    }

    public static void register(String username, String password) {
        User.username = username;
        User.password = password;
        databaseHelper.UserRegister(username, password);
        sharedPreferences.edit().putString("username", username).apply();
        sharedPreferences.edit().putString("password", password).apply();
    }

    public static void login(String username, String password) {
        User.username = username;
        User.password = password;
        sharedPreferences.edit().putString("username", username).apply();
        sharedPreferences.edit().putString("password", password).apply();
    }

    public static void logout() {
        User.username = null;
        User.password = null;
        sharedPreferences.edit().remove("username").apply();
        sharedPreferences.edit().remove("password").apply();
    }

    public static boolean isLoggedIn() {
        return User.username != null && User.password != null;
    }

    public static String getUsername() {
        return User.username;
    }

    public static long getUserId() {
        return databaseHelper.UserGetId(User.username);
    }

    public static boolean checkUsernameExist(String username) {
        return databaseHelper.UserUsernameCheck(username);
    }

    public static boolean checkPassword(String username, String password) {
        return databaseHelper.UserPasswordCheck(username, password);
    }

    public static void changePassword(String newPassword) {
        databaseHelper.UserChangePassword(User.username, newPassword);
    }

    public static void deleteAccount() {
        databaseHelper.UserDelete((int) User.getUserId());
        User.logout();
    }

    public static ArrayList<Wallet> getWallets() {
        return databaseHelper.WalletGet(User.getUserId());
    }

    public static void addWallet(String walletName) {
        databaseHelper.WalletAdd(User.getUserId(), walletName);
    }

    public static void deleteWallet(long walletId) {
        databaseHelper.WalletDelete(walletId);
    }

    public static void addTransaction(String transactionName, double transactionAmount, String transactionCategory, String transactionDescription, String transactionDate, long walletId) {
        databaseHelper.TransactionAdd(transactionName, transactionAmount, transactionCategory, transactionDescription, transactionDate, walletId);
    }

    public static ArrayList<Transaction> getTransactions() {
        ArrayList<Wallet> wallets = databaseHelper.WalletGet(User.getUserId());
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (Wallet wallet : wallets) {
            transactions.addAll(databaseHelper.TransactionGet(wallet.getId()));
        }
        return transactions;
    }

    public static ArrayList<Transaction> getTransactions(long walletId) {
        return databaseHelper.TransactionGet(walletId);
    }

    public static double getBalance(long walletid) {
        return databaseHelper.TransactionGetBalance(walletid);
    }
}
