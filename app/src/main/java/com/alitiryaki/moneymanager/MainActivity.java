package com.alitiryaki.moneymanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    int[] menuItems = {R.id.action_transactions, R.id.action_wallets, R.id.action_settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == menuItems[0]) {
                loadFragment(new TransactionsFragment());
            } else if (id == menuItems[1]) {
                loadFragment(new WalletsFragment());
            } else if (id == menuItems[2]) {
                loadFragment(new SettingFragment());
            }
            return true;
        });
    }

    private void loadFragment(Fragment Fragment) {
        if (Fragment != null) {
            androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, Fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
