package lk.ijse.dep10.pos.dao.custom.impl;

import com.github.javafaker.Faker;
import lk.ijse.dep10.pos.TestConfig;
import lk.ijse.dep10.pos.dao.DAOFactory;
import lk.ijse.dep10.pos.dao.DAOType;
import lk.ijse.dep10.pos.dao.custom.ItemDAO;
import lk.ijse.dep10.pos.entity.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = TestConfig.class)
class ItemDAOImplTest {

    @Autowired
    private EmbeddedDatabase embeddedDatabase;
    private Connection connection;
    private ItemDAO itemDAO;

    @BeforeEach
    void setUp() throws SQLException {
        connection = embeddedDatabase.getConnection();
        itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
        itemDAO.setConnection(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void testItemDAO() throws Exception {
        /* Exercise */
        Faker faker = new Faker();
        Item item = new Item(faker.code().ean8(), faker.book().title(),
                faker.number().numberBetween(5, 8),
                BigDecimal.valueOf(faker.number().randomDouble(2, 2000, 5000)));

        /* Verify */
        assertDoesNotThrow(() -> itemDAO.save(item));
        item.setDescription("Test");
        item.setQty(50);
        item.setUnitPrice(new BigDecimal("200.00"));
        assertDoesNotThrow(() -> itemDAO.update(item));
        Item foundItem = itemDAO.findById(item.getCode()).get();
        assertEquals(item, foundItem);
        assertEquals(itemDAO.findAll().size(), itemDAO.count());
        assertDoesNotThrow(() -> itemDAO.deleteById(item.getCode()));
        assertFalse(itemDAO.existsById(item.getCode()));
    }
}