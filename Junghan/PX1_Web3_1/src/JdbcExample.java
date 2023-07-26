import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class JdbcExample {
    public static void main(String[] args) {
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            prop.load(fis);

            String url = prop.getProperty("db.url");
            String username = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String selectQuery = "SELECT * FROM product";
                PreparedStatement pstmt = conn.prepareStatement(selectQuery);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int product_code = rs.getInt("product_code");
                    String product_name = rs.getString("product_name");
                    double price = rs.getDouble("price");
                    int stock_quantity = rs.getInt("stock_quantity");
                    System.out.println("product_code: " + product_code
                    		+ ", product_name: " + product_name
                    		+ ", price: " + price
                    		+ ", stock_quantity: " + stock_quantity);
                }

                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String selectQuery = "SELECT * FROM order_table";
                PreparedStatement pstmt = conn.prepareStatement(selectQuery);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int order_number = rs.getInt("order_number");
                    int order_quantity = rs.getInt("order_quantity");
                    int product_code = rs.getInt("product_code");
                    System.out.println("order_number: " + order_number 
                    		+ ", order_quantity: " + order_quantity
                    		+ ", product_code: " + product_code);

                }

                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

