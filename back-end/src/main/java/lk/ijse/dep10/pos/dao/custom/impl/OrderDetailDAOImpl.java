package lk.ijse.dep10.pos.dao.custom.impl;

import lk.ijse.dep10.pos.dao.custom.OrderDetailDAO;
import lk.ijse.dep10.pos.dao.util.JdbcTemplate;
import lk.ijse.dep10.pos.entity.OrderDetail;
import lk.ijse.dep10.pos.entity.OrderDetailPK;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static lk.ijse.dep10.pos.dao.util.Mappers.ORDER_DETAIL_ROW_MAPPER;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    private JdbcTemplate jdbcTemplate;

    public void setConnection(Connection connection) {
        jdbcTemplate = new JdbcTemplate(connection);
    }

    public long count() throws Exception {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM order_detail", Long.class);
    }

    public OrderDetail save(OrderDetail orderDetail) throws Exception {
        jdbcTemplate.update("INSERT INTO order_detail (order_id, item_code, unit_price, qty) VALUES (?, ?, ?, ?)", orderDetail.getOrderDetailPK().getOrderId(), orderDetail.getOrderDetailPK().getItemCode(), orderDetail.getUnitPrice(), orderDetail.getQty());
        return orderDetail;
    }

    public void update(OrderDetail orderDetail) throws Exception {
        jdbcTemplate.update("UPDATE order_detail SET unit_price=?, qty=? WHERE order_id=? AND item_code=?", orderDetail.getUnitPrice(), orderDetail.getQty(), orderDetail.getOrderDetailPK().getOrderId(), orderDetail.getOrderDetailPK().getItemCode());
    }

    public void deleteById(OrderDetailPK orderDetailPK) throws Exception {
        jdbcTemplate.update("DELETE FROM order_detail WHERE order_id = ? AND item_code = ?", orderDetailPK.getOrderId(), orderDetailPK.getItemCode());
    }

    public Optional<OrderDetail> findById(OrderDetailPK orderDetailPK) throws Exception {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM order_detail WHERE order_id=? AND item_code = ?", ORDER_DETAIL_ROW_MAPPER, orderDetailPK.getOrderId(), orderDetailPK.getItemCode()));
    }

    public List<OrderDetail> findAll() throws Exception {
        return jdbcTemplate.query("SELECT * FROM order_detail", ORDER_DETAIL_ROW_MAPPER);
    }

    public boolean existsById(OrderDetailPK orderDetailPK) throws Exception {
        return findById(orderDetailPK).isPresent();
    }

    @Override
    public boolean existsOrderDetailByItemCode(String itemCode) throws Exception {
        return jdbcTemplate.queryForObject("SELECT * FROM order_detail WHERE item_code =?",
                ORDER_DETAIL_ROW_MAPPER, itemCode) != null;
    }
}
