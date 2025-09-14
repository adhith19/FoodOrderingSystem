package service;

import model.CartItem;
import model.MenuItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartService {
    private final List<CartItem> cart = new ArrayList<>();

    public void addToCart(MenuItem item, int qty) {
        for (CartItem ci : cart) {
            if (ci.getItem().getId() == item.getId()) {
                cart.remove(ci);
                cart.add(new CartItem(item, ci.getQuantity() + qty));
                return;
            }
        }
        cart.add(new CartItem(item, qty));
    }

    public void removeFromCart(int itemId) {
        Iterator<CartItem> it = cart.iterator();
        while (it.hasNext()) {
            if (it.next().getItem().getId() == itemId) {
                it.remove();
                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cart);
    }

    public double getTotal() {
        return cart.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public void clear() {
        cart.clear();
    }
}