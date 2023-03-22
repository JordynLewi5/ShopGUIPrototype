import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.regex.Pattern;

public class SellProductMenu extends JPanel {
    private BoxLayout addProductsLayout;

    private SellerProductsPanel sellerProductsPanel;
    private String username;
    public SellProductMenu(SellerPanel sellerPanel) {
        this.sellerProductsPanel = sellerPanel.getSellerProductsPanel();
        this.username = sellerPanel.getUsername();

        initialize();
    }

    public void initialize() {


        this.addProductsLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
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
            if (priceTF.getText().startsWith("$")) priceTF.setText(priceTF.getText().substring(1,priceTF.getText().length()));
            if (Pattern.matches("([0-9]*)\\\\.([0-9]*)", priceTF.getText())) return;


            addProduct(productNameTF.getText(), this.username, Double.parseDouble(priceTF.getText()), Integer.parseInt(quantityTF.getText()));

            //reset text fields
            productNameTF.setText("");
            priceTF.setText("");
            quantityTF.setText("");
        });

    }

    public void addProduct(String productName, String sellerName, Double price, int quantity) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/jlewi/IdeaProjects/Project1/shop.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO Products (productName, sellerName, price, quantity)" + "VALUES (" + "'" + productName + "'" + ", " + "'" + sellerName + "'" + ", " + price + ", " + quantity + ")");
            updateDisplay();
//            ResultSet products = statement.executeQuery("SELECT * FROM Products");
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException error) {
                System.err.println(error.getMessage());
            }
        }
    }

    public void updateDisplay() {
        sellerProductsPanel.displayProducts();
    }
}
