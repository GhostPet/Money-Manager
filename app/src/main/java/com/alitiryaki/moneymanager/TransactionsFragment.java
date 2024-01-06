package com.alitiryaki.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alitiryaki.moneymanager.classes.User;
import com.alitiryaki.moneymanager.classes.Transaction;
import com.alitiryaki.moneymanager.classes.Wallet;

import java.util.ArrayList;

public class TransactionsFragment extends Fragment {

    private ArrayList<Transaction> transactionList;

    public TransactionsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTransactions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        // İşlem Listesi
        ListView transactionsListView = view.findViewById(R.id.transactionsListView);
        ArrayAdapter<Transaction> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, transactionList);
        transactionsListView.setAdapter(adapter);

        // Üstteki Cüzdan Seçimi
        Spinner walletsSpinner = view.findViewById(R.id.walletsSpinner);
        loadWallets(walletsSpinner);
        walletsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Seçilen cüzdana göre işlemleri yükle
                long selectedWalletId = ((Wallet) walletsSpinner.getSelectedItem()).getId();
                loadTransactions(selectedWalletId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                loadTransactions();
            }
        });

        // Yeni İşlem Ekle Butonu
        view.findViewById(R.id.addTransactionButton).setOnClickListener(v -> openAddTransactionActivity());

        return view;
    }

    private void openAddTransactionActivity() {
        Intent intent = new Intent(getContext(), AddTransactionActivity.class);
        startActivity(intent);
    }

    // İşlemleri yükle
    private void loadTransactions() {
        transactionList = User.getTransactions();
    }

    private void loadWallets(Spinner walletsSpinner) {
        ArrayList<Wallet> wallets = User.getWallets();
        wallets.add(0, new Wallet(-1, "Tümünü Seç", User.getUserId()));
        ArrayAdapter<Wallet> walletAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, wallets);
        walletAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        walletsSpinner.setAdapter(walletAdapter);
    }

    private void loadTransactions(long selectedWalletId) {
        if (selectedWalletId == -1) {
            transactionList = User.getTransactions();
        } else {
            transactionList = User.getTransactions(selectedWalletId);
        }
        ArrayAdapter<Transaction> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, transactionList);
        ((ListView) getView().findViewById(R.id.transactionsListView)).setAdapter(adapter);
    }
}
