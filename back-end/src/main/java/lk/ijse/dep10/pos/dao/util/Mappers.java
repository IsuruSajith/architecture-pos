package lk.ijse.dep10.pos.dao.util;

import lk.ijse.dep10.pos.dto.OrderDTO2;
import lk.ijse.dep10.pos.entity.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Mappers {
    public static final RowMapper<Customer> CUSTOMER_ROW_MAPPER = ((rs, rowNum) -> {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String address = rs.getString("address");
        String contact = rs.getString("contact");
        return new Customer(id, name, address, contact);
    });

    public static final RowMapper<Item> ITEM_ROW_MAPPER = (rs, rowNum) -> {
        String code = rs.getString("code");
        String description = rs.getString("description");
        int qty = rs.getInt("qty");
        BigDecimal unitPrice = rs.getBigDecimal("unit_price");
        return new Item(code, description, qty, unitPrice);
    };

    public static final RowMapper<OrderCustomer> ORDER_CUSTOMER_ROW_MAPPER = (rs, rowNum) -> {
        int orderId = rs.getInt("order_id");
        int customerId = rs.getInt("customer_id");
        return new OrderCustomer(orderId, customerId);
    };

    public static final RowMapper<Order> ORDER_ROW_MAPPER = (rs, rowNum) -> {
        int id = rs.getInt("id");
        Timestamp datetime = rs.getTimestamp("datetime");
        return new Order(id, datetime);
    };

    public static final RowMapper<OrderDetail> ORDER_DETAIL_ROW_MAPPER = (rs, rowNum) -> {
        int orderId = rs.getInt("order_id");
        String itemCode = rs.getString("item_code");
        BigDecimal unitPrice = rs.getBigDecimal("unit_price");
        int qty = rs.getInt("qty");
        return new OrderDetail(orderId, itemCode, unitPrice, qty);
    };

    public static final RowMapper<OrderDTO2> ORDER_DTO_2_ROW_MAPPER = (rst, rowNum) -> {
        String orderId = rst.getString("id");
        LocalDateTime orderDateTime = rst.getTimestamp("datetime").toLocalDateTime();
        String customerId = rst.getString("customer_id");
        String customerName = rst.getString("name");
        BigDecimal orderTotal = rst.getBigDecimal("total");
        return new OrderDTO2(orderId, orderDateTime, customerId, customerName, orderTotal);
    };
}
