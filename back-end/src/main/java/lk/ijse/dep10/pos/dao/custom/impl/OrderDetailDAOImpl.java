package lk.ijse.dep10.pos.dao.custom.impl;

import lk.ijse.dep10.pos.dao.custom.OrderDetailDAO;
import lk.ijse.dep10.pos.entity.OrderDetail;
import lk.ijse.dep10.pos.entity.OrderDetailPK;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public long count() throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT COUNT(*) FROM order_detail");
        ResultSet rst = stm.executeQuery();
        rst.next();
        return rst.getLong(1);
    }

    public OrderDetail save(OrderDetail orderDetail) throws Exception {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO order_detail (order_id, item_code, unit_price, qty) VALUES (?, ?, ?, ?)");
        stm.setInt(1, orderDetail.getOrderDetailPK().getOrderId());
        stm.setString(2, orderDetail.getOrderDetailPK().getItemCode());
        stm.setBigDecimal(3, orderDetail.getUnitPrice());
        stm.setInt(4, orderDetail.getQty());
        stm.executeUpdate();
        return orderDetail;
    }

    public void update(OrderDetail orderDetail) throws Exception {
        PreparedStatement stm = connection.prepareStatement("UPDATE order_detail SET unit_price=?, qty=? WHERE order_id=? AND item_code=?");
        stm.setBigDecimal(1, orderDetail.getUnitPrice());
        stm.setInt(2, orderDetail.getQty());
        stm.setInt(3, orderDetail.getOrderDetailPK().getOrderId());
        stm.setString(4, orderDetail.getOrderDetailPK().getItemCode());
        stm.executeUpdate();
    }

    public void deleteById(OrderDetailPK orderDetailPK) throws Exception {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM order_detail WHERE order_id = ? AND item_code = ?");
        stm.setInt(1, orderDetailPK.getOrderId());
        stm.setString(2, orderDetailPK.getItemCode());
        stm.executeUpdate();
    }

    public Optional<OrderDetail> findById(OrderDetailPK orderDetailPK) throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM order_detail WHERE order_id=? AND item_code = ?");
        stm.setInt(1, orderDetailPK.getOrderId());
        stm.setString(2, orderDetailPK.getItemCode());
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            BigDecimal unitPrice = rst.getBigDecimal("unit_price");
            int qty = rst.getInt("qty");
            OrderDetail orderDetail = new OrderDetail(orderDetailPK, unitPrice, qty);
            return Optional.of(orderDetail);
        }
        return Optional.empty();
    }

    public List<OrderDetail> findAll() throws Exception {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM order_detail");
        ResultSet rst = stm.executeQuery();
        List<OrderDetail> orderDetailList = new ArrayList<>();
        while (rst.next()) {
            int orderId = rst.getInt("order_id");
            String itemCode = rst.getString("item_code");
            BigDecimal unitPrice = rst.getBigDecimal("unit_price");
            int qty = rst.getInt("qty");
            OrderDetail orderDetail = new OrderDetail(orderId, itemCode, unitPrice, qty);
            orderDetailList.add(orderDetail);
        }
        return orderDetailList;
    }

    public boolean existsById(OrderDetailPK orderDetailPK) throws Exception {
        return findById(orderDetailPK).isPresent();
    }
}
