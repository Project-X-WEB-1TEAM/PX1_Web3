package product;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class ProductSearch {
    public static void main(String[] args) {
        Properties prop = new Properties();	
    	try {
    		 FileInputStream fis = new FileInputStream("config.properties");
             prop.load(fis);

             String url = prop.getProperty("db.url");
             String username = prop.getProperty("db.username");
             String password = prop.getProperty("db.password");
	
	        try {
	        	
	            Scanner scanner = new Scanner(System.in);
	            
	            while (true) {
		            System.out.print("Enter product name: ");
		            String productName = scanner.nextLine();
		
		
		            try (Connection connection = DriverManager.getConnection(url, username, password)) {
		                String selectQuery = "SELECT * FROM product WHERE product_name LIKE ?";
		                try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
		                    preparedStatement.setString(1, "%" + productName + "%");
		                    ResultSet rs = preparedStatement.executeQuery();
		                    
		                    while (rs.next()) {
		                        String productCode = rs.getString("product_code");
		                        String productResultName = rs.getString("product_name");
		                        int price = rs.getInt("price");
		                        int stockQuantity = rs.getInt("stock_quantity");
		                        System.out.println("상품코드: " + productCode + ", 상품명: " + productResultName + ", 가격: " + price + ", 재고수량: " + stockQuantity);
		                    }
		                }
		            } catch (SQLException e) {
		                e.printStackTrace();
		            }
		            
	                System.out.print("계속 할겨 ? (Y/N): ");
	                String input = scanner.nextLine();
	                if (input.equalsIgnoreCase("N")) {
	                    break;
	                }
	            }
	            
	            System.out.println("프로그램 종료 되었습니다. ");
	            scanner.close();
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
    	} catch (IOException e) {
            e.printStackTrace();
        }
    }

}
