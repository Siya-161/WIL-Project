package com.example.inventorymanagementapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteItem extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText editTextItemID;
    private EditText editTextItemName;
    private EditText editTextQuantity;
    private EditText editTextPrice;
    private EditText editTextDescription;
    private EditText editTextSearch; // Added editTextSearch
    private ItemDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteitem);

        // Initialize UI components
        Button buttonSearch = findViewById(R.id.buttonSearch);
        Button buttonDelete = findViewById(R.id.buttonDelete);
        Button buttonReturn = findViewById(R.id.buttonReturnToMenu);
        editTextItemID = findViewById(R.id.editTextItemID);
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextSearch = findViewById(R.id.editTextSearch); // Initialize editTextSearch
        dataSource = new ItemDataSource(this);
        dataSource.open();

        // Set up categories array
        final String[] categories = {"Engineering", "Road and Earthworks", "Repairs and Maintenance", "Construction", "Category 5"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerCategory.setAdapter(adapter);

        // Set listener for item selection
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                Toast.makeText(DeleteItem.this, "Selected category: " + selectedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set click listener for Search button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement search functionality
                searchItem();
            }
        });

        // Set click listener for Delete button
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation dialog before deleting
                showDeleteConfirmationDialog();
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

    // Method to search for an item by its ID or name
    private void searchItem() {
        String searchQuery = editTextSearch.getText().toString().trim();

        Cursor cursor = dataSource.getItemById(searchQuery);
        if (cursor == null || !cursor.moveToFirst()) {
            cursor = dataSource.getItemByName(searchQuery);
        }

        if (cursor != null && cursor.moveToFirst()) {
            editTextItemID.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_ID)));
            editTextItemName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_NAME)));
            editTextPrice.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE)));
            editTextQuantity.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY)));
            editTextDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)));
            setSpinnerSelection(spinnerCategory, cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY)));
            cursor.close();
        } else {
            clearFields();
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
        }
    }


    // Method to show a confirmation dialog before deleting
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform delete operation
                        deleteItem();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No, do nothing
                    }
                })
                .show();
    }

    // Method to delete the item
    private void deleteItem() {
        String itemID = editTextItemID.getText().toString().trim();
        int rowsAffected = dataSource.deleteItem(itemID);

        if (rowsAffected > 0) {
            // Item deleted successfully
            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
            // Clear text fields after deletion
            clearFields();
        } else {
            // Failed to delete item
            Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to clear the input fields
    private void clearFields() {
        editTextItemID.setText("");
        editTextItemName.setText("");
        editTextPrice.setText("");
        editTextQuantity.setText("");
        editTextDescription.setText("");
        spinnerCategory.setSelection(0);
    }

    // Method to return to the Inventory Page
    private void returnToInventoryPage() {
        // Return to the Inventory Page activity
        Intent intent = new Intent(DeleteItem.this, InventoryPage.class);
        startActivity(intent);
        finish(); // Finish this activity
    }

    // Method to set the spinner selection based on the category
    private void setSpinnerSelection(Spinner spinner, String category) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(category)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }
}
