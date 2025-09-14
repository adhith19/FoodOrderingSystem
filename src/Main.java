import model.CartItem;
import model.MenuItem;
import service.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final OwnerService ownerSvc = new OwnerService();
    private static final MenuService menuSvc = new MenuService();
    private static final CartService cartSvc = new CartService();
    private static final OrderService orderSvc = new OrderService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Owner Login  2. Order Food  3. Exit");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": handleOwner(); break;
                case "2": handleCustomer(); break;
                case "3": System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void handleOwner() {
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();
        if (!ownerSvc.authenticate(user, pass)) {
            System.out.println("Auth failed.");
            return;
        }
        System.out.println("Welcome, owner!");
        System.out.print("Item name: ");
        String name = scanner.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        if (menuSvc.addItem(name, price)) {
            System.out.println("Menu updated.");
        } else {
            System.out.println("Update failed.");
        }
    }

    private static void handleCustomer() {
        while (true) {
            System.out.println("\n1. View Menu  2. Add to Cart  3. View Cart");
            System.out.println("4. Remove from Cart  5. Place Order  6. Back");
            String c = scanner.nextLine();
            switch (c) {
                case "1": showMenu(); break;
                case "2": addToCart(); break;
                case "3": showCart(); break;
                case "4": removeFromCart(); break;
                case "5": placeOrder(); break;
                case "6": return;
                default: System.out.println("Invalid.");
            }
        }
    }

    private static void showMenu() {
        List<MenuItem> list = menuSvc.getAllItems();
        list.forEach(System.out::println);
    }

    private static void addToCart() {
        System.out.print("Menu ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Qty: ");
        int q = Integer.parseInt(scanner.nextLine());
        menuSvc.getAllItems().stream()
            .filter(i -> i.getId() == id)
            .findFirst()
            .ifPresent(item -> {
                cartSvc.addToCart(item, q);
                System.out.println("Added.");
            });
    }

    private static void showCart() {
        List<CartItem> items = cartSvc.getCartItems();
        items.forEach(System.out::println);
        System.out.println("Total: ₹" + cartSvc.getTotal());
    }

    private static void removeFromCart() {
        System.out.print("Remove ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        cartSvc.removeFromCart(id);
        System.out.println("Removed if existed.");
    }

    private static void placeOrder() {
        List<CartItem> items = cartSvc.getCartItems();
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        if (orderSvc.placeOrder(items)) {
            System.out.println("Order placed! Total: ₹" + cartSvc.getTotal());
            cartSvc.clear();
        } else {
            System.out.println("Order failed.");
        }
    }
}