<?php
header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "personinfo";

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

    $hashed_password = password_hash($password, PASSWORD_DEFAULT);

    $stmt = $conn->prepare("SELECT * FROM users WHERE username = ?");
    if (!$stmt) {
        echo json_encode(array('message' => 'Prepare failed: (' . $conn->errno . ') ' . $conn->error));
        exit;
    }
    
    $stmt->bind_param("s", $username);
    if (!$stmt->execute()) {
        echo json_encode(array('message' => 'Execute failed: (' . $stmt->errno . ') ' . $stmt->error));
        exit;
    }
    
    $result = $stmt->get_result();
    if (!$result) {
        echo json_encode(array('message' => 'Get result failed: (' . $stmt->errno . ') ' . $stmt->error));
        exit;
    }

    if ($result->num_rows > 0) {
        echo json_encode(array('message' => 'Username already taken'));
    } else {
        $stmt = $conn->prepare("INSERT INTO users (username, password, created_at, updated_at) VALUES (?, ?, NOW(), NOW())");
        if (!$stmt) {
            echo json_encode(array('message' => 'Prepare failed: (' . $conn->errno . ') ' . $conn->error));
            exit;
        }
        
        $stmt->bind_param("ss", $username, $hashed_password);
        if ($stmt->execute()) {
            echo json_encode(array('message' => 'Registration successful'));
        } else {
            echo json_encode(array('message' => 'Error: Registration failed. (' . $stmt->errno . ') ' . $stmt->error));
        }
    }

    $stmt->close();
}

$conn->close();
?>
