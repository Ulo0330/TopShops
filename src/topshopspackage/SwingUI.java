package topshopspackage;

import javax.swing.*;

//UI logic
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import topshopspackage.product;

public class SwingUI {
    //Create GUI a show it.
    public static void CreateAndShowGUI(ArrayList<ArrayList<product>> productsByType, Map<String, Integer> categoryIndex,
                                        ArrayList<ArrayList<product>> productsByCompany, Map<String, Integer> companyIndex, ArrayList<ArrayList<product>> productsByEvent
            , Map<String, Integer> eventIndex) {

        // Create and set up the window
        JFrame TopShops = new JFrame("TopShops");
        TopShops.setSize(100000, 100000);
        TopShops.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TopShops.setLayout(new GridLayout(2, 3));

        // add label to view top ten products
        JLabel TopTenLabel = new JLabel("View Top Ten Products");
        JLabel TopTenCompanyLabel = new JLabel("View Top Ten Company");
        JLabel EventLabel = new JLabel("View Event Recommendations");
        TopShops.add(TopTenCompanyLabel);
        TopShops.add(TopTenLabel);
        TopShops.add(EventLabel);


        JButton TopTenButton = new JButton("View Top Ten Products");
        TopTenButton.addActionListener(e -> {

        });
        TopShops.add(TopTenButton);

        JButton TopTenCompanyButton = new JButton("View Top Ten Companies");
        TopTenButton.addActionListener(e -> {

        });
        TopShops.add(TopTenCompanyButton);

        JButton EventButton = new JButton("View Products Recommendations");
        TopTenButton.addActionListener(e -> {

        });
        TopShops.add(EventButton);

        //JButton ViewTrends = new JButton("Product Trend");

       // ViewTrends.addActionListener(e -> {
        //    productTrendUI.graph(productsByType.get(0).get(0));
       // });
       // TopShops.add(ViewTrends);

        //Display Window
        TopShops.pack();
        TopShops.setVisible(true);
    }

