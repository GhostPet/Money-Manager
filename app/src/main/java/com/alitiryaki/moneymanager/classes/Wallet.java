package com.alitiryaki.moneymanager.classes;

import androidx.annotation.NonNull;

public class Wallet {
    private final long id;
    private String name;
    private final long userId;

    public Wallet(long id, String name, long userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getBalance() {
        return String.valueOf(User.getBalance(id));
    }
}
