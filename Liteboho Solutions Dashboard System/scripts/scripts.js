document.addEventListener('DOMContentLoaded', function() {
    fetchProductData(); // Fetch product data on page load
    // fetchBarChartData(); // Example function call, replace with your actual function
});

// SIDEBAR TOGGLE
let sidebarOpen = false;
const sidebar = document.getElementById('sidebar');

function openSidebar() {
    if (!sidebarOpen) {
        sidebar.classList.add('sidebar-responsive');
        sidebarOpen = true;
    }
}

function closeSidebar() {
    if (sidebarOpen) {
        sidebar.classList.remove('sidebar-responsive');
        sidebarOpen = false;
    }
}

// FUNCTION TO FETCH PRODUCT DATA

function fetchProductData() {
    fetch('http://localhost/Dashboard/get_products.php')
    // Updated URL path
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const tableBody = document.getElementById('products-data');
            if (!tableBody) {
                console.error('Table body element not found');
                return;
            }
            tableBody.innerHTML = ''; // Clear the table before populating

            if (data.status === 'success' && Array.isArray(data.products)) {
                data.products.forEach(product => {
                    const row = `
                        <tr>
                            <td>${product.id}</td>
                            <td>${product.name}</td>
                            <td>${product.quantity}</td>
                            <td>${product.price}</td>
                            <td>${product.description}</td>
                            <td>${product.category}</td>
                        </tr>
                    `;
                    tableBody.innerHTML += row;
                });
            } else {
                console.error('Unexpected data format:', data);
                tableBody.innerHTML = `<tr><td colspan="6">Unexpected data format</td></tr>`;
            }
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            const tableBody = document.getElementById('products-data');
            if (tableBody) {
                tableBody.innerHTML = `<tr><td colspan="6">Error fetching data: ${error.message}</td></tr>`;
            }
        });
}


// FUNCTION TO FETCH BAR CHART DATA
function fetchBarChartData() {
    fetch('get_chart_data.php') // Replace with your API endpoint
        .then(response => response.json())
        .then(data => {
            const barChartOptions = {
                series: [
                    {
                        data: data.chartData, // dynamically fetched data
                        name: 'Products',
                    },
                ],
                chart: {
                    type: 'bar',
                    background: 'transparent',
                    height: 350,
                    toolbar: { show: false },
                },
                colors: ['#2962ff', '#d50000', '#2e7d32', '#ff6d00', '#583cb3'],
                plotOptions: {
                    bar: {
                        distributed: true,
                        borderRadius: 4,
                        horizontal: false,
                        columnWidth: '40%',
                    },
                },
                dataLabels: { enabled: false },
                fill: { opacity: 1 },
                grid: {
                    borderColor: '#55596e',
                    yaxis: { lines: { show: true } },
                    xaxis: { lines: { show: true } },
                },
                legend: {
                    labels: { colors: '#f5f7ff' },
                    show: true,
                    position: 'top',
                },
                stroke: {
                    colors: ['transparent'],
                    show: true,
                    width: 2,
                },
                tooltip: {
                    shared: true,
                    intersect: false,
                    theme: 'dark',
                },
                xaxis: {
                    categories: data.categories, // dynamically fetched categories
                    title: {
                        style: { color: '#f5f7ff' },
                    },
                    axisBorder: { show: true, color: '#55596e' },
                    axisTicks: { show: true, color: '#55596e' },
                    labels: {
                        style: { colors: '#f5f7ff' },
                    },
                },
                yaxis: {
                    title: {
                        text: 'Count',
                        style: { color: '#f5f7ff' },
                    },
                    axisBorder: { show: true, color: '#55596e' },
                    axisTicks: { show: true, color: '#55596e' },
                    labels: { style: { colors: '#f5f7ff' } },
                },
            };

            const barChartElement = document.querySelector('#bar-chart');
            if (!barChartElement) {
                console.error('Bar chart element not found');
                return;
            }

            const barChart = new ApexCharts(barChartElement, barChartOptions);
            barChart.render();
        })
        .catch(error => console.error('Error fetching bar chart data:', error));
}

