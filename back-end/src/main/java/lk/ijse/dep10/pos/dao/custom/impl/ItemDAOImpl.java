package lk.ijse.dep10.pos.dao.custom.impl;

import lk.ijse.dep10.pos.dao.custom.ItemDAO;
import lk.ijse.dep10.pos.entity.Item;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDAOImpl implements ItemDAO {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public long count() throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT COUNT(*) FROM item");
        ResultSet rst = stm.executeQuery();
        rst.next();
        return rst.getLong(1);
    }

    public Item save(Item item) throws Exception {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO item (code, description, qty, unit_price) VALUES (?, ?, ?, ?)");
        stm.setString(1, item.getCode());
        stm.setString(2, item.getDescription());
        stm.setInt(3, item.getQty());
        stm.setBigDecimal(4, item.getUnitPrice());
        stm.executeUpdate();
        return item;
    }

    public void update(Item item) throws Exception {
        PreparedStatement stm = connection.prepareStatement("UPDATE item SET description=?, unit_price=?, qty=? WHERE code=?");
        stm.setString(1, item.getDescription());
        stm.setBigDecimal(2, item.getUnitPrice());
        stm.setInt(3, item.getQty());
        stm.setString(4, item.getCode());
        stm.executeUpdate();
    }

    public void deleteById(String code) throws Exception {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM item WHERE code=?");
        stm.setString(1, code);
        stm.executeUpdate();
    }

    public Optional<Item> findById(String code) throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM item WHERE code=?");
        stm.setString(1, code);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            String description = rst.getString("description");
            int qty = rst.getInt("qty");
            BigDecimal unitPrice = rst.getBigDecimal("unit_price");
            Item item = new Item(code, description, qty, unitPrice);
            return Optional.of(item);
        }
        return Optional.empty();
    }

    public List<Item> findAll() throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM item");
        ResultSet rst = stm.executeQuery();
        List<Item> itemList = new ArrayList<>();
        while (rst.next()) {
            String code = rst.getString("code");
            String description = rst.getString("description");
            int qty = rst.getInt("qty");
            BigDecimal unitPrice = rst.getBigDecimal("unit_price");
            Item item = new Item(code, description, qty, unitPrice);
            itemList.add(item);
        }
        return itemList;
    }

    public boolean existsById(String code) throws Exception {
        return findById(code).isPresent();
    }
}
