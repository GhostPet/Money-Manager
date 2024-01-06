package com.alitiryaki.moneymanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alitiryaki.moneymanager.classes.User;
import com.alitiryaki.moneymanager.classes.Wallet;

import java.util.ArrayList;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText editTextTransactionDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        // Cüzdan seçenekleri
        Spinner walletsSpinner = findViewById(R.id.spinnerWallets);
        loadWallets(walletsSpinner);

        // Tarih seçici
        editTextTransactionDate = findViewById(R.id.editTextTransactionDate);

        // İşlem ekleme butonu
        findViewById(R.id.buttonAddTransaction).setOnClickListener(v -> addTransaction());
    }

    private void addTransaction() {
        // Formdaki verileri al
        String transactionName = ((EditText) findViewById(R.id.editTextTransactionName)).getText().toString();
        long walletId = ((Wallet) ((Spinner) findViewById(R.id.spinnerWallets)).getSelectedItem()).getId();
        double transactionAmount = Double.parseDouble(((EditText) findViewById(R.id.editTextTransactionAmount)).getText().toString());
        String transactionCategory = ((EditText) findViewById(R.id.editTextTransactionCategory)).getText().toString();
        String transactionDescription = ((EditText) findViewById(R.id.editTextTransactionDescription)).getText().toString();
        String transactionDate = ((EditText) findViewById(R.id.editTextTransactionDate)).getText().toString();

        // Boş veri kontrolü
        if (transactionName.isEmpty() || transactionCategory.isEmpty() || transactionDate.isEmpty()) {
            Toast.makeText(AddTransactionActivity.this, "Lütfen gerekli alanları doldurunuz.", Toast.LENGTH_SHORT).show();
            return;
        }

        // İşlem ekle
        User.addTransaction(transactionName, transactionAmount, transactionCategory, transactionDescription, transactionDate, walletId);

        // Geri dön
        Intent intent = new Intent(AddTransactionActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadWallets(Spinner walletsSpinner) {
        ArrayList<Wallet> wallets = User.getWallets();
        ArrayAdapter<Wallet> walletAdapter = new ArrayAdapter<>(AddTransactionActivity.this, android.R.layout.simple_spinner_item, wallets);
        walletAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        walletsSpinner.setAdapter(walletAdapter);
    }

    // Tarih ve saat seçici ekranı için onClick metodumuz
    public void pickDateTime(View view) {
        final Calendar calendar = Calendar.getInstance();

        // Tarih seçici ekranı
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (DatePicker datePicker, int year, int month, int day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            // Saat seçici ekranı
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (TimePicker timePicker, int hourOfDay, int minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                // Seçilen tarih ve saat formatı
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                String formattedDate = sdf.format(calendar.getTime());

                // Seçilen tarih ve saati EditText'e ekle
                editTextTransactionDate.setText(formattedDate);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

            // Saat seçici ekranını göster
            timePickerDialog.show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Tarih seçici ekranını göster
        datePickerDialog.show();
    }
}