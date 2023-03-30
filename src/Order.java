import javax.swing.*;
import java.awt.*;

public class Order extends JPanel {
    private final int productID;
    private final String productName;
    private final double price;
    private final String sellerName;
    private final String buyerName;
    private final int quantity;
    public Order(int productID, String productName, String buyerName, String sellerName, double price, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.buyerName = buyerName;
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
        this.add(new JLabel("Product: " + this.productName));
        this.add(new JLabel("Product ID: " + this.productID));
        this.add(new JLabel("Buyer: " + this.buyerName));
        this.add(new JLabel("Seller: " + this.sellerName));
        this.add(new JLabel("$" + this.price));
        this.add(new JLabel("Stock: " + this.quantity));
    }
}
