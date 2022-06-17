
package main;

public class Product 
{
    private String name;
    private String price;
    private String description;
    
    public Product(String name, String price, String description)
    {
        this.name = name;
        this.price = price;
        this.description = description;
    }
    
    @Override
    public String toString()
    {
        return "Name: " + name + "\n"
                + "Price: " + price + "\n"
                + "Description: " + description + "\n"; 
    }
    
}
