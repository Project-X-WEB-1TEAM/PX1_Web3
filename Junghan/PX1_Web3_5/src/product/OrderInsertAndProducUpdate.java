package product;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class OrderInsertAndProducUpdate {
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

		                System.out.print("Enter order quantity: ");
		                int orderQuantity = scanner.nextInt();
		                scanner.nextLine(); 
			
			
			            try (Connection connection = DriverManager.getConnection(url, username, password)) {
			            	 if (isProductAvailable(connection, productCode, orderQuantity)) {
			                        // 트랜젝션 시
			                        connection.setAutoCommit(false);

			                        // 주문 정보 삽입 
			                        String insertOrderQuery = "INSERT INTO order_table (product_code, order_quantity) VALUES (?, ?)";
			                        try (PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderQuery,
			                                Statement.RETURN_GENERATED_KEYS)) {
			                            insertOrderStatement.setInt(1, productCode);
			                            insertOrderStatement.setInt(2, orderQuantity);

			                            int rowsAffected = insertOrderStatement.executeUpdate();
			                            if (rowsAffected > 0) {
			                                System.out.println("정상적으로 주문이 들어갔습니다. ");
			                            } else {
			                                System.out.println("주문 실패 ");
			                                connection.rollback(); // 실패 시,원자성 보장을 위해 롤백 
			                            }
			                        }

			                        // Update product quantity
			                        String updateProductQuery = "UPDATE product SET stock_quantity = stock_quantity - ? WHERE product_code = ?";
			                        try (PreparedStatement updateProductStatement = connection.prepareStatement(updateProductQuery)) {
			                            updateProductStatement.setInt(1, orderQuantity);
			                            updateProductStatement.setInt(2, productCode);

			                            int rowsAffected = updateProductStatement.executeUpdate();
			                            if (rowsAffected > 0) {
			                                System.out.println("정상적으로 재고가 변경되었습니다.");
			                            } else {
			                                System.out.println("재고 수량 변경 실패 ");
			                                connection.rollback(); // 실패 시,원자성 보장을 위해 롤백 
			                            }
			                        }

			                        connection.commit(); // 성공시 커밋 
			                        connection.setAutoCommit(true);
			                    } else {
			                        System.out.println("코드가 이상하거나 수량이 부족합니다. ");
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
	  
	  private static boolean isProductAvailable(Connection connection, int productCode, int orderQuantity) throws SQLException {
		    String selectProductQuery = "SELECT stock_quantity FROM product WHERE product_code = ?";
		    try (PreparedStatement selectProductStatement = connection.prepareStatement(selectProductQuery)) {
		        selectProductStatement.setInt(1, productCode);
		        try (ResultSet resultSet = selectProductStatement.executeQuery()) {
		            if (resultSet.next()) {
		                int availableQuantity = resultSet.getInt("stock_quantity");
		                return availableQuantity >= orderQuantity;
		            }
		        }
		    }
		    return false;
		}

}
