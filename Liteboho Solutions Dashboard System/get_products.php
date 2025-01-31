<?php
// Ensure no output before JSON
ob_start();  // Start output buffering to prevent any accidental output

// Headers to return JSON
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// Database connection settings
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "inventory_db";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode([
        "status" => "error",
        "message" => "Database connection failed: " . $conn->connect_error
    ]);
    exit();
}

// SQL query to fetch products from the items table
$sql = "SELECT id, name, quantity, price, description, category FROM items";
$result = $conn->query($sql);

// Check if query was successful
if ($result === false) {
    http_response_code(500);
    echo json_encode([
        "status" => "error",
        "message" => "SQL query failed: " . $conn->error
    ]);
    $conn->close();
    exit();
}

// Check if products exist
if ($result->num_rows > 0) {
    $products = [];
    while ($row = $result->fetch_assoc()) {
        $products[] = $row;
    }

    // Send the JSON response
    echo json_encode([
        "status" => "success",
        "products" => $products
    ]);
} else {
    // No products found
    echo json_encode([
        "status" => "error",
        "message" => "No products found"
    ]);
}

// Close the database connection
$conn->close();

// End output buffering and send the buffer contents
ob_end_flush();

