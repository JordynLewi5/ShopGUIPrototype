import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class SellerProductsPanel extends JPanel {
    private final String username;
    private JPanel productsPanel;


    public SellerProductsPanel(SellerPanel sellerPanel) {
        this.username = sellerPanel.getUsername();
        initialize();
    }

    public void initialize() {
        // initialize panels
        this.productsPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(productsPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // configure panels
        productsPanel.setLayout(new GridLayout(0, 5, 2, 2));
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        // add panels
        this.add(new JLabel("Current Products"), BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        OrdersDisplayPanel ordersDisplayPanel = new OrdersDisplayPanel(this.username);
        this.add(ordersDisplayPanel, BorderLayout.SOUTH);

        // display products
        displayProducts();


    }

    /**
     * Retrieves products from server database and updates product display.
     */
    public void displayProducts() {
        // Reset components

        this.productsPanel.removeAll();
        this.productsPanel.revalidate();
        this.productsPanel.repaint();

        ArrayList<String[]> productsTable = Client.selectOnServer("Products");
        for (String[] product : productsTable) {
            SellerProduct productPanel = new SellerProduct(
                    product[1],
                    product[2],
                    Double.parseDouble(product[3]),
                    Integer.parseInt(product[4]));

            this.productsPanel.add(productPanel);

            if (product[2].equals(this.username)) {
                JButton removeItemButton = new JButton("X");
                removeItemButton.setForeground(new Color(255, 50, 50));
                productPanel.add(removeItemButton);

                removeItemButton.addActionListener((event) -> {
                    Client.deleteOnServer("Products", "productID", product[0]);
                    this.productsPanel.remove(productPanel);
                    displayProducts();
                });
            }
        }
    }
}

