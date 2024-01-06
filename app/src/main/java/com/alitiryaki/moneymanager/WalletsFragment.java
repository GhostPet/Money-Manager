package com.alitiryaki.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.alitiryaki.moneymanager.classes.User;
import com.alitiryaki.moneymanager.classes.Wallet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WalletsFragment extends Fragment {

    public WalletsFragment() {
    }

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallets, container, false);

        // Listeleme
        ListView walletsListView = view.findViewById(R.id.walletsListView);
        ArrayList<Wallet> wallets = User.getWallets();
        WalletAdapter adapter = new WalletAdapter(getContext(), wallets);
        walletsListView.setAdapter(adapter);

        // Ekleme Butonu
        FloatingActionButton addTransactionButton = view.findViewById(R.id.addTransactionButton);
        addTransactionButton.setOnClickListener(v -> openAddWalletActivity());

        return view;
    }

    private void openAddWalletActivity() {
        Intent intent = new Intent(getContext(), AddWalletActivity.class);
        startActivity(intent);
    }
}
