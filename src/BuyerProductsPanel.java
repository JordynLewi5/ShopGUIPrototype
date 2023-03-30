import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BuyerProductsPanel extends JPanel {
    private JPanel productsPanel;
    private final BuyerPanel buyerPanel;

    public BuyerProductsPanel(BuyerPanel buyerPanel) {

        this.buyerPanel = buyerPanel;

        initialize();
    }

    public void initialize() {

        // Initialize panels
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY,2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        this.productsPanel = new JPanel();
        this.productsPanel.setLayout(new GridLayout(0,5,2,2));

        this.add(new JLabel("Current Products"), BorderLayout.NORTH);
        this.add(new JScrollPane(productsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        // Update product display
        displayProducts();
    }

    /**
     * Retrieves products from server database and updates product display.
     */
    public void displayProducts() {

        // Reset components
        this.productsPanel.removeAll();
        this.productsPanel.revalidate();
        this.productsPanel.repaint();

        // Retrieve data from server
        ArrayList<String[]> productsTable = Client.selectOnServer("Products");

        // Further parse out the data and add products to the display
        for (String[] product : productsTable) {
            BuyerProduct productPanel = new BuyerProduct(
                    this.buyerPanel.getOrderProductMenu(),
                    Integer.parseInt(product[0]),
                    product[1],
                    product[2],
                    Double.parseDouble(product[3]),
                    Integer.parseInt(product[4]));

            this.productsPanel.add(productPanel);
        }
    }
}

