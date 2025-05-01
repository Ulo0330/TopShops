package topshopspackage;

import javax.swing.*;

//UI logic
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;


import static topshopspackage.RecommendedByEvent.getTop10ByEvent;
import static topshopspackage.filereader.getTop10ProductsBySales;
import static topshopspackage.productTrend.calculateRegression;

public class SwingUI {
    //Create GUI a show it.
    public static void CreateAndShowGUI(ArrayList<ArrayList<product>> productsByType, Map<String, Integer> categoryIndex,
                                        ArrayList<ArrayList<product>> productsByCompany, Map<String, Integer> companyIndex, ArrayList<ArrayList<product>> productsByEvent
            , Map<String, Integer> eventIndex) {

        // Create and set up the window
        JFrame TopShops = new JFrame("TopShops");
        TopShops.setExtendedState(JFrame.MAXIMIZED_BOTH);
        TopShops.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // LOGO
        ImageIcon logo = new ImageIcon("topshopspackage/logo.png");
        TopShops.setIconImage(logo.getImage());

        // Creating Panel that will switch out with other panels
        CardLayout topShopsLayout = new CardLayout();
        JPanel MainPage = new JPanel(topShopsLayout);

        //Initializing all panel types
        MainPage.add(homePage(productsByType, categoryIndex, productsByCompany, companyIndex, productsByEvent, eventIndex,topShopsLayout, MainPage), "Home");

        MainPage.add(TopTenProductsUI.open(productsByType, categoryIndex, topShopsLayout, MainPage), "Top Ten Products");

        MainPage.add(EventRecommendationUI.open(productsByEvent, eventIndex, topShopsLayout, MainPage), "Event Recommendations");
        TopShops.add(MainPage);

        //Display Window
        TopShops.pack();
        TopShops.setVisible(true);
    }

    /* Author: Clayton Frandeen
     * Last Modified: April 30th, 2025
     * File Description: Creates and returns a home page panel that will have labels and buttons to redirect
     * to Top Ten Products, Top Ten Companies, and Event Recommendations
     */
    public static JPanel homePage(ArrayList<ArrayList<product>> productsByType, Map<String, Integer> categoryIndex,
                                  ArrayList<ArrayList<product>> productsByCompany, Map<String, Integer> companyIndex, ArrayList<ArrayList<product>> productsByEvent
            , Map<String, Integer> eventIndex, CardLayout topShopsLayout, JPanel TopShopsPanel) {

        JPanel homePage = new JPanel();
        // add label to view top ten products

        homePage.setLayout(new GridLayout(2, 3));
        homePage.setSize(100000, 100000);
        JLabel TopTenLabel = new JLabel("View Top Ten Products");
        JLabel TopTenCompanyLabel = new JLabel("View Top Ten Company");
        JLabel EventLabel = new JLabel("View Event Recommendations");
        homePage.add(TopTenCompanyLabel);
        homePage.add(TopTenLabel);
        homePage.add(EventLabel);


        JButton TopTenButton = new JButton("View Top Ten Products");
        TopTenButton.addActionListener(e -> {
            topShopsLayout.show(TopShopsPanel, "Top Ten Products");
        });


        homePage.add(TopTenButton);

        JButton TopTenCompanyButton = new JButton("View Top Ten Companies");
        TopTenCompanyButton.addActionListener(e -> {

        });
        homePage.add(TopTenCompanyButton);

        JButton EventButton = new JButton("View Products Recommendations");
        EventButton.addActionListener(e -> {
            topShopsLayout.show(TopShopsPanel, "Event Recommendations"); // open new Event UI
        });
        homePage.add(EventButton);

        return homePage;
    }

