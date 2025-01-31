package com.example.inventorymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddItem extends AppCompatActivity {

    private EditText editTextItemID;
    private EditText editTextItemName;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextDescription;
    private Spinner spinnerCategory;
    private Button buttonSaveItem;
    private Button buttonReturn;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        // Initialize UI components
        editTextItemID = findViewById(R.id.editTextItemID);
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSaveItem = findViewById(R.id.buttonSaveItem);
        buttonReturn = findViewById(R.id.buttonReturnMenu);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Set up categories array
        final String[] categories = {"Engineering", "Road and Earthworks", "Repairs and Maintenance", "Construction"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Set click listener for Save button
        buttonSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        // Set click listener for Return button
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToInventoryPage();
            }
        });
    }

    private void saveItem() {
        String itemID = editTextItemID.getText().toString().trim();
        String itemName = editTextItemName.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().trim();

        // Validate required fields
        if (itemID.isEmpty()) {
            Toast.makeText(this, "Item ID is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (itemName.isEmpty()) {
            Toast.makeText(this, "Item name is required", Toast.LENGTH_SHORT).show();
            return;
        } else if (itemName.length() < 3) {
            Toast.makeText(this, "Item name must be at least 3 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        if (priceStr.isEmpty()) {
            Toast.makeText(this, "Price is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Quantity is required", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        int quantity;

        // Validate numeric values
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                Toast.makeText(this, "Price must be a positive number", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                Toast.makeText(this, "Quantity must be a positive integer", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid quantity format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log the values for debugging
        Log.d("AddItem", "itemID: " + itemID);
        Log.d("AddItem", "itemName: " + itemName);
        Log.d("AddItem", "price: " + price);
        Log.d("AddItem", "quantity: " + quantity);
        Log.d("AddItem", "description: " + description);
        Log.d("AddItem", "category: " + category);

        // Create a string with the form-encoded data
        String postData = "itemID=" + itemID +
                "&itemName=" + itemName +
                "&price=" + price +
                "&quantity=" + quantity +
                "&description=" + description +
                "&category=" + category;

        String url = "http://172.30.80.1/InventoryApp/add_items.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AddItem.this, "Item saved successfully", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddItem.this, "Failed to save item", Toast.LENGTH_SHORT).show();
                        Log.e("AddItem", "Volley Error: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("itemID", itemID);
                params.put("itemName", itemName);
                params.put("price", String.valueOf(price));
                params.put("quantity", String.valueOf(quantity));
                params.put("description", description);
                params.put("category", category);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void clearFields() {
        editTextItemID.setText("");
        editTextItemName.setText("");
        editTextPrice.setText("");
        editTextQuantity.setText("");
        editTextDescription.setText("");
    }

    private void returnToInventoryPage() {
        Intent intent = new Intent(this, InventoryPage.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(this);
    }
}
