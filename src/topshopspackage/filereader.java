package topshopspackage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class filereader {
    public static void fileReader(ArrayList<ArrayList<product>> productsByType, Map<String, Integer> categoryIndex,
                                  ArrayList<ArrayList<product>> productsByCompany, Map<String, Integer> companyIndex,
                                  ArrayList<ArrayList<product>> productsByEvent, Map<String, Integer> eventIndex) {

        String url = "jdbc:mysql://localhost:3306/topshops";
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
}
