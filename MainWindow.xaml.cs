using System.Windows;

namespace Inventory_System_WPF
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void Login_Click(object sender, RoutedEventArgs e)
        {
            // Replace the ContentGrid with the Login page content
            ContentGrid.Children.Clear();
            //ContentGrid.Children.Add(new Login());
        }
    }
}
