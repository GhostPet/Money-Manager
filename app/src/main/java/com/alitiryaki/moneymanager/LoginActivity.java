package com.alitiryaki.moneymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alitiryaki.moneymanager.classes.User;
import com.alitiryaki.moneymanager.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        User.setSettings(new DatabaseHelper(LoginActivity.this), sharedPreferences);

        // Otomatik giriş
        String savedUsername = sharedPreferences.getString("username", null);
        String savedPassword = sharedPreferences.getString("password", null);
        if (savedUsername != null && savedPassword != null) {
            if (User.checkPassword(savedUsername, savedPassword)) {
                User.login(savedUsername, savedPassword);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        // Kayıt işlemi
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (username.equals("") || password.equals("")) {
                Toast.makeText(LoginActivity.this, "Kullanıcı adı veya şifre boş olamaz.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (User.checkUsernameExist(username)) {
                Toast.makeText(LoginActivity.this, "Bu kullanıcı adı zaten kullanılıyor.", Toast.LENGTH_SHORT).show();
                return;
            }

            User.register(username, password);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Giriş işlemi
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (username.equals("") || password.equals("")) {
                Toast.makeText(LoginActivity.this, "Kullanıcı adı veya şifre boş olamaz.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!User.checkPassword(username, password)) {
                Toast.makeText(LoginActivity.this, "Kullanıcı adı veya şifre yanlış.", Toast.LENGTH_SHORT).show();
                return;
            }

            User.login(username, password);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
