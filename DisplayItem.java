package com.example.inventorymanagementapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayItem extends AppCompatActivity {

    private ItemDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayitem);

        // Initialize the ItemDataSource
        dataSource = new ItemDataSource(this);
        dataSource.open();

        // Find the Return to Menu button
        Button buttonReturnToMenu = findViewById(R.id.buttonReturnToMenu);
        buttonReturnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic to return to the InventoryPage activity
                Intent intent = new Intent(DisplayItem.this, InventoryPage.class);
                startActivity(intent);
                finish(); // Finish this activity and return to the InventoryPage
            }
        });

        // Find the Display Total button and set its click listener
        Button buttonDisplayTotal = findViewById(R.id.buttonDisplayTotal);
        buttonDisplayTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTotalAmount();
            }
        });

        // Display items from the database
        displayItems();
    }

    private void displayItems() {
        // Retrieve all items from the database
        Cursor cursor = dataSource.getAllItems();

        // Add items to the layout dynamically
        if (cursor != null && cursor.moveToFirst()) {
            LinearLayout layoutItemsContainer = findViewById(R.id.layoutItemsContainer);
            do {
                // Retrieve item properties from the cursor
                @SuppressLint("Range") String itemID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_ID));
                @SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_NAME));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY));

                // Create TextViews for each item property
                TextView textViewItemID = createTextView("ID: " + itemID);
                TextView textViewItemName = createTextView("Name: " + itemName);
                TextView textViewQuantity = createTextView("Quantity: " + quantity);
                TextView textViewPrice = createTextView("Price: R" + price);
                TextView textViewDescription = createTextView("Description: " + description);
                TextView textViewCategory = createTextView("Category: " + category);

                // Add TextViews to the layout
                layoutItemsContainer.addView(textViewItemID);
                layoutItemsContainer.addView(textViewItemName);
                layoutItemsContainer.addView(textViewQuantity);
                layoutItemsContainer.addView(textViewPrice);
                layoutItemsContainer.addView(textViewDescription);
                layoutItemsContainer.addView(textViewCategory);

                // Add a separator line
                View separator = new View(this);
                separator.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2
                ));
                separator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                layoutItemsContainer.addView(separator);
            } while (cursor.moveToNext());

            // Close the cursor after use
            cursor.close();
        }
    }

    // Helper method to create TextView with specified text
    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        return textView;
    }


    private void displayTotalAmount() {
        // Retrieve all items from the database
        Cursor cursor = dataSource.getAllItems();

        double totalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY));

                // Calculate the total amount
                totalAmount += (price * quantity);
            } while (cursor.moveToNext());

            // Close the cursor after use
            cursor.close();
        }

        // Display the total amount as a Toast
        Toast.makeText(this, "Total Amount: R" + totalAmount, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
