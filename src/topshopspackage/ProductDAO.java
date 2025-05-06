package topshopspackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ProductDAO {

    public List<product> getAllProducts() {
        List<product> products = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/topshops";
        String user = "nopass";
        String password = "";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                product p = new product();
                p.setName(rs.getString("name"));
                p.setPrice(String.valueOf(rs.getDouble("price")));
                p.setCompany(rs.getString("company"));
                p.setType(rs.getString("type"));
                p.setEvent(rs.getString("event"));
                p.setTotalSales(String.valueOf(rs.getInt("totalSales")));
                p.setSalesBy7(rs.getString("salesBy8"));
                products.add(p);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
