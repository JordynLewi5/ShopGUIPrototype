import org.sqlite.jdbc3.JDBC3ResultSet;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
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



        /** Resize listener */
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                BuyProductMenu.this.orderMenuListContainer.setPreferredSize(new Dimension(200, 200));
            }
        });
    }


    public void setOrderMenu(int productID, String productName, String sellerName, Double price, int quantity) {
//        this.removeAll();
//        this.revalidate();
//        this.repaint();
//
//        JLabel title = new JLabel("REVIEW ORDER");
//        this.add(title);
//        JLabel priceLabel = new JLabel("$" + price);
//        this.add(priceLabel);
//        JLabel quantityLabel = new JLabel(quantity + " left in stock");
//        this.add(quantityLabel);
//        JLabel sellerNameLabel = new JLabel("Sold by " + sellerName);
//        this.add(sellerNameLabel);
//        this.add(new JLabel(" "));
//        this.add(new JLabel("Quantity: "));
//        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1,1, quantity,1));
//        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEnabled(false);
//        spinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
//        this.add(spinner);
//        this.add(new JLabel(" "));
//        this.add(new JLabel("Enter shipping address:"));
//        JTextField addressField = new JTextField();
//        addressField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
//        this.add(addressField);
//        double totalPrice = Double.parseDouble(new DecimalFormat("#.##").format(price * (int) spinner.getValue()));
//        JLabel totalPriceLabel = new JLabel("Total Cost: $" + totalPrice);
//        this.add(totalPriceLabel);
//        spinner.addChangeListener((event) -> totalPriceLabel.setText("Total Cost: $" + Double.parseDouble(new DecimalFormat("#.##").format(price * (int) spinner.getValue()))));
//
//        this.add(new JLabel(" "));
//
//
//        JButton orderButton = new JButton("Place order");
//        this.add(orderButton);
//
//        // Order button event listener, orders product when clicked
//        orderButton.addActionListener((event) -> {
//            orderProduct(productID, productName, buyerPanel.getUsername(), sellerName, Double.parseDouble(new DecimalFormat("#.##").format(price * (int) spinner.getValue())), (int) spinner.getValue(), addressField.getText(), quantity);
//            this.buyerPanel.getOrdersDisplayPanel().displayOrders();
//        });
    }

    // getter methods for the components

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

    public void updateDisplay() {
        /** Update the product list for buyers */
        this.buyerPanel.getBuyerProductsPanel().displayProducts();
        this.orderMenuListContainer.removeAll();
        this.orderMenuListContainer.revalidate();
        this.orderMenuListContainer.repaint();
    }



}