    public static void main(ArrayList<ArrayList<product>> productsByType, Map<String, Integer> categoryIndex,
                            ArrayList<ArrayList<product>> productsByCompany, Map<String, Integer> companyIndex, ArrayList<ArrayList<product>> productsByEvent
            , Map<String, Integer> eventIndex) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CreateAndShowGUI(productsByType, categoryIndex, productsByCompany, companyIndex, productsByEvent, eventIndex);
            }
        });
    }

    /* Author: Clayton Frandeen
     * Last Modified: April, 24, 2025
     * File description: This is the UI or view component of the Product Trends.
     *  It creates the window for product trends, calls out to get the information
     * for the product sales and regression line. It then outputs the product information
     * and the graph filled with plot points and regression line.
     */
    public class productTrendUI {
        public static void graph(product current) {
            int[] sales = productTrend.toNum(current.getSalesBy7());
            productTrend.regressionResult r = productTrend.calculateRegression(current);

            // Creating the frame set up for our product trend window
            JFrame graphFrame = new JFrame("Product Trends");
            graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Just closes window, not whole program
            graphFrame.setSize(500, 700);
            graphFrame.setLayout(new GridLayout(2, 1));
            graphFrame.setResizable(false);                               //Non-resizable, So information doesn't get distorted

            //Top panel for the product information
            JPanel productInfo = new JPanel();
            productInfo.setLayout(new GridLayout(3, 3));       //Grid layout to use JLabels to add in our information

            JLabel productName = new JLabel("   " + current.getName());
            productInfo.add(productName);

            JLabel productPrice = new JLabel("$" + current.getPrice());
            productInfo.add(productPrice);

            JLabel productSales = new JLabel("Total Sales: " + current.getTotalSales());
            productInfo.add(productSales);

            JLabel productCompany = new JLabel(current.getCompany());
            productInfo.add(productCompany);

            JLabel regressionLine = new JLabel("Line of best fit:");
            productInfo.add(regressionLine);

            JLabel regressionLine2 = new JLabel("y = " + r.beta1 + "x + " + r.beta0);
            productInfo.add(regressionLine2);

            float growthRate = productTrend.calculateGrowthRate(r.beta0, r.beta1);
            JLabel growthRateLabel = new JLabel("Growth rate: " + growthRate);
            productInfo.add(growthRateLabel);

            if (growthRate < 0.0) {
                JLabel recommend = new JLabel("Recommendation: Avoid this at all costs");
                productInfo.add(recommend);
            } else if (growthRate > 0.0 && growthRate < 10.0) {
                JLabel recommend = new JLabel("Recommendation: This is a risky investment!");
                productInfo.add(recommend);
            } else if (growthRate > 10.0 && growthRate < 50.0) {
                JLabel recommend = new JLabel("Recommendation: This is a good investment!");
                productInfo.add(recommend);
            } else if (growthRate > 50.0) {
                JLabel recommend = new JLabel("Recommendation: This is a great investment!");
                productInfo.add(recommend);
            }

            graphFrame.add(productInfo);
            //Graph
            GraphPanel graphPanel = new GraphPanel();
            graphPanel.setYValues(sales);
            graphPanel.setLine(r.beta0, r.beta1);
            graphFrame.add(graphPanel);

            graphFrame.setVisible(true);
        }

        //This portion is added by ChatGPT, modified by Clayton Frandeen
        public static class GraphPanel extends JPanel {
            private int[] yValues;
            private float intercept;
            private float slope;

            public void setYValues(int[] yValues) {
                this.yValues = yValues;
                repaint();
            }

            public void setLine(float intercept, float slope) {
                this.intercept = intercept;
                this.slope = slope;
                repaint();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                int width = getWidth();
                int height = getHeight();

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
                    g2.drawString(Integer.toString(i * 100), originX - 40, y + 5);
                }

                int numXDivisions = 10;
                LocalDate currentDate = LocalDate.now();
                for (int i = 0; i <= numXDivisions; i++) {
                    int x = originX + (i * graphWidth / numXDivisions);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.drawLine(x, originY, x, padding);
                    g2.setColor(Color.BLACK);
                    g2.drawString(Integer.toString(i), x - 5, originY + 20);
                    //g2.drawString(String.valueOf(currentDate), x, originY - 20);
                }

                // Axis labels
                g2.drawString("Weeks", originX + graphWidth / 2 - 20, height - 10);

                Graphics2D g2Rotated = (Graphics2D) g2.create();
                g2Rotated.rotate(-Math.PI / 2);
                g2Rotated.drawString("Sales", -height / 2 - 20, 20);
                g2Rotated.dispose();

                //Adding plot points
                if (yValues != null) {
                    int pointDiameter = 8;

                    int xStep = graphWidth / numXDivisions;

                    for (int i = 0; i < yValues.length; i++) {
                        int x = originX + ((i + 1) * xStep);
                        int y = originY - (yValues[i] * graphHeight / 1000); // scale y to 0–1000

                        g.setColor(Color.BLACK);
                        g.fillOval(x - pointDiameter / 2, y - pointDiameter / 2, pointDiameter, pointDiameter);
                    }
                }

                //Adding Regression Line
                if (yValues != null) {
                    int xStep = graphWidth / numXDivisions;

                    Graphics2D g3 = (Graphics2D) g;
                    g3.setColor(Color.BLUE);
                    g3.setStroke(new BasicStroke(2));

                    // Graph x-values from 0 to 10
                    double x1 = 0;
                    double x2 = 10;

                    // Calculate y-values for those x's
                    double y1 = slope * x1 + intercept;
                    double y2 = slope * x2 + intercept;

                    // Clamp y values between 0 and 1000 for clean drawing
                    y1 = Math.max(0, Math.min(1000, y1));
                    y2 = Math.max(0, Math.min(1000, y2));

                    // Convert to pixel coords
                    int px1 = originX + (int) (x1 * xStep);
                    int py1 = originY - (int) (y1 * graphHeight / 1000);
                    int px2 = originX + (int) (x2 * xStep);
                    int py2 = originY - (int) (y2 * graphHeight / 1000);

                    g3.drawLine(px1, py1, px2, py2);
                }

            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 600);
            }
        }
    }

}