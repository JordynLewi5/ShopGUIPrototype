import javax.swing.*;
import java.awt.*;


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
        this.add(new JLabel("Seller's Page"), BorderLayout.NORTH);

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
