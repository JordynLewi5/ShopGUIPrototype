import javax.swing.*;
import java.awt.*;

public class BuyerProduct extends JPanel {
    private final int productID;
    private final String productName;
    private final double price;
    private final String sellerName;
    private int quantity;
    private final BuyProductMenu menu;
    public BuyerProduct(BuyProductMenu menu, int productID, String productName, String sellerName, double price, int quantity) {
        this.menu = menu;
        this.productID = productID;
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

        JButton orderButton = new JButton("Order");
        this.add(orderButton);

        orderButton.addActionListener((event) -> this.menu.addToBuyProductMenu(this));
    }


    /**
     * Get productID
     */
    public int getProductID() {
        return this.productID;
    }

    /**
     * Get productName
     */
    public String getProductName() {
        return this.productName;
    }

    /**
     * Get sellerName
     */
    public String getSellerName() {
        return this.sellerName;
    }

    /**
     * Get price
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Get quantity
     * @return quantity
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Sets quantity
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
