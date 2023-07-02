package lk.ijse.dep10.pos.dao.custom.impl;

import lk.ijse.dep10.pos.TestConfig;
import lk.ijse.dep10.pos.dao.DAOFactory;
import lk.ijse.dep10.pos.dao.DAOType;
import lk.ijse.dep10.pos.dao.custom.OrderDetailDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = TestConfig.class)
class OrderDetailDAOImplTest {

    @Autowired
    private EmbeddedDatabase embeddedDatabase;
    private Connection connection;
    private final OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER_DETAIL);

    @BeforeEach
    void setUp() throws SQLException {
        connection = embeddedDatabase.getConnection();
        orderDetailDAO.setConnection(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void existsOrderDetailByItemCode() throws Exception {
       assertTrue(orderDetailDAO.existsOrderDetailByItemCode("456789"));
       assertTrue(orderDetailDAO.existsOrderDetailByItemCode("123456"));
       assertFalse(orderDetailDAO.existsOrderDetailByItemCode("78974541"));
       assertFalse(orderDetailDAO.existsOrderDetailByItemCode("789741fasdf"));
       assertFalse(orderDetailDAO.existsOrderDetailByItemCode("fdafasdf"));
    }
}