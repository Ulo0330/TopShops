package topshopspackage;


import java.util.*;

public class RecommendedByEvent {

    public static List<product> getTop10ByEvent(String event,
                                                ArrayList<ArrayList<product>> productsByEvent,
                                                Map<String, Integer> eventIndex) {
        if (!eventIndex.containsKey(event)) {
            System.out.println("Event not found: " + event);
            return new ArrayList<>();
        }

        ArrayList<product> eventProducts = productsByEvent.get(eventIndex.get(event));

        // Sort by total sales (descending)
        eventProducts.sort((a, b) -> {
            try {
                return Integer.compare(
                        Integer.parseInt(b.getTotalSales().trim()),
                        Integer.parseInt(a.getTotalSales().trim())
                );
            } catch (NumberFormatException e) {
                return 0;
            }
        });

        return eventProducts.subList(0, Math.min(10, eventProducts.size()));
    }

    // Optional helper to display results (for dev use or console frontend)
    public static void printTop10(List<product> topProducts) {
        for (int i = 0; i < topProducts.size(); i++) {
            product p = topProducts.get(i);
            System.out.printf("%d. %s | Sales: %s | Price: %s | Company: %s\n",
                    i + 1, p.getName(), p.getTotalSales(), p.getPrice(), p.getCompany());
        }
    }
}