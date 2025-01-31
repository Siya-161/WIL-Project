<?php
session_start();
header("Content-Type: application/json");

// Enable error reporting
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

echo json_encode(['loggedIn' => isset($_SESSION['username'])]);
?>