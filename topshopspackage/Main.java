package com.topshopspackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.topshopspackage.filereader.fileReader;
import static com.topshopspackage.filereader.printProducts;


public class Main {
    public static void main(String[] args) {
        Map<String, Integer> categoryIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByType = new ArrayList<>();

        Map<String, Integer> companyIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByCompany = new ArrayList<>();

        Map<String, Integer> eventIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByEvent = new ArrayList<>();

        String file = "/Users/blkhttr/Desktop/dev/School/CS370/TopShops/src/main/resources/database.txt";

        fileReader(file, productsByType, categoryIndex, productsByCompany, companyIndex, productsByEvent, eventIndex);

        System.out.println("Products by Category");
        System.out.println("Electronics: ");
        printProducts(productsByType.get(2));
        System.out.println();
        System.out.println("Products by Company");
        System.out.println("Apple: ");
        printProducts(productsByCompany.get(3));

    }
}