package com.alitiryaki.moneymanager.classes;

import androidx.annotation.NonNull;

public class Transaction {

    private final long transactionId;
    private String name;
    private double amount;
    private String category;
    private String description;
    private String date;
    private final long walletId;

    public Transaction(long transactionId, String name, double amount, String category, String description, String date, long walletId) {
        this.transactionId = transactionId;
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
        this.walletId = walletId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public long getWalletId() {
        return walletId;
    }

    @NonNull
    @Override
    public String toString() {
        return name + "\nTutar: " + amount + " ₺\nAçıklama: " + description + "\nTarih: " + date;
    }
}
