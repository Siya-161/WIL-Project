<?php
header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "personinfo";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die(json_encode(array('message' => 'Connection failed: ' . $conn->connect_error)));
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = filter_var($_POST['username'], FILTER_SANITIZE_EMAIL);
    $password = $_POST['password'];

    if (empty($username) || empty($password)) {
        echo json_encode(array('message' => 'Username or password cannot be empty'));
        exit;
    }

    if (!filter_var($username, FILTER_VALIDATE_EMAIL)) {
        echo json_encode(array('message' => 'Invalid email format'));
        exit;
    }

    $stmt = $conn->prepare("SELECT password FROM users WHERE username = ?");
    if (!$stmt) {
        echo json_encode(array('message' => 'Prepare failed: (' . $conn->errno . ') ' . $conn->error));
        exit;
    }
    
    $stmt->bind_param("s", $username);
    if (!$stmt->execute()) {
        echo json_encode(array('message' => 'Execute failed: (' . $stmt->errno . ') ' . $stmt->error));
        exit;
    }
    
    $stmt->bind_result($hashed_password);
    $stmt->fetch();

    if (password_verify($password, $hashed_password)) {
        echo json_encode(array('message' => 'Login successful'));
    } else {
        echo json_encode(array('message' => 'Login failed: Invalid username or password'));
    }

    $stmt->close();
}

$conn->close();
?>
