package topshopspackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class filereader {
    public static void fileReader(ArrayList<ArrayList<product>> productsByType, Map<String, Integer> categoryIndex,
                                  ArrayList<ArrayList<product>> productsByCompany, Map<String, Integer> companyIndex,
                                  ArrayList<ArrayList<product>> productsByEvent, Map<String, Integer> eventIndex) {

        String url = "jdbc:mysql://144.37.209.4:3306/topshops";
        String user = "root";
        String password = "Ulyses2004";  // Replace this with your actual password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                product temp = new product();
                String type = rs.getString("type");
                String company = rs.getString("company");
                String event = rs.getString("event");

                temp.setName(rs.getString("name"));
                temp.setPrice(String.valueOf(rs.getDouble("price")));
                temp.setCompany(company);
                temp.setType(type);
                temp.setEvent(event);
                temp.setTotalSales(String.valueOf(rs.getInt("totalSales")));
                temp.setSalesBy7(rs.getString("salesBy8"));
                temp.setMarketValue(rs.getString("marketValue"));

                // Organize by Type
                if (!categoryIndex.containsKey(type)) {
                    categoryIndex.put(type, productsByType.size());
                    productsByType.add(new ArrayList<>());
                }
                productsByType.get(categoryIndex.get(type)).add(temp);

                // Organize by Company
                if (!companyIndex.containsKey(company)) {
                    companyIndex.put(company, productsByCompany.size());
                    productsByCompany.add(new ArrayList<>());
                }
                productsByCompany.get(companyIndex.get(company)).add(temp);

                // Organize by Event
                if (!eventIndex.containsKey(event)) {
                    eventIndex.put(event, productsByEvent.size());
                    productsByEvent.add(new ArrayList<>());
                }
                productsByEvent.get(eventIndex.get(event)).add(temp);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void fileReader2(String fileName, ArrayList<ArrayList<product>> productsByType, Map<String, Integer> categoryIndex,
                                   ArrayList<ArrayList<product>> productsByCompany, Map<String, Integer> companyIndex,
                                   ArrayList<ArrayList<product>> productsByEvent, Map<String, Integer> eventIndex) {

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int fieldCounter = 0;
            product temp = new product();
            String category = "";
            String company = "";
            String event = "";

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                switch (fieldCounter) {
                    case 0 -> temp.setName(line);
                    case 1 -> temp.setPrice(line);
                    case 2 -> {
                        temp.setCompany(line);
                        company = line;
                    }
                    case 3 -> {
                        temp.setType(line);
                        category = line;
                    }
                    case 4 -> {
                        temp.setEvent(line);
                        event = line;
                    }
                    case 5 -> temp.setTotalSales(line);
                    case 6 -> temp.setSalesBy7(line);
                    case 7 -> {
                        temp.setMarketValue(line);

                        // Add product to Type grouping
                        if (!categoryIndex.containsKey(category)) {
                            categoryIndex.put(category, productsByType.size());
                            productsByType.add(new ArrayList<>());
                        }
                        productsByType.get(categoryIndex.get(category)).add(temp);

                        // Add product to Company grouping
                        if (!companyIndex.containsKey(company)) {
                            companyIndex.put(company, productsByCompany.size());
                            productsByCompany.add(new ArrayList<>());
                        }
                        productsByCompany.get(companyIndex.get(company)).add(temp);

                        // Add product to Event grouping
                        if (!eventIndex.containsKey(event)) {
                            eventIndex.put(event, productsByEvent.size());
                            productsByEvent.add(new ArrayList<>());
                        }
                        productsByEvent.get(eventIndex.get(event)).add(temp);

                        temp = new product();  // Prepare for the next product
                        fieldCounter = -1;     // Reset for next loop
                    }
                }
                fieldCounter++;
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static class Top10Fetcher {

        // Fetch top 10 products based on total unit sales
        public static List<product> fetchTop10ProductsBySales(ArrayList<ArrayList<product>> groupedProducts) {
            List<product> allProducts = new ArrayList<>();

            for (List<product> group : groupedProducts) {
                allProducts.addAll(group);
            }

            allProducts.sort((p1, p2) -> Integer.compare(p2.getTotalSalesInt(), p1.getTotalSalesInt()));

            return allProducts.subList(0, Math.min(10, allProducts.size()));
        }

        // Fetch top 10 companies based on total market value
        public static List<product> fetchTop10CompaniesByMarketValue(ArrayList<ArrayList<product>> groupedProducts) {
            Map<String, Long> companyMarketValueMap = new HashMap<>();

            for (List<product> group : groupedProducts) {
                for (product p : group) {
                    String company = p.getCompany();
                    long value = p.getMarketValueLong();
                    companyMarketValueMap.put(company, companyMarketValueMap.getOrDefault(company, 0L) + value);
                }
            }

            // Sort companies by total market value in descending order
            List<Map.Entry<String, Long>> sortedCompanies = new ArrayList<>(companyMarketValueMap.entrySet());
            sortedCompanies.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

            // Create dummy product objects for UI display
            List<product> topCompanies = new ArrayList<>();
            for (int i = 0; i < Math.min(10, sortedCompanies.size()); i++) {
                Map.Entry<String, Long> entry = sortedCompanies.get(i);
                product p = new product();
                p.setCompany(entry.getKey());
                p.setMarketValue(String.valueOf(entry.getValue()));
                topCompanies.add(p);
            }

            return topCompanies;
        }
    }
}

