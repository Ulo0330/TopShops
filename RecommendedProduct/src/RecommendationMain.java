import java.util.*;

public class RecommendationMain {
    public static void main(String[] args) {
        // Set up data structures for fileReader
        Map<String, Integer> categoryIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByType = new ArrayList<>();

        Map<String, Integer> companyIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByCompany = new ArrayList<>();

        Map<String, Integer> eventIndex = new HashMap<>();
        ArrayList<ArrayList<product>> productsByEvent = new ArrayList<>();

        // Load data using the shared fileReader from Main.java
        String filePath = "database.txt"; // update path if needed
        Main.fileReader(filePath, productsByType, categoryIndex,
                productsByCompany, companyIndex,
                productsByEvent, eventIndex);
        System.out.println("✅ File loaded: " + filePath);
        System.out.println("📦 Events found: " + eventIndex.size());
        System.out.println("🔍 Events: " + eventIndex.keySet());


        // Build event menu
        List<String> eventNames = new ArrayList<>(eventIndex.keySet());
        Collections.sort(eventNames);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n🎉 Available Events:");
            for (int i = 0; i < eventNames.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, eventNames.get(i));
            }

            System.out.print("\nSelect an event by number (or 0 to quit): ");
            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Try again.");
                continue;
            }

            if (choice == 0) {
                System.out.println("👋 Exiting. Thanks!");
                break;
            }

            if (choice < 1 || choice > eventNames.size()) {
                System.out.println("❌ Invalid choice.");
                continue;
            }

            // Fetch top 10 recommendations from backend
            String selectedEvent = eventNames.get(choice - 1);
            List<product> topProducts = RecommendedByEvent.getTop10ByEvent(selectedEvent, productsByEvent, eventIndex);

            System.out.println("\n🔍 Top 10 Products for Event: " + selectedEvent);
            RecommendedByEvent.printTop10(topProducts); // optional
        }
    }
}
