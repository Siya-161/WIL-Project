package com.example.inventorymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonRegister;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Set button backgrounds using selector drawable
        buttonRegister.setBackgroundResource(R.drawable.button_selector);
        buttonLogin.setBackgroundResource(R.drawable.button_selector);

        // Set click listener for Register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewAccount();
            }
        });

        // Set click listener for Login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLoginPage();
            }
        });

        // Set focus change listener for buttons
        buttonRegister.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                buttonRegister.setTextColor(hasFocus ? getResources().getColor(R.color.button_focused_colour) :
                        getResources().getColor(R.color.button_color));
            }
        });

        buttonLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                buttonLogin.setTextColor(hasFocus ? getResources().getColor(R.color.button_focused_text_color) :
                        getResources().getColor(R.color.button_text_color));
            }
        });
    }

    // Method to handle registration of a new account
    private void registerNewAccount() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Check if username or password is empty
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform additional validation checks
        if (!username.contains("@gmail.com")) {
            Toast.makeText(this, "Username must contain @gmail.com", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8 || !password.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,}$")) {
            Toast.makeText(this, "Password must be 8 characters or more and contain special characters and numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show registration successful message
        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

        // Clear the text fields after registration
        editTextUsername.setText("");
        editTextPassword.setText("");
    }

    // Method to navigate to the login page
    private void navigateToLoginPage() {
        // Perform action to navigate to the login page
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