    /* Author: Clayton Frandeen
     * Last Modified: April, 24, 2025
     * File description: This is the UI or view component of the Product Trends.
     *  It creates the window for product trends, calls out to get the information
     * for the product sales and regression line. It then outputs the product information
     * and the graph filled with plot points and regression line.
     */
    public class productTrendUI {
        public static void graph(ArrayList<product> current) {
            if (current.size() == 1) {
                int[] sales = productTrend.toNum(current.get(0).getSalesBy7());
                productTrend.regressionResult r = calculateRegression(current.get(0));

                // Creating the frame set up for our product trend window
                JFrame graphFrame = new JFrame("Product Trends");
                graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Just closes window, not whole program
                graphFrame.setSize(700, 600);
                graphFrame.setLayout(new BorderLayout());
                graphFrame.setResizable(false);                               //Non-resizable, So information doesn't get distorted

                // Top panel for the product information
                JPanel productInfo = new JPanel();
                productInfo.setLayout(new BoxLayout(productInfo, BoxLayout.Y_AXIS));
                productInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

                // Labels
                productInfo.add(createLabel("Product: " + current.get(0).getName()));
                productInfo.add(createLabel("Price: $" + current.get(0).getPrice()));
                productInfo.add(createLabel("Total Sales: " + current.get(0).getTotalSales()));
                productInfo.add(createLabel("Company: " + current.get(0).getCompany()));
                productInfo.add(createLabel("Line of Best Fit: y = " + r.beta1 + "x + " + r.beta0));

                float growthRate = productTrend.calculateGrowthRate(r.beta0, r.beta1);
                productInfo.add(createLabel("Growth Rate: " + growthRate));

                // Add recommendation
                String recommendation;
                if (growthRate < 0.0) {
                    recommendation = "Avoid this at all costs.";
                } else if (growthRate < 10.0) {
                    recommendation = "This is a risky investment!";
                } else if (growthRate < 50.0) {
                    recommendation = "This is a good investment!";
                } else {
                    recommendation = "This is a great investment!";
                }
                productInfo.add(createLabel("Recommendation: " + recommendation));

                graphFrame.add(productInfo, BorderLayout.NORTH);

                 // Graph
                 GraphPanel graphPanel = new GraphPanel();
                 graphPanel.setArray(current);
                 //graphPanel.setYValues(sales);
                 //graphPanel.setLine(r.beta0, r.beta1);
                 graphFrame.add(graphPanel, BorderLayout.CENTER);

                 graphFrame.setVisible(true);
            } else if (current.size() > 1) {
                JFrame graphFrame = new JFrame("Product Trends");
                graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Just closes window, not whole program
                graphFrame.setSize(700, 600);
                graphFrame.setLayout(new BorderLayout());
                graphFrame.setResizable(false);

                // Graph Legend
                JPanel legendPanel = new JPanel();
                int rows = (int) Math.ceil(current.size() / 2.0);
                legendPanel.setLayout(new GridLayout(rows, 2, 10, 5));
                legendPanel.setBorder(BorderFactory.createTitledBorder("Legend"));

                Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE,
                        Color.PINK, Color.CYAN, Color.YELLOW, Color.GRAY, Color.DARK_GRAY};

                for (int i = 0; i < current.size() && i < 10; i++) {
                    product p = current.get(i);
                    Color color = colors[i % colors.length];

                    JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT));

                    // Colored square
                    JLabel colorBox = new JLabel();
                    colorBox.setOpaque(true);
                    colorBox.setBackground(color);
                    colorBox.setPreferredSize(new Dimension(15, 15));

                    // Product label
                    JLabel label = new JLabel(" " + p.getName());

                    item.add(colorBox);
                    item.add(label);
                    legendPanel.add(item);
                }

                // Graph of top ten products
                GraphPanel graphPanel = new GraphPanel();
                graphPanel.setArray(current);

