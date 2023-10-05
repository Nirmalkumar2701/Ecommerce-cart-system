import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Product class representing items in the store
class Product {
    private String name;
    private double price;
    private boolean available;

    public Product(String name, double price, boolean available) {
        this.name = name;
        this.price = price;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }
}

// Discount strategy interface
interface DiscountStrategy {
    double applyDiscount(double price, int quantity);
}

// Percentage discount strategy
class PercentageDiscountStrategy implements DiscountStrategy {
    private double percentage;

    public PercentageDiscountStrategy(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double applyDiscount(double price, int quantity) {
        return price * (1 - percentage) * quantity;
    }
}

// Cart item representing a product in the cart
class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemTotal() {
        return product.getPrice() * quantity;
    }
}

// Cart class for managing the user's cart
class Cart {
    private Map<String, CartItem> cartItems = new HashMap<>();
    private DiscountStrategy discountStrategy;

    public Cart(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public void addProduct(Product product, int quantity) {
        if (!product.isAvailable()) {
            System.out.println("Product " + product.getName() + " is not available.");
            return;
        }

        CartItem cartItem = cartItems.getOrDefault(product.getName(), null);

        if (cartItem == null) {
            cartItems.put(product.getName(), new CartItem(product, quantity));
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        System.out.println(quantity + " " + product.getName() + "(s) added to the cart.");
    }

    public void updateQuantity(String productName, int newQuantity) {
        CartItem cartItem = cartItems.getOrDefault(productName, null);

        if (cartItem != null) {
            cartItem.setQuantity(newQuantity);
        }
    }

    public void removeProduct(String productName) {
        cartItems.remove(productName);
        System.out.println(productName + " removed from the cart.");
    }

    public double calculateTotal() {
        double total = 0;

        for (CartItem cartItem : cartItems.values()) {
            total += cartItem.getItemTotal();
        }

        return discountStrategy.applyDiscount(total, cartItems.size());
    }

    public void displayCart() {
        System.out.println("Cart Items:");
        for (CartItem cartItem : cartItems.values()) {
            System.out.println(cartItem.getQuantity() + " " + cartItem.getProduct().getName());
        }

        System.out.println("Total Bill: Your total bill is $" + calculateTotal() + ".");
    }
}

public class ECommerceCartSystem {
    public static void main(String[] args) {
        // Create some products
        Product laptop = new Product("Laptop", 1000, true);
        Product headphones = new Product("Headphones", 50, true);

        // Create a cart with a discount strategy (e.g., 10% off)
        DiscountStrategy discountStrategy = new PercentageDiscountStrategy(0.10);
        Cart cart = new Cart(discountStrategy);

        // Add products to the cart
        cart.addProduct(laptop, 2);
        cart.addProduct(headphones, 1);

        // Display the cart
        cart.displayCart();
    }
}
