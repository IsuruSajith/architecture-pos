package lk.ijse.dep10.pos.dao.custom.impl;

import lk.ijse.dep10.pos.dao.custom.OrderCustomerDAO;
import lk.ijse.dep10.pos.dao.util.JdbcTemplate;
import lk.ijse.dep10.pos.entity.OrderCustomer;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static lk.ijse.dep10.pos.dao.util.Mappers.ORDER_CUSTOMER_ROW_MAPPER;

public class OrderCustomerDAOImpl implements OrderCustomerDAO {

    private JdbcTemplate jdbcTemplate;

    public void setConnection(Connection connection) {
        this.jdbcTemplate = new JdbcTemplate(connection);
    }

    public long count() throws Exception {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM order_customer", Long.class);
    }

    public OrderCustomer save(OrderCustomer orderCustomer) throws Exception {
        jdbcTemplate.update("INSERT INTO order_customer (order_id, customer_id) VALUES (?, ?)", orderCustomer.getOrderId(), orderCustomer.getCustomerId());
        return orderCustomer;
    }

    public void update(OrderCustomer orderCustomer) throws Exception {
        jdbcTemplate.update("UPDATE order_customer SET customer_id = ? WHERE order_id=?", orderCustomer.getCustomerId(), orderCustomer.getOrderId());
    }

    public void deleteById(Integer orderId) throws Exception {
        jdbcTemplate.update("DELETE FROM order_customer WHERE order_id = ?", orderId);
    }

    public Optional<OrderCustomer> findById(Integer orderId) throws Exception {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM order_customer WHERE order_id = ?", ORDER_CUSTOMER_ROW_MAPPER, orderId));
    }

    public List<OrderCustomer> findAll() throws Exception {
        return jdbcTemplate.query("SELECT * FROM order_customer", ORDER_CUSTOMER_ROW_MAPPER);
    }

    public boolean existsById(Integer orderId) throws Exception {
        return findById(orderId).isPresent();
    }
}
