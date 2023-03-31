import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;


public class SellerPanel extends JPanel {
    private SellerProductsPanel sellerProductsPanel;
    private SellProductMenu sellProductMenu;
    private String username;

    public SellerPanel(String username) {
        this.username = username;
        initialize();
    }

    public void initialize() {
        this.setLayout(new BorderLayout());

        // header contains title and logout button
        JPanel headerPanel = new JPanel();
        this.add(headerPanel, BorderLayout.NORTH);
        headerPanel.add(new JLabel(this.username + "'s Seller Page"), BorderLayout.WEST);
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
        this.sellerProductsPanel = new SellerProductsPanel(this); // instantiate products list panel
        this.sellProductMenu = new SellProductMenu(this); // instantiate product menu panel
        this.add(this.sellerProductsPanel, BorderLayout.CENTER); // Add product list panel to seller panel
        this.add(this.sellProductMenu, BorderLayout.EAST); // Add product menu to seller panel
    }
    public String getUsername() {
        return this.username;
    }

    public SellerProductsPanel getSellerProductsPanel() {
        return sellerProductsPanel;
    }
}
