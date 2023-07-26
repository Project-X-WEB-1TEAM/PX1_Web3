package Product;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class ProductInsert {


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
		            // Get product information from the user
		            System.out.print("Enter product name: ");
		            String productName = scanner.nextLine();
		
		            System.out.print("Enter price: ");
		            int price = scanner.nextInt();
		            scanner.nextLine();
		
		            System.out.print("Enter quantity: ");
		            int stock_quantity = scanner.nextInt();
		            scanner.nextLine();
		
		            // Connect to the database and insert the product information
		            try (Connection connection = DriverManager.getConnection(url, username, password)) {
		                String insertQuery = "INSERT INTO product (product_name, price, stock_quantity) VALUES (?, ?, ?)";
		                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
		                    preparedStatement.setString(1, productName);
		                    preparedStatement.setInt(2, price);
		                    preparedStatement.setInt(3, stock_quantity);
		
		                    int rowsAffected = preparedStatement.executeUpdate();
		                    if (rowsAffected > 0) {
		                        System.out.println("삽입이 성공적으로 이뤄졌습니다. ");
		                    } else {
		                        System.out.println("삽입동작이 실패하였습니다.");
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
