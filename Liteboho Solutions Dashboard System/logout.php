<?php
session_start();

// Completely remove all session variables
$_SESSION = [];

// If there's a session cookie, clear it by setting its expiration in the past
if (ini_get("session.use_cookies")) {
    $params = session_get_cookie_params();
    setcookie(session_name(), '', time() - 42000,
        $params["path"], $params["domain"],
        $params["secure"], $params["httponly"]
    );
}

// Destroy the session completely
session_destroy();

// Set headers to prevent caching
header("Cache-Control: no-store, no-cache, must-revalidate, max-age=0");
header("Expires: Mon, 01 Jan 1990 00:00:00 GMT");
header("Pragma: no-cache");

header("Content-Type: application/json");
echo json_encode(['message' => 'Logout successful']);
?>