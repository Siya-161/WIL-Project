package com.example.inventorymanagementapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditItem extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText editTextSearch;
    private EditText editTextItemID;
    private EditText editTextItemName;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextDescription;
    private Button buttonSearchItem;
    private Button buttonSaveChanges;
    private Button buttonReturnToMenu;
    private ItemDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edititem);

        // Initialize UI components
        editTextSearch = findViewById(R.id.editTextSearch);
        editTextItemID = findViewById(R.id.editTextItemID);
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSearchItem = findViewById(R.id.buttonSearch);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        buttonReturnToMenu = findViewById(R.id.buttonReturnToMenu);
        dataSource = new ItemDataSource(this);
        dataSource.open();

        // Set click listener for "Search Item" button
        buttonSearchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem();
            }
        });

        // Set click listener for "Save Changes" button
        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSaveChanges();
            }
        });

        // Set click listener for "Return to Menu" button
        buttonReturnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMenu();
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

    // Method to prompt the user for confirmation before saving changes
    private void confirmSaveChanges() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to save the changes?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveChanges();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.create().show();
    }

    // Method to save the changes made to the item
    private void saveChanges() {
        String itemID = editTextItemID.getText().toString().trim();
        String itemName = editTextItemName.getText().toString().trim();
        double price = Double.parseDouble(editTextPrice.getText().toString().trim());
        int quantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
        String description = editTextDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        int rowsAffected = dataSource.updateItem(itemID, itemName, price, quantity, description, category);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to save changes", Toast.LENGTH_SHORT).show();
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

    // Method to set the spinner selection based on the category
    private void setSpinnerSelection(Spinner spinner, String category) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(category)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    // Method to return to the InventoryPage activity
    private void returnToMenu() {
        Intent intent = new Intent(EditItem.this, InventoryPage.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }
}
