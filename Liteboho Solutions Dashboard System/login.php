<?php
header("Access-Control-Allow-Origin: http://localhost");  // Update to match the origin of your HTML file
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

session_start();
header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "personinfo";

// Create a connection
$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die(json_encode(['message' => 'Connection failed: ' . $conn->connect_error]));
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = filter_var($_POST['username'], FILTER_SANITIZE_EMAIL);
    $password = $_POST['password'];

    if (empty($username) || empty($password)) {
        echo json_encode(['message' => 'Username or password cannot be empty']);
        exit;
    }

    if (!filter_var($username, FILTER_VALIDATE_EMAIL)) {
        echo json_encode(['message' => 'Invalid email format']);
        exit;
    }

    // Prepare the statement to select the plaintext password from the database
    $stmt = $conn->prepare("SELECT password FROM users WHERE username = ?");
    if (!$stmt) {
        echo json_encode(['message' => 'Prepare failed: (' . $conn->errno . ') ' . $conn->error]);
        exit;
    }
    
    $stmt->bind_param("s", $username);
    if (!$stmt->execute()) {
        echo json_encode(['message' => 'Execute failed: (' . $stmt->errno . ') ' . $stmt->error]);
        exit;
    }
    
    // Bind the result and fetch the plaintext password
    $stmt->bind_result($db_password);
    $stmt->fetch();
    
    // Directly compare plaintext passwords
    if ($password === $db_password) {
        $_SESSION['username'] = $username; // Set session variable on login
        echo json_encode(['message' => 'Login successful']);
    } else {
        echo json_encode(['message' => 'Login failed: Invalid username or password']);
    }

    $stmt->close();
}

$conn->close();
?>