                graphFrame.add(legendPanel, BorderLayout.NORTH);
                graphFrame.add(graphPanel, BorderLayout.CENTER);
                graphFrame.setVisible(true);
            }
        }

        // Helper method to add a label
        private static JLabel createLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("SansSerif", Font.PLAIN, 14));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Top/bottom spacing
            return label;
        }

        //This portion is added by ChatGPT, modified by Clayton Frandeen
        public static class GraphPanel extends JPanel {
            private ArrayList<product> current;
            public void setArray (ArrayList<product> products) {
                current = products;
                repaint();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                int width = getWidth();
                int height = getHeight();
                int maxY = 10000;
                int padding = 50;
                int labelPadding = 30;
                int graphWidth = width - 2 * padding - labelPadding;
                int graphHeight = height - 2 * padding;

                int originX = padding + labelPadding;
                int originY = height - padding;

                // Draw border
                g2.drawRect(originX, padding, graphWidth, graphHeight);

                // Draw grid lines and labels
                int numYDivisions = 10;
                for (int i = 0; i <= numYDivisions; i++) {
                    int y = originY - (i * graphHeight / numYDivisions);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.drawLine(originX, y, originX + graphWidth, y);
                    g2.setColor(Color.BLACK);
                    g2.drawString(Integer.toString(i * (maxY/ numYDivisions)), originX - 40, y + 5);
                }

                int numXDivisions = 10;
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d");
                for (int i = 0; i <= numXDivisions; i++) {
                    int x = originX + (i * graphWidth / numXDivisions);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.drawLine(x, originY, x, padding);
                    g2.setColor(Color.BLACK);

                    LocalDate labelDate = currentDate.minusDays((8 - i) * 7L);
                    String dateLabel = labelDate.format(formatter);
                    g2.drawString(dateLabel, x - 5, originY + 20);
                    //g2.drawString(String.valueOf(currentDate), x, originY - 20);
                }

                // Axis labels
                g2.drawString("Weeks", originX + graphWidth / 2 - 20, height - 10);

                Graphics2D g2Rotated = (Graphics2D) g2.create();
                g2Rotated.rotate(-Math.PI / 2);
                g2Rotated.drawString("Sales", -height / 2 - 20, 20);
                g2Rotated.dispose();

                // Plot each product
                Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.CYAN, Color.YELLOW, Color.GRAY, Color.DARK_GRAY};

                for (int pIndex = 0; pIndex < current.size() && pIndex < 10; pIndex++) {
                    product p = current.get(pIndex);
                    int[] sales = productTrend.toNum(p.getSalesBy7());
                    Color c = colors[pIndex % colors.length];
                    g2.setColor(c);
                    // Plot points
                    int pointDiameter = 8;
                    int xStep = graphWidth / numXDivisions;
                    for (int i = 0; i < sales.length; i++) {
                        int x = originX + ((i + 1) * xStep);
                        int y = originY - (sales[i] * graphHeight / maxY); // scale y to 0–1000

                        g.fillOval(x - pointDiameter / 2, y - pointDiameter / 2, pointDiameter, pointDiameter);
                    }

                    //Draw Regression Line
                    productTrend.regressionResult r = calculateRegression(p);
                    float slope = r.beta1;
                    float intercept = r.beta0;

                    Graphics2D g3 = (Graphics2D) g;

                    g3.setStroke(new BasicStroke(2));

                    // Graph x-values from 0 to 10
                    double x1 = 0;
                    double x2 = 10;

                    // Calculate y-values for those x's
                    double y1 = slope * x1 + intercept;
                    double y2 = slope * x2 + intercept;

                    // Clamp y values between 0 and 1000 for clean drawing
                    y1 = Math.max(0, Math.min(maxY, y1));
                    y2 = Math.max(0, Math.min(maxY, y2));

                    // Convert to pixel coords
                    int px1 = originX + (int) (x1 * xStep);
                    int py1 = originY - (int) (y1 * graphHeight / maxY);
                    int px2 = originX + (int) (x2 * xStep);
                    int py2 = originY - (int) (y2 * graphHeight / maxY);

                    g3.drawLine(px1, py1, px2, py2);
                }

            } // End paintComponent

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 600);
            }
        }
    } // End productTrendUI


    /*****************
     * Ulyses UI
     ******************/
    public class EventRecommendationUI {
     public static JPanel open(ArrayList<ArrayList<product>> productsByEvent, Map<String, Integer> eventIndex, CardLayout topShopsLayout, JPanel topShopsPanel) {
            // === Set up JFrame ===
            JPanel frame = new JPanel();
            frame.setLayout(new BorderLayout());
            frame.setBackground(Color.WHITE);

            // Title label
            JLabel titleLabel = new JLabel("Event Recommendations", SwingConstants.CENTER);
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

            JPanel productsPanel = new JPanel();
            productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));

            JScrollPane productsScrollPane = new JScrollPane(productsPanel);

            rightPanel.add(productLabel, BorderLayout.NORTH);
            rightPanel.add(productsScrollPane, BorderLayout.CENTER);

            // === Update product list when dropdown changes ===
            eventDropdown.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String event = (String) eventDropdown.getSelectedItem();
                    if (eventIndex.containsKey(event)) {
                        List<product> topProducts = getTop10ByEvent(event, productsByEvent, eventIndex);
                        updateProducts(productsPanel,topProducts);
                    } else {
                        updateProducts(productsPanel,Collections.emptyList());
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

            JButton homeBtn = new JButton("🏠 Home");
            homeBtn.setFocusPainted(false);
            JButton viewAllTrends = new JButton("View All Trends");
            topRightPanel.add(viewAllTrends);
            topRightPanel.add(homeBtn);

            frame.add(topRightPanel, BorderLayout.PAGE_START);

            // === Button Actions ===

            // Home button (empty for now)
            homeBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    topShopsLayout.show(topShopsPanel, "Home");
                }
            });

         viewAllTrends.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 String event = (String) eventDropdown.getSelectedItem();
                     ArrayList<product> topProducts = productsByEvent.get(eventIndex.get(event));
                     productTrendUI.graph(topProducts);
                 }
         });
            frame.setVisible(true);
            return frame;
        }
    }

    static void updateProducts(JPanel eventPanel, List<product> products) {
        eventPanel.removeAll();
        if(products.size() == 0) {
            eventPanel.add(new JLabel("No products found."));
        } else {
            for (int i = 0; i < 10 && i < products.size(); i++) {
                product p = products.get(i);

                JPanel entryPanel = new JPanel(new BorderLayout());
                JLabel productInfo = new JLabel( (i+1) + ". " + p.getName() + "    $" + p.getPrice() + "    " + p.getTotalSales());
                ArrayList<product> current = new ArrayList<>();
                current.add(p);
                JButton viewTrends = new JButton("View Product Trends");
                viewTrends.addActionListener(e ->  {
                    productTrendUI.graph(current);
                });

                entryPanel.add(productInfo, BorderLayout.WEST);
                entryPanel.add(viewTrends, BorderLayout.EAST);
                eventPanel.add(entryPanel);
            }
        }
        eventPanel.revalidate();
        eventPanel.repaint();
    }
