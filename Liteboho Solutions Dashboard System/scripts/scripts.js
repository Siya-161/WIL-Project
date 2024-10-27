document.addEventListener('DOMContentLoaded', function() {
  fetchProductData(); // Fetch product data on page load
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
  // Fetch data from get_products.php
  fetch('http://localhost/Dashboard/get_products.php')
      .then(response => response.text()) // Fetch the raw response as text instead of JSON
      .then(data => {
          // Log the raw response
          console.log("Raw response from get_products.php:", data);

          // Try parsing the response as JSON
          try {
              const jsonData = JSON.parse(data); // Parse the text as JSON
              console.log("Parsed JSON data:", jsonData);

              // Check if the data structure is what we expect
              const tableBody = document.getElementById('products-data');
              if (!tableBody) {
                  console.error('Table body element not found');
                  return;
              }

              tableBody.innerHTML = ''; // Clear the table before populating

              // Populate the table if the response is successful and contains products
              if (jsonData.status === 'success' && Array.isArray(jsonData.products)) {
                  jsonData.products.forEach(product => {
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
                  console.error('Unexpected data format:', jsonData);
                  tableBody.innerHTML = `<tr><td colspan="6">Unexpected data format</td></tr>`;
              }
          } catch (error) {
              console.error('Error parsing JSON:', error);
              console.log("Raw data received:", data); // Log the raw data that caused the error
              const tableBody = document.getElementById('products-data');
              if (tableBody) {
                  tableBody.innerHTML = `<tr><td colspan="6">Error parsing data</td></tr>`;
              }
          }
      })
      .catch(error => {
          // Log any fetch or network errors
          console.error('Error fetching data:', error);
          const tableBody = document.getElementById('products-data');
          if (tableBody) {
              tableBody.innerHTML = `<tr><td colspan="6">Error fetching data: ${error.message}</td></tr>`;
          }
      });
}

// FUNCTION TO FETCH BAR CHART DATA (Bar Chart Configuration)
const barChartOptions = {
  series: [
      {
          data: [1000, 1], // Dummy data for the bar chart
          name: 'Products',
      },
  ],
  chart: {
      type: 'bar',
      background: 'transparent',
      height: 350,
      toolbar: {
          show: false,
      },
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
  dataLabels: {
      enabled: false,
  },
  fill: {
      opacity: 1,
  },
  grid: {
      borderColor: '#55596e',
      yaxis: {
          lines: {
              show: true,
          },
      },
      xaxis: {
          lines: {
              show: true,
          },
      },
  },
  legend: {
      labels: {
          colors: '#f5f7ff',
      },
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
      categories: ['Sand', 'TLB'], // Categories based on product data
      title: {
          style: {
              color: '#f5f7ff',
          },
      },
      axisBorder: {
          show: true,
          color: '#55596e',
      },
      axisTicks: {
          show: true,
          color: '#55596e',
      },
      labels: {
          style: {
              colors: '#f5f7ff',
          },
      },
  },
  yaxis: {
      title: {
          text: 'Count',
          style: {
              color: '#f5f7ff',
          },
      },
      axisBorder: {
          color: '#55596e',
          show: true,
      },
      axisTicks: {
          color: '#55596e',
          show: true,
      },
      labels: {
          style: {
              colors: '#f5f7ff',
          },
      },
  },
};

// Check if the #bar-chart element exists before rendering the chart
const barChartElement = document.querySelector('#bar-chart');
if (barChartElement) {
  const barChart = new ApexCharts(barChartElement, barChartOptions);
  barChart.render();
} else {
  console.log('#bar-chart element not found on this page');
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

// Check if the #area-chart element exists before rendering the chart
const areaChartElement = document.querySelector('#area-chart');
if (areaChartElement) {
  const areaChart = new ApexCharts(areaChartElement, areaChartOptions);
  areaChart.render();
} else {
  console.log('#area-chart element not found on this page');
}

// FUNCTION TO INITIALIZE INVENTORY STOCK CHART
const inventoryChartOptions = {
  series: [
      {
          data: [1000, 1], // Example stock data; can be updated dynamically
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
      categories: ['Construction', 'Engineering'], // Example categories; can be updated dynamically
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

// Check if the #inventory-chart element exists before rendering the chart
const inventoryChartElement = document.querySelector('#inventory-chart');
if (inventoryChartElement) {
  const inventoryChart = new ApexCharts(inventoryChartElement, inventoryChartOptions);
  inventoryChart.render();
} else {
  console.log('#inventory-chart element not found on this page');
}
