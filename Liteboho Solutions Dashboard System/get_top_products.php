<?php 
// Allow CORS for testing purposes
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// Database connection settings
$servername = "localhost";
$username = "root"; // Default username for WAMP
$password = "";     // Default password for WAMP is empty
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

// SQL query to fetch top 5 products by quantity
$sql = "SELECT name, quantity FROM items ORDER BY quantity DESC LIMIT 5";
$result = $conn->query($sql);

if ($result === false) {
    // SQL query failed
    http_response_code(500);
    echo json_encode([
        "status" => "error",
        "message" => "SQL query failed: " . $conn->error
    ]);
    $conn->close();
    exit();
}

if ($result->num_rows > 0) {
    $products = [];
    // Fetch data and store it in an array
    while ($row = $result->fetch_assoc()) {
        $products[] = $row;
    }

    // Return the result as JSON
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

$conn->close();
?>
