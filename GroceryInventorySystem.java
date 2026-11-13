import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

// Structure to represent a Grocery Item
class GroceryItem {
    int id;
    String name;
    String category;
    int quantity;
    double price;

    public GroceryItem(int id, String name, String category, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }
}

public class GroceryInventorySystem {
    // Hash Map for O(1) lookups by Item ID
    private HashMap<Integer, GroceryItem> inventoryMap = new HashMap<>();

    // Min-Heap (Priority Queue) to quickly find items with low stock
    private PriorityQueue<GroceryItem> lowStockHeap = new PriorityQueue<>(
        (a, b) -> Integer.compare(a.quantity, b.quantity)
    );

    // Helper to rebuild the low-stock heap
    private void refreshLowStockHeap() {
        lowStockHeap.clear();
        for (GroceryItem item : inventoryMap.values()) {
            if (item.quantity <= 10) { // Threshold for low stock is 10 units
                lowStockHeap.offer(item);
            }
        }
    }

    // 1. Add or Update Item - O(1) average
    public void addOrUpdateItem(int id, String name, String category, int quantity, double price) {
        GroceryItem newItem = new GroceryItem(id, name, category, quantity, price);
        inventoryMap.put(id, newItem);
        refreshLowStockHeap();
        System.out.println("\n[Success] Item '" + name + "' added/updated successfully.");
    }

    // 2. Search Item by ID - O(1) average
    public void searchItem(int id) {
        if (inventoryMap.containsKey(id)) {
            GroceryItem item = inventoryMap.get(id);
            System.out.println("\n--- Item Details ---");
            System.out.println("ID       : " + item.id);
            System.out.println("Name     : " + item.name);
            System.out.println("Category : " + item.category);
            System.out.println("Quantity : " + item.quantity);
            System.out.printf("Price    : ₹%.2f\n", item.price);
        } else {
            System.out.println("\n[Error] Item with ID " + id + " not found!");
        }
    }

    // 3. Delete Item - O(1) average
    public void deleteItem(int id) {
        if (inventoryMap.containsKey(id)) {
            System.out.println("\n[Success] Removed item: " + inventoryMap.get(id).name);
            inventoryMap.remove(id);
            refreshLowStockHeap();
        } else {
            System.out.println("\n[Error] Item ID not found.");
        }
    }

    // 4. Display All Inventory - O(N)
    public void displayInventory() {
        if (inventoryMap.isEmpty()) {
            System.out.println("\n[Info] Inventory is currently empty.");
            return;
        }

        System.out.println("\n=============================================================");
        System.out.printf("%-6s %-18s %-15s %-10s %-10s\n", "ID", "Name", "Category", "Qty", "Price");
        System.out.println("=============================================================");
        for (GroceryItem item : inventoryMap.values()) {
            System.out.printf("%-6d %-18s %-15s %-10d ₹%-9.2f\n", 
                item.id, item.name, item.category, item.quantity, item.price);
        }
        System.out.println("=============================================================");
    }

    // 5. Check Low Stock Items using Min-Heap - O(1) to check top
    public void checkLowStockAlert() {
        if (lowStockHeap.isEmpty()) {
            System.out.println("\n[Info] All items have sufficient stock (>10 units).");
            return;
        }

        System.out.println("\n--- Low Stock Alerts (Quantity <= 10) ---");
        // Create a temporary copy to display without altering the original heap
        PriorityQueue<GroceryItem> tempHeap = new PriorityQueue<>(lowStockHeap);
        while (!tempHeap.isEmpty()) {
            GroceryItem item = tempHeap.poll();
            System.out.println("-> " + item.name + " (ID: " + item.id + ") | Left: " + item.quantity + " units");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GroceryInventorySystem store = new GroceryInventorySystem();
        int choice;

        do {
            System.out.println("\n=== Grocery Inventory System (Java DSA) ===");
            System.out.println("1. Add / Update Item");
            System.out.println("2. Search Item by ID");
            System.out.println("3. Delete Item");
            System.out.println("4. Display All Inventory");
            System.out.println("5. View Low Stock Alerts");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Item ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter Quantity: ");
                    int qty = scanner.nextInt();
                    System.out.print("Enter Price (₹): ");
                    double price = scanner.nextDouble();
                    store.addOrUpdateItem(id, name, category, qty, price);
                    break;
                case 2:
                    System.out.print("Enter Item ID to search: ");
                    int searchId = scanner.nextInt();
                    store.searchItem(searchId);
                    break;
                case 3:
                    System.out.print("Enter Item ID to delete: ");
                    int deleteId = scanner.nextInt();
                    store.deleteItem(deleteId);
                    break;
                case 4:
                    store.displayInventory();
                    break;
                case 5:
                    store.checkLowStockAlert();
                    break;
                case 6:
                    System.out.println("\nExiting system. Thank you!");
                    break;
                default:
                    System.out.println("\n[Error] Invalid choice. Try again.");
            }
        } while (choice != 6);

        scanner.close();
    }
}