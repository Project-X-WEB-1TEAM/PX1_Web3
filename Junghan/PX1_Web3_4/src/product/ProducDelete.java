package product;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class ProducDelete {
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
		            System.out.print("Enter product code: ");
		            int productCode = scanner.nextInt();
		            scanner.nextLine();
		
		
		            try (Connection connection = DriverManager.getConnection(url, username, password)) {
		                String selectQuery = "DELETE FROM product WHERE product_code = ?";
		                try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
		                    preparedStatement.setInt(1, productCode);
		                    
	                        int rowsAffected = preparedStatement.executeUpdate();
	                        if (rowsAffected > 0) {
	                            System.out.println("삭제가 성공적으로 이뤄졌습니다.");
	                        } else {
	                            System.out.println("상품코드가 일치하는 것이 없습니다.");
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
