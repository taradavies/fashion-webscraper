package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FashionWebscraper 
{
    private static Document websiteDoc;
    private static HashSet<String> newArrivalProductLinks = new HashSet<>();
    private static ArrayList<Product> products = new ArrayList<>();

    
    public static void main(String[] args) 
    {
        String websiteURL = "https://fordays.com/";
        websiteDoc = CreateDocument(websiteURL);
        
        PrintWebsiteTitle();
        
        GetIndividualProductLinks();
        
        GetSpecificProductDetails();
        
        PrintProducts();
        
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

    private static void PrintWebsiteTitle() 
    {
        System.out.println("---Website Title---");
        System.out.println(websiteDoc.title());
    }

    private static void GetSpecificProductDetails()
    {
        for (String productLink : newArrivalProductLinks) 
        {
            ParseProduct(productLink);
        }
    }

    private static void ParseProduct(String productLink) 
    {
        Document productDoc = CreateDocument(productLink);
        
        String productName = GetProductName(productDoc);
        String productPrice = GetProductPrice(productDoc);
        String productDescription = GetProductDescription(productDoc);
        
        products.add(CreateProduct(productName, productPrice, productDescription));
    }

    private static String GetProductPrice(Document productDoc) 
    {
        Element productPrice = productDoc.getElementById("product-new-price");
        return productPrice.text().trim();
    }

//    private static String GetProductName(Document productDoc)
//    {
////        Element productName = productDoc.GetEleme
//    }

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

    private static Product CreateProduct(String productName, String productPrice, String productDescription) {
        return new Product(productName, productPrice, productDescription);
    }

    private static void PrintProducts() 
    {
        System.out.println("---Products---");
        for (Product product : products)
        {
            System.out.println(product);
        }
    }
}
