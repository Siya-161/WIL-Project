Liteboho Admin Dashboard ReadMe file
The Liteboho Solutions Admin Dashboard is a responsive web application designed to help users manage products, view inventory, generate reports, and track system metrics. It includes login and logout functionality, 
customizable charts, and a clean, user-friendly interface. The dashboard is solely meant to supplement the inventory management application and its functionality.

Table of Contents
1.	Features
2.	Tech Stack
3.	Installation and Setup
4.	Usage
5.	Troubleshooting
----------------------------
1.	Features
•	User Authentication: Login and logout functionality with session management.

•	Dashboard Overview: Displays key metrics in a card layout, including stock items, product categories, users, and alerts.

•	Data Visualization: Bar and area charts powered by ApexCharts, displaying top inventory items and other key data.

•	Responsive Design: Adapts to various screen sizes and devices.

•	Sidebar Navigation: Easily navigate between dashboard sections via the sidebar menu.

•	Error Handling and Caching Prevention: Includes session validation, error messages, and caching prevention to ensure secure and smooth operation.
----------------------------
2.	Tech Stack
•	Frontend: HTML, CSS (with responsive layout), JavaScript, and Google Material Icons.

•	Backend: PHP for server-side processing and session management.

•	Database: MySQL, used for storing user credentials and product information.

•	Charts: ApexCharts for data visualization.

•	AJAX and Fetch API: For handling asynchronous data loading.
----------------------------
3.	Installation and Setup
•	Clone the Repository: https://github.com/BlackSIYA11/WIL-Project/tree/main/LitebohoSolutionsDashboardSystem

•	Database Setup:

i.	Create a new MySQL database named inventory_db.

ii.	Import the database schema (e.g., from database.sql if provided in the repo) to set up necessary tables such as users and items.

•	Configure database connection: 

i.	In login.php, get_products.php, and any other PHP files requiring database access, set your database credentials: $servername = "localhost"; $username = "your_db_username"; $password = "your_db_password"; $dbname = "inventory_db";

•	Start Local Server:

i.	Use Apache or a similar web server to serve the files. If using XAMPP, place the project in the htdocs folder, if using WAMP place the project
in C:/wamp64/www

ii.	Start the server and navigate to http://localhost/Liteboho-Admin-Dashboard/login.html
----------------------------
4.	Usage
•	Login: Access the login page at login.html and enter your username and password.  Upon successful login, you will be redirected to index.html, where you can access the dashboard.

•	Logout: Use the Logout button located in the sidebar to log out and return to the login page. This action immediately clears the session data.

•	Viewing Data and Charts:  The main dashboard displays key metrics, including the number of stock items, users, and categories. Charts display top products and inventory levels. Data is dynamically fetched from the database.

•	Adding products: The dashboard is strictly for viewing purposes, adding products is done on the inventory management application, this will reflect on the dashboard.
----------------------------
5.	Troubleshooting
•	Logout Not Immediate: Ensure logout.php is configured to fully clear the session and cookies. Confirm the Cache-Control headers are properly set. If the logout action seems delayed, check for caching in the browser's Network tab in Developer Tools.

•	Database Connection Errors: Verify that your MySQL database credentials in login.php and other PHP files match your local setup. Confirm that the MySQL service is running and accessible on localhost.

•	Charts Not Displaying: Ensure that productsData is correctly stored in localStorage by products.html. Open the browser console to check for errors and confirm that localStorage contains the expected data.
