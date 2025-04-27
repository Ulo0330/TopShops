import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import topshopspackage.*;

import static topshopspackage.SwingUI.CreateAndShowGUI;
import static topshopspackage.filereader.fileReader;



public class TopShops {
    public static void main(String[] args) {
        Map<String, Integer> categoryIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByType = new ArrayList<>();

        Map<String, Integer> companyIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByCompany = new ArrayList<>();

        Map<String, Integer> eventIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByEvent = new ArrayList<>();

        String file = "/Users/claytonfrandeen/Documents/GitHub/TopShops2/src/topshopspackage/database.txt";

        fileReader(file, productsByType, categoryIndex, productsByCompany, companyIndex, productsByEvent, eventIndex);

        System.out.println(productsByType.get(0).get(0).getName());
       // product tempProduct = new product();
//        tempProduct = productsByType.get(0).get(0);
       // graph(tempProduct);

        CreateAndShowGUI(productsByType, categoryIndex, productsByCompany, companyIndex, productsByEvent, eventIndex);
        System.out.println("Products by Category");
        System.out.println("Electronics: ");
        //printProducts(productsByType.get(2));
        System.out.println();
        System.out.println("Products by Company");
        System.out.println("Apple: ");
        //printProducts(productsByCompany.get(3));


    }
}
