package com.alitiryaki.moneymanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alitiryaki.moneymanager.classes.User;

public class SettingFragment extends Fragment {

    public SettingFragment() {
    }

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView welcomeTextView = view.findViewById(R.id.welcomeTextView);
        String welcomeText = getString(R.string.hello) + " " + User.getUsername();
        welcomeTextView.setText(welcomeText);

        EditText currentPasswordEditText = view.findViewById(R.id.currentPasswordEditText);
        EditText newPasswordEditText = view.findViewById(R.id.newPasswordEditText);
        Button changePasswordButton = view.findViewById(R.id.changePasswordButton);
        Button logoutButton = view.findViewById(R.id.logoutButton);
        Button deleteAccountButton = view.findViewById(R.id.deleteAccountButton);

        changePasswordButton.setOnClickListener(v -> {
            String currentPassword = currentPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();

            if (currentPassword.equals("") || newPassword.equals("")) {
                Toast.makeText(requireContext(), "Mevcut şifre veya yeni şifre boş olamaz.", Toast.LENGTH_SHORT).show();
                return;
            } else if (!User.checkPassword(User.getUsername(), currentPassword)) {
                Toast.makeText(requireContext(), "Mevcut şifreniz yanlış.", Toast.LENGTH_SHORT).show();
                return;
            }

            User.changePassword(newPassword);
            currentPasswordEditText.setText("");
            newPasswordEditText.setText("");
            Toast.makeText(requireContext(), "Şifreniz başarıyla değiştirildi.", Toast.LENGTH_SHORT).show();
        });
        logoutButton.setOnClickListener(v -> {
            User.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
        deleteAccountButton.setOnClickListener(v -> showDeleteAccountDialog());
        return view;
    }

    private void showDeleteAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.deleteaccountdialog);
        builder.setMessage(R.string.deleteaccountdialog2);

        builder.setPositiveButton("Evet", (dialog, which) -> {
            User.deleteAccount();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
        builder.setNegativeButton("Hayır", (dialog, which) -> {});
        builder.show();
    }
}
