import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class OrderMenuItem extends JPanel {
    private final int productID;
    private final String productName;
    private final double price;
    private final BuyerProduct product;
    private final String sellerName;
    private final BuyProductMenu buyProductMenu;
    private int quantityInStock;
    private int quantityInOrder;
    private double totalPriceOfOrderItem;

    public OrderMenuItem(BuyerProduct product, BuyProductMenu buyProductMenu) {
        this.product = product;
        this.productID = product.getProductID();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.sellerName = product.getSellerName();
        this.quantityInStock = product.getQuantity();
        this.quantityInOrder = 1;
        this.buyProductMenu = buyProductMenu;

        initialize();
    }

    public void initialize() {
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 10, 20, 10))); // top, left, bottom, right
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        /** Product Label */
        JLabel productNameLabel = new JLabel(this.productName);
        this.add(productNameLabel);

        /** Price */
        JLabel priceLabel = new JLabel("$" + price);
        this.add(priceLabel);

        /** Left in stock */
        JLabel quantityLabel = new JLabel(this.quantityInStock + " left in stock");
        this.add(quantityLabel);

        /** Seller name */
        JLabel sellerNameLabel = new JLabel("Sold by " + this.sellerName);
        this.add(sellerNameLabel);

        /** Quantity spinner */
        this.add(new JLabel("Quantity: "));
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1,1, this.quantityInStock,1));
        spinner.setForeground(new Color(0, 0, 0));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEnabled(false);
        spinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        this.add(spinner);
        this.totalPriceOfOrderItem = Double.parseDouble(new DecimalFormat("#.##").format(price * (int) spinner.getValue()));
        spinner.addChangeListener((event) -> {
            this.quantityInOrder = (int) spinner.getValue();
            this.totalPriceOfOrderItem = Double.parseDouble(new DecimalFormat("#.##")
                .format(price * (int) spinner.getValue())
            );
            this.buyProductMenu.renderBuyProductMenuList();
        });

    }


    /**
     * Sets the quantity in the order
     * @param quantity
     */
    public void setQuantityInOrder(int quantity) {
        this.quantityInOrder = quantity;
    }

    public int getQuantityInOrder() {
        return this.quantityInOrder;
    }

    /**
     * Sets the quantity in stock (probably won't be used)
     * @param quantity
     */
    public void setQuantityInStock(int quantity) {
        this.product.setQuantity(quantity);
    }

    public int getQuantityInStock() {
        return this.quantityInStock;
    }

    /**
     * Gets total price of item order for the
     * BuyProductMenuList to add all items together for final price.
     * @return totalPriceOfOrderItem
     */
    public double getTotalPriceOfOrderItem() {
        return this.totalPriceOfOrderItem;
    }

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






}
