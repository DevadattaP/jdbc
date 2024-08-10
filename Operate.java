import java.util.Scanner;

public class Operate {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // Display menu
            System.out.println("Operation Menu:");
            System.out.println("1. Customer");
            System.out.println("2. Cloths");
            System.out.println("3. Work");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> operateCustomer(scanner);
                case 2 -> operateCloths(scanner);
                case 3 -> operateWork(scanner);
                case 4 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        } while (choice != 4);

        scanner.close();
    }

    public static void operateCustomer(Scanner scanner) {
        int choice;
        do {
            // Display customer operation menu
            System.out.println("Customer Operations:");
            System.out.println("1. Show customer list");
            System.out.println("2. Update existing customer");
            System.out.println("3. Delete existing customer");
            System.out.println("4. Add new customer");
            System.out.println("5. Back to main menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> showCustomerList();
                case 2 -> updateCustomer(scanner);
                case 3 -> deleteCustomer(scanner);
                case 4 -> addNewCustomer(scanner);
                case 5 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        } while (choice != 5);
    }

    public static void showCustomerList() {
        // Method to display list of customers
    }

    public static void updateCustomer(Scanner scanner) {
        // Method to update existing customer
    }

    public static void deleteCustomer(Scanner scanner) {
        // Method to delete existing customer
    }

    public static void addNewCustomer(Scanner scanner) {
        // Method to add new customer
    }

    public static void operateCloths(Scanner scanner) {
        int choice;
        do {
            // Display cloth operation menu
            System.out.println("Cloths Operations:");
            System.out.println("1. Show cloth list");
            System.out.println("2. Update existing cloth item");
            System.out.println("3. Delete existing cloth");
            System.out.println("4. Add new cloth");
            System.out.println("5. Back to main menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> showClothList();
                case 2 -> updateCloth(scanner);
                case 3 -> deleteCloth(scanner);
                case 4 -> addNewCloth(scanner);
                case 5 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        } while (choice != 5);
    }

    public static void showClothList() {
        // Method to display list of cloths
    }

    public static void updateCloth(Scanner scanner) {
        // Method to update existing cloth
    }

    public static void deleteCloth(Scanner scanner) {
        // Method to delete existing cloth
    }

    public static void addNewCloth(Scanner scanner) {
        // Method to add new cloth
    }

    public static void operateWork(Scanner scanner) {
        int choice;
        do {
            // Display work operation menu
            System.out.println("Work Operations:");
            System.out.println("1. Show work list");
            System.out.println("2. Update existing work item");
            System.out.println("3. Delete existing work");
            System.out.println("4. Add new work");
            System.out.println("5. Back to main menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> showWorkList();
                case 2 -> updateWork(scanner);
                case 3 -> deleteWork(scanner);
                case 4 -> addNewWork(scanner);
                case 5 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        } while (choice != 5);
    }

    public static void showWorkList() {
        // Method to display list of work items
    }

    public static void updateWork(Scanner scanner) {
        // Method to update existing work item
    }

    public static void deleteWork(Scanner scanner) {
        // Method to delete existing work item
    }

    public static void addNewWork(Scanner scanner) {
        // Method to add new work item
    }
}
