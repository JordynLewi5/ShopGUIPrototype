import org.sqlite.SQLiteException;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

// Displays the same information regardless of whether the user is in the buyer or seller page.
// Displays most relevant orders, i.e. buyer will see orders they have made first (or exclusively), sellers will see
// orders that they are selling.
public class OrdersDisplayPanel extends JPanel {
    private String username;
    private JPanel ordersPanel;
    private boolean otherOrders = true;
    public OrdersDisplayPanel(String username) {
        this.username = username;
        initialize();
    }

    public void initialize() {
        this.ordersPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(ordersPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // configure panels
        this.ordersPanel.setLayout(new GridLayout(0,5,2,2));
        this.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY,2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        // add panels
        this.add(new JLabel("Orders"), BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        JCheckBox displayOtherOrdersCheckBox = new JCheckBox("Display unrelated orders");
        displayOtherOrdersCheckBox.setSelected(otherOrders);
        this.add(displayOtherOrdersCheckBox, BorderLayout.SOUTH);
        displayOtherOrdersCheckBox.addActionListener((event) -> {
            otherOrders = displayOtherOrdersCheckBox.isSelected();
            displayOrders();
        });
        displayOrders();
    }

    public void displayOrders() {
        // Reset components
        this.ordersPanel.removeAll();
        this.ordersPanel.revalidate();
        this.ordersPanel.repaint();


        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/jlewi/IdeaProjects/Project1/shop.db");
            Statement statement = connection.createStatement();
            ResultSet orders = statement.executeQuery("SELECT * FROM Orders");
            while (orders.next()) {
                if (this.username.equals(orders.getString("buyerName")) || this.username.equals(orders.getString("sellerName")) || this.otherOrders) {
                    Order order = new Order(
                            orders.getInt("productID"),
                            orders.getString("productName"),
                            orders.getString("buyerName"),
                            orders.getString("sellerName"),
                            orders.getDouble("price"),
                            orders.getInt("quantity"));
                    this.ordersPanel.add(order);
                }
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
