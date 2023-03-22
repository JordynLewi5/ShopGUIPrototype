import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {
    public MainFrame(String label) {
        super(label);
        initialize();
    }

    public void initialize() {
        createLoginPanel();
        this.setTitle("Shop App");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(400, 200));
    }

    public void createLoginPanel() {
        LoginPanel loginPanel = new LoginPanel();
        JPanel smallPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(400, 100));
        smallPanel.add(loginPanel, BorderLayout.CENTER);
        this.add(smallPanel, BorderLayout.CENTER);

    }
}
