import javax.swing.*;
import java.awt.*;

public class BuyerPanel extends JPanel {
    private String username;
    private BuyerProductsPanel buyerProductsPanel;
    private BuyProductMenu buyProductMenu;
    private OrdersDisplayPanel ordersDisplayPanel;

    public BuyerPanel(String username) {
        this.username = username;
        initialize();
    }

    public void initialize() {
        this.setLayout(new BorderLayout());
        this.add(new JLabel("Buyer's Page"), BorderLayout.NORTH);

        // creates main panels for seller panel
        this.buyProductMenu = new BuyProductMenu(this); // instantiate product menu panel
        this.add(this.buyProductMenu, BorderLayout.EAST); // Add product menu to seller panel
        this.buyerProductsPanel = new BuyerProductsPanel(this); // instantiate products list panel
        this.add(this.buyerProductsPanel, BorderLayout.CENTER); // Add product list panel to seller panel
        this.ordersDisplayPanel = new OrdersDisplayPanel(this.username);
        this.add(this.ordersDisplayPanel, BorderLayout.SOUTH);

    }

    public String getUsername() {
        return this.username;
    }
    public BuyProductMenu getOrderProductMenu() {
        return this.buyProductMenu;
    }

    public BuyerProductsPanel getBuyerProductsPanel() {
        return this.buyerProductsPanel;
    }
    public OrdersDisplayPanel getOrdersDisplayPanel() { return this.ordersDisplayPanel; }
}
