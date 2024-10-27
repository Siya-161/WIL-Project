<?php
// Ensure no output before JSON
ob_start();  // Start output buffering to prevent any accidental output

// Headers to return JSON
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// Enable error reporting for debugging (can be disabled in production)
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// Database connection settings
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "personinfo";  // Updated to use the "personinfo" database

// Create a connection to the database
$conn = new mysqli($servername, $username, $password, $dbname);

// Check the connection
if ($conn->connect_error) {
    // Return a JSON response for a failed connection
    echo json_encode(["status" => "error", "message" => "Connection failed: " . $conn->connect_error]);
    exit();  // Stop further execution
}

// SQL query to count users in the "users" table
$sql = "SELECT COUNT(*) as user_count FROM users";  // Updated query for the "users" table
$result = $conn->query($sql);

// Check if the query returned a result
if ($result && $result->num_rows > 0) {
    $row = $result->fetch_assoc();
    // Return the count of users as a JSON response
    echo json_encode(["status" => "success", "user_count" => $row['user_count']]);
} else {
    // Return an error message if no users were found
    echo json_encode(["status" => "error", "message" => "No users found."]);
}

// Close the database connection
$conn->close();
?>
