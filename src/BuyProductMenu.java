import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class BuyProductMenu extends JPanel {
    private final BuyerPanel buyerPanel;
    private JPanel orderMenuListContainer;
    private JPanel orderMenuOrderInfoContainer;
    private int totalPrice = 0;
    private final Map<Integer, OrderMenuItem> orderMenuItemHashMap = new HashMap<>();

    public BuyProductMenu(BuyerPanel buyerPanel) {
        this.buyerPanel = buyerPanel;
        initialize();
    }

    public void initialize() {

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY,1),
                BorderFactory.createEmptyBorder(10, 10, 20, 20)));

        this.orderMenuListContainer = new JPanel();
        this.orderMenuOrderInfoContainer = new JPanel();

        this.orderMenuOrderInfoContainer.setLayout(new GridLayout(4,1  ));
        this.orderMenuOrderInfoContainer.setPreferredSize(new Dimension(200, 100));

        this.orderMenuListContainer.setLayout(new BoxLayout(this.orderMenuListContainer, BoxLayout.Y_AXIS));
        this.orderMenuListContainer.setBackground(Color.decode("#DDDDDD"));

        JScrollPane scrollPane = new JScrollPane(this.orderMenuListContainer,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(orderMenuOrderInfoContainer, BorderLayout.SOUTH);
    }

    /**
     * Creates new order on server and updates the products table depending on whether the product ordered is now out of stock.
     * @param productID product id
     * @param productName product name
     * @param username username
     * @param sellerName sellers name
     * @param price price
     * @param quantity quantity
     * @param startingQuantity starting quantity
     * @param shippingDestinationAddress address
     */
    public void orderProduct(int productID, String productName, String username, String sellerName, Double price, int quantity, int startingQuantity, String shippingDestinationAddress) {
        if (shippingDestinationAddress.trim().equals("")) shippingDestinationAddress = "null";
        //
        Client.insertOnServer("INSERT INTO Orders (productName, buyerName, sellerName, price, quantity, shippingDestinationAddress)" + "VALUES (" + "'" + productName + "', '" + username + "', '" + sellerName + "'," + price + ", " + quantity + ", '" + shippingDestinationAddress + "')");

        if (startingQuantity - quantity > 0) {
            Client.updateOnServer("Products", "quantity", Integer.toString(startingQuantity - quantity), "productID", Integer.toString(productID));
        } else {
            Client.deleteOnServer("Products", "productID", Integer.toString(productID));
        }

        // Update product display
        updateDisplay();
    }

    /**
     * Add products to the buyer product menu.
     * @param product BuyerProduct object
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

    /**
     * Update the product list for buyers
     */

    public void updateDisplay() {
        this.buyerPanel.getBuyerProductsPanel().displayProducts();
        this.orderMenuListContainer.removeAll();
        this.orderMenuListContainer.revalidate();
        this.orderMenuListContainer.repaint();
    }



}
