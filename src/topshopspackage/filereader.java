package topshopspackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class filereader {
    public static void fileReader(String fileName, ArrayList<ArrayList<product>> productsByType, Map<String, Integer> categoryIndex,
                                  ArrayList<ArrayList<product>> productsByCompany, Map<String, Integer> companyIndex, ArrayList<ArrayList<product>> productsByEvent
            , Map<String, Integer> eventIndex) {

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {  //how we will read file
            String line;
            int fieldCounter = 0;
            product temp = new product();
            String category = "";
            String company = "";
            String event = "";

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {            // if there is only a new line or space
                    continue;                           // skip
                }

                switch (fieldCounter) {                 //to differentiate the fields
                    case 0 -> temp.setName(line);       //set name
                    case 1 -> temp.setPrice(line);      //set price
                    case 2 -> {
                        temp.setCompany(line);
                        company = line;
                    }    //set company
                    case 3 -> {                         //set type
                        temp.setType(line);
                        category = line;                //type stored, to separate into categories in matrix
                    }
                    case 4 -> {
                        temp.setEvent(line);
                        event = line;
                    }      //set event
                    case 5 -> temp.setTotalSales(line); //set total sales
                    case 6 -> {
                        temp.setSalesBy7(line);         //set sales by last 7 days

                        //inputs product into matrix of types
                        if(!categoryIndex.containsKey(category)) {  //create a new key for our matrix
                            categoryIndex.put(category, productsByType.size());
                            productsByType.add(new ArrayList<>());  //create new array
                        }
                        productsByType.get(categoryIndex.get(category)).add(temp); //add product after fields are all set

                        //inputs product into matrix of companies
                        if(!companyIndex.containsKey(company)) {
                            companyIndex.put(company, productsByCompany.size());
                            productsByCompany.add(new ArrayList<>());
                        }
                        productsByCompany.get(companyIndex.get(company)).add(temp);

                        //inputs product into matrix of companies
                        if(!eventIndex.containsKey(event)) {
                            eventIndex.put(event, productsByEvent.size());
                            productsByEvent.add(new ArrayList<>());
                        }
                        productsByEvent.get(eventIndex.get(event)).add(temp);

                        temp = new product(); //new temp
                        fieldCounter = -1; //resets counter for next product
                    }
                }
                fieldCounter++; //increments counter
            }// end while loop

        } catch (IOException e) {
            System.out.println("Error reading file" + e.getMessage());
        }
    }

    public static void printProducts(ArrayList<product> products) {
        for(int i = 0 ; i < 10; i++) {
            System.out.println(products.get(i).getName() + " " + products.get(i).getPrice());
            if (products.size() - 1 == i) {
                break;
            }
        }
    }


}
