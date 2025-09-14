package service;

import model.MenuItem;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuService {

    public List<MenuItem> getAllItems() {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new MenuItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price")
                ));
            }
        } catch (Exception e) {
            System.err.println("Fetch menu error: " + e.getMessage());
        }
        return list;
    }

    public boolean addItem(String name, double price) {
        String sql = "INSERT INTO menu_items(name, price) VALUES(?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Add menu item error: " + e.getMessage());
            return false;
        }
    }
}
