package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FashionWebscraper 
{
    private static Document websiteDoc;
    private static HashSet<String> newArrivalProductLinks = new HashSet<>();
    private static ArrayList<Product> products = new ArrayList<>();
    
    private static FashionDatabase database;
    
    public static void main(String[] args) 
    {
        String websiteURL = "https://fordays.com/";
        websiteDoc = CreateDocument(websiteURL);
        
        printWebsiteTitle();
              
        getSpecificProductDetails();
        
        database = new FashionDatabase("fordaysProducts");
                
        addProductsToDatabase();
        
        printProducts();
    }
    
    private static void printWebsiteTitle() 
    {
        System.out.println("---Website Title---");
        System.out.println(websiteDoc.title());
        System.out.println("");
    }
    
    private static void getSpecificProductDetails()
    {
        GetIndividualProductLinks();
        
        for (String productLink : newArrivalProductLinks) 
        {
            ParseProduct(productLink);
        }
    }

    public static void GetIndividualProductLinks() 
    {         
        Elements newArrivalProducts = websiteDoc.getElementsByClass("grid-product__link");
        
        for (Element product : newArrivalProducts)
        {
            String productLink = "https://fordays.com" + product.attr("href");
            newArrivalProductLinks.add(productLink);
        }
    }

    private static Document CreateDocument(String websiteURL) 
    {
        try
        {
            websiteDoc = Jsoup.connect(websiteURL).timeout(6000).get();
            return websiteDoc;
        }
        catch (IOException e)
        {
            System.out.println("Error connecting to website.");
        }
        return null;
    }

    private static void ParseProduct(String productLink) 
    {
        Document productDoc = CreateDocument(productLink);
        
        String productName = GetProductName(productDoc);
        String productPrice = GetProductPrice(productDoc);
        String productDescription = GetProductDescription(productDoc);
        String productID = GetProductID(websiteDoc);
        
        products.add(new Product(productName, productPrice, productDescription, productID));
    }

    private static String GetProductPrice(Document productDoc) 
    {
        Element productPrice = productDoc.getElementById("product-new-price");
        return productPrice.text().trim();
    }

    private static String GetProductDescription(Document productDoc) 
    {
        String descriptionText = "";
        Elements productDescriptions = productDoc.getElementsByClass("product-single__description rte");
        for (Element description : productDescriptions)
        {
            descriptionText = description.text();
            break;
        }
        return descriptionText;
    }

    private static String GetProductName(Document productDoc) 
    {
        String productName = "";
        Elements productNames = productDoc.getElementsByClass("grid-product__title text-sm-h4 text-lg-h5");
        for (Element name : productNames)
        {
            productName = name.text();
            break;
        }
        return productName;
    }

    private static String GetProductID(Document websiteDoc)
    {
        String productID = "";
        Elements productIDs = websiteDoc.getElementsByAttribute("data-product-id");
        
        for (Element ID : productIDs)
        {
            productID = ID.attr("data-product-id");
            break;
        }
        return productID;
    }
    
    private static void printProducts() 
    {
        System.out.println("---Products---");
        for (Product product : products)
        {
            System.out.println(product);
        }
    }

    private static void addProductsToDatabase()
    {
        for (Product product : products)
        {
            database.addProductToDatabase(product);
        }
    }
}
