import javax.swing.*;
import java.awt.*;

public class SellerProduct extends JPanel {
    private final String productName;
    private final double price;
    private final String sellerName;
    private final int quantity;
    public SellerProduct(String productName, String sellerName, double price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.sellerName = sellerName;
        this.quantity = quantity;
        initialize();
    }

    public void initialize() {
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 10, 20, 10))); // top, left, bottom, right
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(new JLabel(this.productName));
        this.add(new JLabel("$" + this.price));
        this.add(new JLabel("Seller: " + this.sellerName));
        this.add(new JLabel("Stock: " + this.quantity));
    }

}
