package service;

import model.CartItem;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class OrderService {

    public boolean placeOrder(List<CartItem> cartItems) {
        String insertOrder = "INSERT INTO orders(total) VALUES(?)";
        String insertItem = "INSERT INTO order_items(order_id, menu_item_id, quantity) VALUES(?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            double total = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
            int orderId;

            try (PreparedStatement ps = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, total);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    orderId = rs.getInt(1);
                }
            }

            try (PreparedStatement psDetail = conn.prepareStatement(insertItem)) {
                for (CartItem ci : cartItems) {
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, ci.getItem().getId());
                    psDetail.setInt(3, ci.getQuantity());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            System.err.println("Place order error: " + e.getMessage());
            return false;
        }
    }
}