// Daniel - Top Ten Products UI
    public class TopTenProductsUI {
        public static JPanel open(ArrayList<ArrayList<product>> productsByType, Map<String, Integer> categoryIndex, CardLayout topShopsLayout, JPanel topShopsPanel) {
            JPanel frame = new JPanel();
            frame.setLayout(new BorderLayout());

            JPanel contentPanel = new JPanel(new GridLayout(1, 2, 50, 0));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
            contentPanel.setBackground(Color.WHITE);
            //Left Panel (Dropdown Menu)
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.setBackground(Color.WHITE);

            JLabel dropdownLabel = new JLabel("Product Categories", SwingConstants.CENTER);
            dropdownLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            dropdownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            List<String> categoryNames = new ArrayList<>(categoryIndex.keySet());
            Collections.sort(categoryNames);
            categoryNames.add(0, "All Categories");
            JComboBox<String> categoryDropdown = new JComboBox<>(categoryNames.toArray(new String[0]));
            categoryDropdown.setFont(new Font("SansSerif", Font.PLAIN, 12));

            categoryDropdown.setPreferredSize(new Dimension(150, 25));
            categoryDropdown.setMaximumSize(new Dimension(150, 25));
            categoryDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);


            leftPanel.add(dropdownLabel);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            leftPanel.add(categoryDropdown);

            // Right Panel (Products)
            JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
            rightPanel.setBackground(Color.WHITE);

            JLabel productLabel = new JLabel("Products", SwingConstants.CENTER);
            productLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

            JPanel productsPanel = new JPanel();
            productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));

            JScrollPane productsScrollPane = new JScrollPane(productsPanel);

            rightPanel.add(productLabel, BorderLayout.NORTH);
            rightPanel.add(productsScrollPane, BorderLayout.CENTER);

            List<product> allProducts = getTop10ProductsBySales(productsByType);

            // === Update product list when dropdown changes ===
            categoryDropdown.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String category = (String) categoryDropdown.getSelectedItem();
                    if (category.equals("All Categories")) {
                        updateProducts(productsPanel, allProducts);
                    }
                    else if (categoryIndex.containsKey(category)) {
                        List<product> topProducts = productsByType.get(categoryIndex.get(category));
                        updateProducts(productsPanel,topProducts);
                    } else {
                        updateProducts(productsPanel,Collections.emptyList());
                    }
                }
            });

            if (categoryDropdown.getItemCount() > 0) {
                categoryDropdown.setSelectedIndex(0); // Auto-load first event if available
            }

            contentPanel.add(leftPanel);
            contentPanel.add(rightPanel);
            frame.add(contentPanel, BorderLayout.CENTER);
            JLabel titleLabel = new JLabel("Top 10 Products by Sales", SwingConstants.CENTER);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            frame.add(titleLabel, BorderLayout.NORTH);

            frame.setVisible(true);

            JPanel topRightPanel = new JPanel();
            topRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 10));
            topRightPanel.setBackground(Color.WHITE);

            JButton homeBtn = new JButton("🏠 Home");
            JButton viewAllTrends = new JButton("View All Trends");
            homeBtn.setFocusPainted(false);
            viewAllTrends.setFocusPainted(false);

            topRightPanel.add(viewAllTrends);
            topRightPanel.add(homeBtn);

            frame.add(topRightPanel, BorderLayout.PAGE_START);
            homeBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    topShopsLayout.show(topShopsPanel, "Home");
                }
            });

            viewAllTrends.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String category = (String) categoryDropdown.getSelectedItem();
                    if (category.equals("All Categories")) {
                        ArrayList<product> topTen = new ArrayList<>(allProducts);
                        productTrendUI.graph(topTen);
                    } else if (categoryIndex.containsKey(category)) {
                        ArrayList<product> topProducts = productsByType.get(categoryIndex.get(category));
                        productTrendUI.graph(topProducts);
                    }
                }
            });

            return frame;
        }
    }



}
