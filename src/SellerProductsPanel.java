import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SellerProductsPanel extends JPanel {
    private final String username;
    private JPanel productsPanel;
    private JScrollPane scrollPane;
    private OrdersDisplayPanel ordersDisplayPanel;

    public SellerProductsPanel(SellerPanel sellerPanel) {
        this.username = sellerPanel.getUsername();
        initialize();
    }

    public void initialize() {
        // initialize panels
        this.productsPanel = new JPanel();
        this.scrollPane = new JScrollPane(productsPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // configure panels
        this.productsPanel.setLayout(new GridLayout(0,5,2,2));
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY,2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        // add panels
        this.add(new JLabel("Current Products"), BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.ordersDisplayPanel = new OrdersDisplayPanel(this.username);
        this.add(this.ordersDisplayPanel, BorderLayout.SOUTH);

        // display products
        displayProducts();
    }


    public void displayProducts() {
        // Reset components
        this.productsPanel.removeAll();
        this.productsPanel.revalidate();
        this.productsPanel.repaint();

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/jlewi/IdeaProjects/Project1/shop.db");
            Statement statement = connection.createStatement();
            ResultSet products = statement.executeQuery("SELECT * FROM Products");
            while (products.next()) {
                SellerProduct sellerProduct = new SellerProduct(
                        products.getString("productName"),
                        products.getDouble("price"),
                        products.getString("sellerName"),
                        products.getInt("quantity"));
                this.productsPanel.add(sellerProduct);
            }
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException error) {
                System.err.println(error.getMessage());
            }
        }
    }

}

