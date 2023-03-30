import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.regex.Pattern;

public class SellProductMenu extends JPanel {
    private final SellerProductsPanel sellerProductsPanel;
    private final String username;
    public SellProductMenu(SellerPanel sellerPanel) {
        this.sellerProductsPanel = sellerPanel.getSellerProductsPanel();
        this.username = sellerPanel.getUsername();
        initialize();
    }

    public void initialize() {


        BoxLayout addProductsLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(addProductsLayout);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY,2),
                BorderFactory.createEmptyBorder(30, 10, 30, 10))); // top, left, bottom, right


        this.add(new JLabel("Product name:"));

        JTextField productNameTF = new JTextField();
        this.add(productNameTF);

        this.add(new JLabel("Price:"));
        JTextField priceTF = new JTextField();
        this.add(priceTF);

        this.add(new JLabel("Quantity:"));
        JTextField quantityTF = new JTextField();
        this.add(quantityTF);


        JButton addButton = new JButton("Add product");
        this.add(addButton);

        addButton.addActionListener((event) -> {
            try {
                if (Integer.parseInt(quantityTF.getText()) <= 0) return;
            } catch (NumberFormatException e) { return; }
            if (!productNameTF.getText().matches(".*\\w.*")) return;
            if (priceTF.getText().startsWith("$")) priceTF.setText(priceTF.getText().substring(1));
            if (Pattern.matches("([0-9]*)\\\\.([0-9]*)", priceTF.getText())) return;

            try {
                addProduct(productNameTF.getText(), this.username, Double.parseDouble(priceTF.getText()), Integer.parseInt(quantityTF.getText()));
            } catch (SQLException err) {
                System.err.println(err);
            }
            //reset text fields
            productNameTF.setText("");
            priceTF.setText("");
            quantityTF.setText("");
        });

    }

    public void addProduct(String productName, String sellerName, Double price, int quantity) throws SQLException {
        Client.insertOnServer("Products", "productName, sellerName, price, quantity", productName + ", " + sellerName + ", " + price + ", " + quantity);
        updateDisplay();
    }

    public void updateDisplay() throws SQLException {
        sellerProductsPanel.displayProducts();
    }
}
