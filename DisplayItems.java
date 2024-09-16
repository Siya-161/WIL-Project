package com.example.inventorymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayItems extends AppCompatActivity {

    private LinearLayout layoutItemsContainer;
    private TextView textViewTotalAmount;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayitems);

        // Initialize UI components
        layoutItemsContainer = findViewById(R.id.layoutItemsContainer);
        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        Button buttonDisplayTotal = findViewById(R.id.buttonDisplayTotal);
        Button buttonReturnToMenu = findViewById(R.id.buttonReturnToMenu);
        Button buttonViewItems = findViewById(R.id.buttonViewItems);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Set click listener for Display Total Amount button
        buttonDisplayTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchTotalAmount();  // Fetch and display total amount when the button is clicked
            }
        });

        // Set click listener for View Items button
        buttonViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayItems.this, ViewItemsActivity.class);
                startActivity(intent);
            }
        });


        // Set click listener for Return to Menu button
        buttonReturnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMenu();
            }
        });
    }

    private void fetchItems() {
        String url = "http://172.23.128.1/InventoryApp/display_items.php";
        Log.d("DisplayItems", "Fetching items from URL: " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("DisplayItems", "Response: " + response.toString());
                        layoutItemsContainer.removeAllViews(); // Clear previous views
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject item = response.getJSONObject(i);

                                String itemDetails = "ID: " + item.getString("id") +
                                        ", Name: " + item.getString("name") +
                                        ", Price: R" + item.getDouble("price") +
                                        ", Quantity: " + item.getInt("quantity") +
                                        ", Description: " + item.getString("description") +
                                        ", Category: " + item.getString("category");

                                // Create a TextView for each item and add it to the layout
                                TextView textView = new TextView(DisplayItems.this);
                                textView.setText(itemDetails);
                                textView.setTextSize(16);
                                layoutItemsContainer.addView(textView);
                            } catch (JSONException e) {
                                Log.e("DisplayItems", "Error parsing item data", e);
                                Toast.makeText(DisplayItems.this, "Error parsing item data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DisplayItems", "Volley error: " + error.toString());
                        Toast.makeText(DisplayItems.this, "Failed to retrieve items", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void fetchTotalAmount() {
        String url = "http://172.23.128.1/InventoryApp/display_total_amount.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("DisplayItems", "Response: " + response.toString());
                        try {
                            double totalAmount = response.getDouble("total_amount");
                            textViewTotalAmount.setText("Total Amount: R" + String.format("%.2f", totalAmount));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DisplayItems.this, "Error parsing total amount data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DisplayItems", "Error: " + error.toString());
                        Toast.makeText(DisplayItems.this, "Failed to retrieve total amount", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void returnToMenu() {
        Intent intent = new Intent(this, InventoryPage.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
    }
}

