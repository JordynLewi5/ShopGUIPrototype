import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class BuyProductMenu extends JPanel {
    private final BuyerPanel buyerPanel;
    private JPanel orderMenuListContainer;
    private JPanel orderMenuOrderInfoContainer;
    private int totalPrice = 0;
    private Map<Integer, OrderMenuItem> orderMenuItemHashMap = new HashMap<>();


    public BuyProductMenu(BuyerPanel buyerPanel) {
        this.buyerPanel = buyerPanel;
        initialize();
    }

    public void initialize() {

        this.setLayout(new BorderLayout());

        this.orderMenuListContainer = new JPanel();
        this.orderMenuOrderInfoContainer = new JPanel();

        this.orderMenuOrderInfoContainer.setLayout(new GridLayout(4,1  ));
        JScrollPane scrollPane = new JScrollPane(this.orderMenuListContainer,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(250, 200));
        this.orderMenuListContainer.setPreferredSize(new Dimension(200, 100));
        this.orderMenuListContainer.setLayout(new BoxLayout(this.orderMenuListContainer, BoxLayout.Y_AXIS));
        this.orderMenuListContainer.setBackground(Color.decode("#DDDDDD"));

        this.orderMenuOrderInfoContainer.setPreferredSize(new Dimension(200, 200));

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(orderMenuOrderInfoContainer, BorderLayout.SOUTH);

        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY,1),
                BorderFactory.createEmptyBorder(10, 10, 20, 20))); // top, left, bottom, right



        // Resize listener
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                BuyProductMenu.this.orderMenuListContainer.setPreferredSize(new Dimension(200, 200));
            }
        });
    }

    /**
     * Orders provided product
     * @param productID product id
     * @param productName product name
     * @param username username
     * @param sellerName sellers name
     * @param price price
     * @param quantity quantity
     * @param startingQuantity starting quantity
     * @param shippingDestinationAddress address
     */
    public void orderProduct(int productID, String productName, String username, String sellerName, Double price, int quantity, int startingQuantity, String shippingDestinationAddress) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/jlewi/IdeaProjects/Project1/shop.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO Orders (productName, buyerName, sellerName, price, quantity, shippingDestinationAddress)" + "VALUES (" + "'" + productName + "', '" + username + "', '" + sellerName + "'," + price + ", " + quantity + ", '" + shippingDestinationAddress + "')");

            if (startingQuantity - quantity > 0) {
                statement.executeUpdate("UPDATE Products SET quantity = " + (startingQuantity - quantity) + " WHERE productID = " + productID);
            } else {
                statement.executeUpdate("Delete FROM Products WHERE productID = " + productID);
            }
            updateDisplay();
        } catch (SQLException error) {
            System.err.println(error.getMessage());
            this.add(new JLabel("Invalid parameters, please try again."));
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException error) {
                System.err.println(error.getMessage());
            }
        }
    }

    /**
     * Add products to the buyer product menu.
     * @param product BuyerProduct object
     */
    public void addToBuyProductMenu(BuyerProduct product) {
        OrderMenuItem orderMenuItem = new OrderMenuItem(product, this);
        this.orderMenuItemHashMap.put(product.getProductID(), orderMenuItem);
        renderBuyProductMenuList();
    }

    /**
     * Renders the list of OrderMenuItems onto the BuyProductMenu
     */
    public void renderBuyProductMenuList() {
        this.orderMenuOrderInfoContainer.removeAll();
        this.orderMenuOrderInfoContainer.revalidate();
        this.orderMenuOrderInfoContainer.repaint();
        this.orderMenuListContainer.removeAll();
        this.orderMenuListContainer.revalidate();
        this.orderMenuListContainer.repaint();

        this.totalPrice = 0;

        this.orderMenuItemHashMap.forEach((key, orderMenuItem) -> {
            this.orderMenuListContainer.add(orderMenuItem);
            JButton removeItemButton = new JButton("X");
            removeItemButton.setForeground(new Color(255, 50, 50));
            this.orderMenuListContainer.add(removeItemButton);
            this.orderMenuListContainer.add(Box.createVerticalStrut(10));

            removeItemButton.addActionListener((event)->{
                this.orderMenuItemHashMap.remove(key);
                renderBuyProductMenuList();
            });
            this.totalPrice += orderMenuItem.getTotalPriceOfOrderItem();
        });

        JButton orderButton = new JButton("Order");

        JTextField addressField = new JTextField();
        addressField.setMaximumSize(new Dimension(150, 20));
        double totalPrice = Double.parseDouble(new DecimalFormat("#.##").format(this.totalPrice));
        JLabel totalPriceLabel = new JLabel("Total Cost: $" + totalPrice);

        this.orderMenuOrderInfoContainer.add(new JLabel("Enter shipping address:"));
        this.orderMenuOrderInfoContainer.add(addressField);
        this.orderMenuOrderInfoContainer.add(totalPriceLabel);
        this.orderMenuOrderInfoContainer.add(orderButton);
        orderButton.addActionListener((event) -> {
            this.orderMenuItemHashMap.forEach((key, orderMenuItem) -> {
                orderProduct(
                        orderMenuItem.getProductID(),
                        orderMenuItem.getProductName(),
                        this.buyerPanel.getUsername(),
                        orderMenuItem.getSellerName(),
                        orderMenuItem.getPrice(),
                        orderMenuItem.getQuantityInOrder(),
                        orderMenuItem.getQuantityInStock(),
                        addressField.getText());

                this.buyerPanel.getOrdersDisplayPanel().displayOrders();
            });
            this.orderMenuItemHashMap.clear();
            this.renderBuyProductMenuList();
        });
    }

    /**
     * Update the product list for buyers
     */

    public void updateDisplay() {
        this.buyerPanel.getBuyerProductsPanel().displayProducts();
        this.orderMenuListContainer.removeAll();
        this.orderMenuListContainer.revalidate();
        this.orderMenuListContainer.repaint();
    }



}
