import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
* The login screen that users see when the program is first executed
*/
public class LoginPanel extends JPanel {
    private GroupLayout layout;
    private JLabel usernamePrompt;
    private JTextField usernameTextField;
    private JButton loginButton;
    private JCheckBox sellerCheckBox;
    public LoginPanel() {
        initialize();
    }

    public void initialize() {
        layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        this.setLayout(layout);
        
        promptUserLogin();
    }
    
    /**
    * Create prompts for user login
    */
    public void promptUserLogin() {
        usernamePrompt = new JLabel("Username");
        usernameTextField = new JTextField();
        usernameTextField.setMaximumSize(new Dimension(400, 20));
        loginButton = new JButton("Login");
        sellerCheckBox = new JCheckBox("Seller?", false);

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
            login();
        });
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
    }

    /**
    * Login sequence, navigate to correct panel
    */
    public void login() {
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
