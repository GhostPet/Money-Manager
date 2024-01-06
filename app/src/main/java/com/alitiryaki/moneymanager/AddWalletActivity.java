package com.alitiryaki.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alitiryaki.moneymanager.classes.User;

public class AddWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);

        EditText editTextWalletName = findViewById(R.id.editTextWalletName);
        Button buttonAddWallet = findViewById(R.id.buttonAddWallet);
        buttonAddWallet.setOnClickListener(v -> addWallet(editTextWalletName));
    }

    private void addWallet(EditText walletName) {
        if (!walletName.getText().toString().equals("")) {
            // Cüzdanı veritabanına ekle
            User.addWallet(walletName.getText().toString().trim());

            // Veritabanına cüzdan ekledikten sonra WalletsFragment'e geri dönebilirsiniz
            Intent intent = new Intent(AddWalletActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Lütfen Gerekli Alanları Doldurun mesajı gösterilebilir
            Toast.makeText(this, "Lütfen Gerekli Alanları Doldurun", Toast.LENGTH_SHORT).show();
        }
    }
}
