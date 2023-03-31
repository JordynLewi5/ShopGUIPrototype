import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

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

        // header contains title and logout button
        JPanel headerPanel = new JPanel();
        this.add(headerPanel, BorderLayout.NORTH);
        headerPanel.add(new JLabel(this.username + "'s Buyer Page"), BorderLayout.WEST);
        JButton backToLogin = new JButton("Logout");
        headerPanel.add(Box.createHorizontalStrut(100), BorderLayout.CENTER);
        headerPanel.add(backToLogin, BorderLayout.EAST);

        backToLogin.addActionListener((event) -> {
            //close window, call for garbage collection and then initialize a new window.
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispatchEvent(new WindowEvent(topFrame, WindowEvent.WINDOW_CLOSING));;
            System.gc();
            new MainFrame("Shop App");
        });

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
