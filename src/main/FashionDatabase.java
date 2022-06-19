package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FashionDatabase 
{
    private final String CONNECTIONURL = "jdbc:mysql://localhost:3306/fashion";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";
    private final Connection CONNECTION = connectToDatabase();
    private final String TABLENAME;
    
    public FashionDatabase(String tableName)
    {
        TABLENAME = tableName;
        createTableForWebsite();
    }
    
    private void createTableForWebsite()
    {
        try 
        {
            String query = "CREATE TABLE IF NOT EXISTS " + TABLENAME 
                    + "(product_name varchar(200), "
                    + " product_id varchar(20),  "
                    + " product_price DOUBLE, "
                    + " product_description varchar(1000))";

            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            preparedStatement.execute();
        }
        catch (SQLException e) 
        {
            System.out.println("Error creating table for database...");
        }
    }
    
    public void readFromColumnOfTable(String columnName) throws SQLException
    {
        String query = "SELECT * FROM " + TABLENAME;
        
        Statement statement = CONNECTION.createStatement();
        
        ResultSet sqlResults = statement.executeQuery(query);
        
        while (sqlResults.next())
        {
            String productName = sqlResults.getString(columnName);
        }
    }
    
    public void addProductToDatabase(Product product)
    {
        try 
        {
            String query = "INSERT INTO " + TABLENAME
                    + " (product_name, "
                    + "product_id, "
                    + "product_price,"
                    + " product_description)"
                    + "values (?,?,?,?)";
            
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(query);
            
            appendProductInfoToDatabase(preparedStatement, product);
            
            preparedStatement.execute();
        }
        catch (SQLException e)
        {
            System.out.println("Error adding product to database...");
        }
    }
    
    
    private Connection connectToDatabase()
    {
        try
        {
            Connection connection = DriverManager.getConnection(CONNECTIONURL, USERNAME, PASSWORD);
            return connection;
        } 
        catch (SQLException e) 
        {
            System.out.println("Error creating connection to database...");
            return null;
        }
    }

    private void appendProductInfoToDatabase(PreparedStatement preparedStatement, Product product)
    {
        try 
        {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getID());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setString(4, product.getDescription());
        }
        catch (SQLException e)
        {
            System.out.println("Error appending product values...");
        }
    }
}
