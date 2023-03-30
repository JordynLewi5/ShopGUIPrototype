import javax.swing.*;
import java.awt.*;


public class LoginPanel extends JPanel {
    public LoginPanel() {
        initialize();
    }

    public void initialize() {
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        this.setLayout(layout);


        promptUserLogin(layout);
    }

    public void promptUserLogin(GroupLayout layout) {
        JLabel usernamePrompt = new JLabel("Username");
        JTextField usernameTextField = new JTextField();
        usernameTextField.setMaximumSize(new Dimension(400, 20));
        JButton loginButton = new JButton("Login");
        JCheckBox sellerCheckBox = new JCheckBox("Seller?", false);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(usernamePrompt)
                                .addComponent(usernameTextField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(loginButton)
                                .addComponent(sellerCheckBox))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(usernamePrompt)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(usernameTextField)
                                .addComponent(loginButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(sellerCheckBox))
        );

        // Login button listener
        loginButton.addActionListener((event) -> {
            if (!usernameTextField.getText().matches(".*\\w.*")) {
                usernamePrompt.setText("Username -> Please enter a valid username.");
                return;
            }
            if (usernameTextField.getText().length() > 20) {
                usernamePrompt.setText("Username -> Please enter a valid username, 20 characters or less.");
                return;
            }
            login(sellerCheckBox, usernameTextField);
        });
    }

    public void login(JCheckBox sellerCheckBox, JTextField usernameTextField) {

        Container parentFrame = this.getParent().getParent();
        parentFrame.remove(this.getParent());
        parentFrame.revalidate();
        parentFrame.repaint();
        if (sellerCheckBox.isSelected()) {
            SellerPanel sellerPanel = new SellerPanel(usernameTextField.getText());
            parentFrame.add(sellerPanel);
        } else {
            BuyerPanel buyerPanel = new BuyerPanel(usernameTextField.getText());
            parentFrame.add(buyerPanel);
        }
    }
}
