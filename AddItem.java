package com.example.inventorymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddItem extends AppCompatActivity {

    private EditText editTextItemID;
    private EditText editTextItemName;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextDescription;
    private Spinner spinnerCategory;
    private Button buttonSaveItem;
    private Button buttonReturn;
    private ItemDataSource dataSource;

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

        // Initialize the dataSource
        dataSource = new ItemDataSource(this);
        dataSource.open(); // Open the database connection

        // Set up categories array
        final String[] categories = {"Engineering", "Road and Earthworks", "Repairs and Maintenance", "Construction", "Category 5"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
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

    // Method to handle saving the item
    private void saveItem() {
        // Retrieve data from EditText fields
        String itemID = editTextItemID.getText().toString().trim();
        String itemName = editTextItemName.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().trim();

        // Validate if required fields are not empty
        if (itemID.isEmpty() || itemName.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert price and quantity to appropriate data types
        double price = Double.parseDouble(priceStr);
        int quantity = Integer.parseInt(quantityStr);

        // Insert item into the database
        long insertedItemId = dataSource.insertItem(itemID, itemName, price, quantity, description, category);

        if (insertedItemId != -1) {
            // Item inserted successfully
            Toast.makeText(this, "Item saved successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            // Failed to insert item
            Toast.makeText(this, "Failed to save item", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to clear EditText fields
    private void clearFields() {
        editTextItemID.setText("");
        editTextItemName.setText("");
        editTextPrice.setText("");
        editTextQuantity.setText("");
        editTextDescription.setText("");
    }

    // Method to navigate back to InventoryPage
    private void returnToInventoryPage() {
        Intent intent = new Intent(this, InventoryPage.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close(); // Close the database connection when activity is destroyed
    }
}
