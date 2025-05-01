package topshopspackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static topshopspackage.SwingUI.CreateAndShowGUI;

import static topshopspackage.filereader.fileReader;
import static topshopspackage.filereader.fileReader2;


public class TopShops {
    public static void main(String[] args) {
        // Data structures for categorization
        Map<String, Integer> categoryIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByType = new ArrayList<>();

        Map<String, Integer> companyIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByCompany = new ArrayList<>();

        Map<String, Integer> eventIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByEvent = new ArrayList<>();

        String fileName = "/Users/claytonfrandeen/Documents/Programs/TopShops/src/topshopspackage/database.txt";
        // Read from MySQL database
        fileReader2(fileName, productsByType, categoryIndex, productsByCompany, companyIndex, productsByEvent, eventIndex);
        //fileReader(productsByType, categoryIndex, productsByCompany, companyIndex, productsByEvent, eventIndex);

        // Start the GUI
        CreateAndShowGUI(productsByType, categoryIndex, productsByCompany, companyIndex, productsByEvent, eventIndex);
    }
}
