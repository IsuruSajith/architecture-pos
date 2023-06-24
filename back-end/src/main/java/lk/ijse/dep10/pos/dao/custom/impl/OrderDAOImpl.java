package lk.ijse.dep10.pos.dao.custom.impl;

import lk.ijse.dep10.pos.dao.custom.OrderDAO;
import lk.ijse.dep10.pos.entity.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public long count() throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT COUNT(*) FROM `order`");
        ResultSet rst = stm.executeQuery();
        rst.next();
        return rst.getLong(1);
    }

    public Order save(Order order) throws Exception {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO `order` (datetime) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        stm.setTimestamp(1, order.getDatetime());
        stm.executeUpdate();
        ResultSet generatedKeys = stm.getGeneratedKeys();
        generatedKeys.next();
        order.setId(generatedKeys.getInt(1));
        return order;
    }

    public void update(Order order) throws Exception {
        PreparedStatement stm = connection.prepareStatement("UPDATE `order` SET datetime=? WHERE id=?");
        stm.setTimestamp(1, order.getDatetime());
        stm.setInt(2, order.getId());
        stm.executeUpdate();
    }

    public void deleteById(Integer orderId) throws Exception {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM `order` WHERE id=?");
        stm.setInt(1, orderId);
        stm.executeUpdate();
    }

    public Optional<Order> findById(Integer orderId) throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM `order` WHERE id=?");
        stm.setInt(1, orderId);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            Timestamp datetime = rst.getTimestamp("datetime");
            Order order = new Order(orderId, datetime);
            return Optional.of(order);
        }
        return Optional.empty();
    }

    public List<Order> findAll() throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM `order`");
        ResultSet rst = stm.executeQuery();
        List<Order> orderList = new ArrayList<>();
        while (rst.next()) {
            int id = rst.getInt("id");
            Timestamp datetime = rst.getTimestamp("datetime");
            orderList.add(new Order(id, datetime));
        }
        return orderList;
    }

    public boolean existsById(Integer orderId) throws Exception {
        return findById(orderId).isPresent();
    }

}
