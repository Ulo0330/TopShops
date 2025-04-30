package topshopspackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static topshopspackage.SwingUI.CreateAndShowGUI;
import static topshopspackage.filereader.fileReader;


public class TopShops {
    public static void main(String[] args) {
        // Data structures for categorization
        Map<String, Integer> categoryIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByType = new ArrayList<>();

        Map<String, Integer> companyIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByCompany = new ArrayList<>();

        Map<String, Integer> eventIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByEvent = new ArrayList<>();

        // Read from MySQL database
        fileReader(productsByType, categoryIndex, productsByCompany, companyIndex, productsByEvent, eventIndex);

  

        // Print one product for verification
        if (!productsByType.isEmpty() && !productsByType.get(0).isEmpty()) {
            System.out.println(productsByType.get(0).get(0).getName());
        }

        // Start the GUI
        CreateAndShowGUI(productsByType, categoryIndex, productsByCompany, companyIndex, productsByEvent, eventIndex);

        // Optional debug prints
        System.out.println("Products by Category:");
        System.out.println("Electronics: ");
        // You can add: filereader.printProducts(productsByType.get(categoryIndex.get("Electronics")));
    }
}
