import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// Displays the same information regardless of whether the user is in the buyer or seller page.
// Displays most relevant orders, i.e. buyer will see orders they have made first (or exclusively), sellers will see
// orders that they are selling.
public class OrdersDisplayPanel extends JPanel {
    private final String username;
    private JPanel ordersPanel;
    private boolean otherOrders = true;
    public OrdersDisplayPanel(String username) {
        this.username = username;
        initialize();
    }

    public void initialize() {
        this.ordersPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(ordersPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // configure panels
        this.ordersPanel.setLayout(new GridLayout(0,5,2,2));
        this.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY,2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        // add panels
        this.add(new JLabel("Orders"), BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        JCheckBox displayOtherOrdersCheckBox = new JCheckBox("Display unrelated orders");
        displayOtherOrdersCheckBox.setSelected(otherOrders);
        this.add(displayOtherOrdersCheckBox, BorderLayout.SOUTH);
        displayOtherOrdersCheckBox.addActionListener((event) -> {
            otherOrders = displayOtherOrdersCheckBox.isSelected();
            displayOrders();
        });
        displayOrders();
    }

    /**
     * Retrieves orders from server database and updates order display.
     */
    public void displayOrders() {
        // Reset components
        this.ordersPanel.removeAll();
        this.ordersPanel.revalidate();
        this.ordersPanel.repaint();

        ArrayList<String[]> ordersTable = Client.selectOnServer("Orders");
        for (String[] order : ordersTable) {
                if (this.username.equals(order[2]) || this.username.equals(order[3]) || this.otherOrders) {
                    Order orderPanel = new Order(
                            Integer.parseInt(order[0]),
                            order[1],
                            order[2],
                            order[3],
                            Double.parseDouble(order[4]),
                            Integer.parseInt(order[5]));
                    this.ordersPanel.add(orderPanel);
                }
            }
        }
    }