// FUNCTION TO INITIALIZE AREA CHART
const areaChartOptions = {
    series: [
        { name: 'Purchase Orders', data: [31, 40, 28, 51, 42, 109, 100] },
        { name: 'Sales Orders', data: [11, 32, 45, 32, 34, 52, 41] },
    ],
    chart: {
        type: 'area',
        background: 'transparent',
        height: 350,
        stacked: false,
        toolbar: { show: false },
    },
    colors: ['#00ab57', '#d50000'],
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
    dataLabels: { enabled: false },
    fill: {
        gradient: {
            opacityFrom: 0.4,
            opacityTo: 0.1,
            shadeIntensity: 1,
            stops: [0, 100],
            type: 'vertical',
        },
        type: 'gradient',
    },
    grid: {
        borderColor: '#55596e',
        yaxis: { lines: { show: true } },
        xaxis: { lines: { show: true } },
    },
    legend: {
        labels: { colors: '#f5f7ff' },
        show: true,
        position: 'top',
    },
    markers: {
        size: 6,
        strokeColors: '#1b2635',
        strokeWidth: 3,
    },
    stroke: { curve: 'smooth' },
    xaxis: {
        axisBorder: { color: '#55596e', show: true },
        axisTicks: { color: '#55596e', show: true },
        labels: {
            offsetY: 5,
            style: { colors: '#f5f7ff' },
        },
    },
    yaxis: [
        {
            title: {
                text: 'Purchase Orders',
                style: { color: '#f5f7ff' },
            },
            labels: { style: { colors: ['#f5f7ff'] } },
        },
        {
            opposite: true,
            title: {
                text: 'Sales Orders',
                style: { color: '#f5f7ff' },
            },
            labels: { style: { colors: ['#f5f7ff'] } },
        },
    ],
    tooltip: { shared: true, intersect: false, theme: 'dark' },
};

const areaChart = new ApexCharts(document.querySelector('#area-chart'), areaChartOptions);
areaChart.render();

// FUNCTION TO INITIALIZE INVENTORY STOCK CHART
const inventoryChartOptions = {
    series: [
        {
            data: [100, 250, 500, 150, 75], // Example stock data; can be updated dynamically
            name: 'Stock Levels',
        },
    ],
    chart: {
        type: 'bar',
        background: 'transparent',
        height: 350,
        toolbar: { show: false },
    },
    colors: ['#2962ff', '#d50000', '#2e7d32', '#ff6d00', '#583cb3'],
    plotOptions: {
        bar: {
            distributed: true,
            borderRadius: 4,
            horizontal: false,
            columnWidth: '40%',
        },
    },
    dataLabels: { enabled: false },
    fill: { opacity: 1 },
    grid: {
        borderColor: '#55596e',
        yaxis: { lines: { show: true } },
        xaxis: { lines: { show: true } },
    },
    legend: {
        labels: { colors: '#f5f7ff' },
        show: true,
        position: 'top',
    },
    stroke: {
        colors: ['transparent'],
        show: true,
        width: 2,
    },
    tooltip: {
        shared: true,
        intersect: false,
        theme: 'dark',
    },
    xaxis: {
        categories: ['Category 1', 'Category 2', 'Category 3', 'Category 4', 'Category 5'], // Example categories; can be updated dynamically
        title: {
            style: { color: '#f5f7ff' },
        },
        axisBorder: { show: true, color: '#55596e' },
        axisTicks: { show: true, color: '#55596e' },
        labels: {
            style: { colors: '#f5f7ff' },
        },
    },
    yaxis: {
        title: {
            text: 'Stock Levels',
            style: { color: '#f5f7ff' },
        },
        axisBorder: { show: true, color: '#55596e' },
        axisTicks: { show: true, color: '#55596e' },
        labels: { style: { colors: '#f5f7ff' } },
    },
};

const inventoryChart = new ApexCharts(document.querySelector('#inventory-chart'), inventoryChartOptions);
inventoryChart.render();
