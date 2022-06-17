
package main;

public class Product 
{
    private String name;
    private String price;
    private String description;
    private String id;
    
    public Product(String name, String price, String description, String id)
    {
        this.name = name;
        this.price = price;
        this.description = description;
        this.id = id;
    }
    
    @Override
    public String toString()
    {
        return "Name: " + name + "\n"
                + "Price: " + price + "\n"
                + "Description: " + description + "\n"
                + "ID: " + id + "\n"; 
    }
    
}
