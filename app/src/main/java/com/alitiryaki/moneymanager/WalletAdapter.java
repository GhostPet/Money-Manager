package com.alitiryaki.moneymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alitiryaki.moneymanager.classes.User;
import com.alitiryaki.moneymanager.classes.Wallet;

import java.util.ArrayList;

public class WalletAdapter extends ArrayAdapter<Wallet> {

    ArrayList<Wallet> wallets;

    public WalletAdapter(Context context, ArrayList<Wallet> wallets) {
        super(context, 0, wallets);
        this.wallets = wallets;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Wallet wallet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.wallet_item, parent, false);
        }

        TextView walletNameTextView = convertView.findViewById(R.id.walletNameTextView);
        TextView walletBalanceTextView = convertView.findViewById(R.id.walletBalanceTextView);
        ImageButton deleteWalletImageButton = convertView.findViewById(R.id.deleteWalletImageButton);

        if (wallet != null) {
            walletNameTextView.setText(wallet.getName());
            walletBalanceTextView.setText(String.format("Bakiye: %s TL", wallet.getBalance()));

            // Silme butonuna tıklama dinleyicisi ekle
            deleteWalletImageButton.setOnClickListener(view -> {
                User.deleteWallet(wallet.getId());
                wallets.remove(wallet);
                notifyDataSetChanged();
            });
        }

        return convertView;
    }
}
