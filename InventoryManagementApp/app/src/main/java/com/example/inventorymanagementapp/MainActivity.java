package com.example.inventorymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonRegister;
    private Button buttonLogin;
    private Button buttonHelp;
    private ImageView imageViewTogglePassword;
    private boolean isPasswordVisible = false;
    private String baseURL = "http://172.30.80.1/InventoryApp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonHelp = findViewById(R.id.buttonHelp);
        imageViewTogglePassword = findViewById(R.id.imageViewTogglePassword);

        // Toggle password visibility
        imageViewTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // Help button click listener
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHelpActivity();
            }
        });

        // Register button click listener
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewAccount();
            }
        });

        // Login button click listener
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLoginPage();
            }
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageViewTogglePassword.setImageResource(R.drawable.visibilityon_icon);
        } else {
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageViewTogglePassword.setImageResource(R.drawable.visibilityoff_icon);
        }
        isPasswordVisible = !isPasswordVisible;
        editTextPassword.setSelection(editTextPassword.getText().length());
    }

    private void openHelpActivity() {
        Intent intent = new Intent(MainActivity.this, HelpActivity.class);
        startActivity(intent);
    }

    private void registerNewAccount() {
        final String username = editTextUsername.getText().toString().trim().toLowerCase();
        final String password = editTextPassword.getText().toString().trim();

        if (validateInput(username, password)) {
            sendRegisterRequest(username, password);
        }
    }

    private boolean validateInput(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(username)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password must be at least 8 characters long and contain at least one number, one special character, and one letter", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        // Use a regular expression to ensure proper email format (not just @gmail.com)
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+$";
        return Pattern.matches(emailPattern, email);
    }

    private boolean isValidPassword(String password) {
        // Password must be at least 8 characters long and contain a number, letter, and special character
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(passwordPattern);
    }

    private void sendRegisterRequest(final String username, final String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = baseURL + "register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleRegisterResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleRegisterError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void handleRegisterResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String message = jsonObject.getString("message");

            if (message.equalsIgnoreCase("Username taken")) {
                Toast.makeText(MainActivity.this, "Username is already taken", Toast.LENGTH_SHORT).show();
            } else if (message.equalsIgnoreCase("Registration successful")) {
                Toast.makeText(MainActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                editTextUsername.setText("");
                editTextPassword.setText("");
            } else {
                Toast.makeText(MainActivity.this, "Registration failed: " + message, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Failed to parse response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleRegisterError(VolleyError error) {
        Log.e("VolleyError", "Error: " + error.toString());
        String errorMessage = error.networkResponse != null ?
                "Network error: " + new String(error.networkResponse.data) :
                "Network error: " + error.getMessage();
        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void navigateToLoginPage() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
