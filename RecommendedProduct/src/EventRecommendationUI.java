import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List; // important: use java.util.List

public class EventRecommendationUI {
    // Shared data
    static Map<String, Integer> eventIndex = new HashMap<>();
    static ArrayList<ArrayList<product>> productsByEvent = new ArrayList<>();

    public static void main(String[] args) {
        // === Load file at the beginning ===
        String filePath = "database.txt"; // <-- Update path if needed
        Main.fileReader(filePath,
                new ArrayList<>(), new HashMap<>(),
                new ArrayList<>(), new HashMap<>(),
                productsByEvent, eventIndex);
        System.out.println("File loaded: " + filePath);

        // === Set up JFrame ===
        JFrame frame = new JFrame("Event Recommendations");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // Title label
        JLabel titleLabel = new JLabel("Event Recommendations", SwingConstants.CENTER);can y
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 50, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        contentPanel.setBackground(Color.WHITE);

        // === LEFT: Dropdown panel ===
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        JLabel dropdownLabel = new JLabel("Event Listing", SwingConstants.CENTER);
        dropdownLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        dropdownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Build events dynamically
        List<String> eventNames = new ArrayList<>(eventIndex.keySet());
        Collections.sort(eventNames);
        JComboBox<String> eventDropdown = new JComboBox<>(eventNames.toArray(new String[0]));
        eventDropdown.setFont(new Font("SansSerif", Font.PLAIN, 12));
        eventDropdown.setMaximumRowCount(5);
        eventDropdown.setPreferredSize(new Dimension(150, 25));
        eventDropdown.setMaximumSize(new Dimension(150, 25));
        eventDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(dropdownLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(eventDropdown);

        // === RIGHT: Product List panel ===
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(Color.WHITE);

        JLabel productLabel = new JLabel("Products", SwingConstants.CENTER);
        productLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> productList = new JList<>(listModel);
        productList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        productList.setFixedCellHeight(25);
        JScrollPane productScroll = new JScrollPane(productList);

        rightPanel.add(productLabel, BorderLayout.NORTH);
        rightPanel.add(productScroll, BorderLayout.CENTER);

        // === Update product list when dropdown changes ===
        eventDropdown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String event = (String) eventDropdown.getSelectedItem();
                listModel.clear();

                if (eventIndex.containsKey(event)) {
                    List<product> topProducts = RecommendedByEvent.getTop10ByEvent(event, productsByEvent, eventIndex);
                    for (int i = 0; i < topProducts.size(); i++) {
                        product p = topProducts.get(i);
                        listModel.addElement((i + 1) + ". " + p.getName() + "    $" + p.getPrice());
                    }
                } else {
                    listModel.addElement("No products found for this event.");
                }
            }
        });

        if (eventDropdown.getItemCount() > 0) {
            eventDropdown.setSelectedIndex(0); // Auto-load first event if available
        }

        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);
        frame.add(contentPanel, BorderLayout.CENTER);

        // === TOP-RIGHT: Buttons ===
        JPanel topRightPanel = new JPanel();
        topRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        topRightPanel.setBackground(Color.WHITE);

        JButton refreshBtn = new JButton("🔄 Refresh");
        JButton homeBtn = new JButton("🏠 Home");
        refreshBtn.setFocusPainted(false);
        homeBtn.setFocusPainted(false);

        topRightPanel.add(refreshBtn);
        topRightPanel.add(homeBtn);
        frame.add(topRightPanel, BorderLayout.PAGE_START);

        // === Button Actions ===

        // Refresh button working
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eventIndex.clear();
                productsByEvent.clear();
                Main.fileReader(filePath,
                        new ArrayList<>(), new HashMap<>(),
                        new ArrayList<>(), new HashMap<>(),
                        productsByEvent, eventIndex);

                // Update the dropdown with refreshed events
                List<String> updatedEvents = new ArrayList<>(eventIndex.keySet());
                Collections.sort(updatedEvents);
                eventDropdown.setModel(new DefaultComboBoxModel<>(updatedEvents.toArray(new String[0])));

                // Clear product list
                listModel.clear();

                JOptionPane.showMessageDialog(frame, "✅ Refreshed Successfully!");
            }
        });

        // Home button (empty for now)
        homeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Home button clicked (feature coming soon)!");
                // TODO: In future: Switch to MainPage screen
            }
        });

        frame.setVisible(true);
    }
}
