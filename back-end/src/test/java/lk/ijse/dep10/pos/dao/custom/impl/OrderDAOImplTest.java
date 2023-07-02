package lk.ijse.dep10.pos.dao.custom.impl;

import lk.ijse.dep10.pos.TestConfig;
import lk.ijse.dep10.pos.dao.DAOFactory;
import lk.ijse.dep10.pos.dao.DAOType;
import lk.ijse.dep10.pos.dao.custom.OrderDAO;
import lk.ijse.dep10.pos.entity.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class OrderDAOImplTest {

    private final EmbeddedDatabase embeddedDatabase;
    private Connection connection;
    private OrderDAO orderDAO;

    @Autowired
    public OrderDAOImplTest(EmbeddedDatabase embeddedDatabase) {
        this.embeddedDatabase = embeddedDatabase;
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = embeddedDatabase.getConnection();
        orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
        orderDAO.setConnection(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void orderDAOTest() throws Exception {
        Order order = new Order(Timestamp.valueOf(LocalDateTime.now()));
        Order savedOrder = orderDAO.save(order);

        assertNotEquals(0, savedOrder.getId());
        assertTrue(orderDAO.existsById(savedOrder.getId()));

        System.out.println(savedOrder.getDatetime());
        LocalDateTime datetime = LocalDateTime.of(2000, Month.APRIL, 20, 8, 30);
        savedOrder.setDatetime(Timestamp.valueOf(datetime));
        assertDoesNotThrow(() -> orderDAO.update(savedOrder));

        Order foundOrder = orderDAO.findById(savedOrder.getId()).get();
        System.out.println(foundOrder.getDatetime());
        assertEquals(savedOrder, foundOrder);

        long count = orderDAO.count();
        assertTrue(count > 0);
        assertEquals(count, orderDAO.findAll().size());

        assertDoesNotThrow(()->orderDAO.deleteById(foundOrder.getId()));
        assertFalse(orderDAO.existsById(foundOrder.getId()));
    }
}