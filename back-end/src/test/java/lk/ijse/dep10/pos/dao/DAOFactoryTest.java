package lk.ijse.dep10.pos.dao;

import lk.ijse.dep10.pos.dao.custom.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DAOFactoryTest {

    @BeforeEach
    void setUp() {
        System.out.println("Before each test case, this is going to execute");
    }

    @AfterEach
    void tearDown() {
        System.out.println("After each test case, this is going to execute");
    }

    @Test
    void getInstance() {
        /* Exercise */
        DAOFactory instance1 = DAOFactory.getInstance();
        DAOFactory instance2 = DAOFactory.getInstance();

        /* Verify */
        assertEquals(instance1, instance2);
    }

    @Test
    void getDAO() {
        System.out.println(this);
        /* Exercise */
        CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
        ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
        OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
        OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER_DETAIL);
        OrderCustomerDAO orderCustomerDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER_CUSTOMER);
        QueryDAO queryDAO = DAOFactory.getInstance().getDAO(DAOType.QUERY);

        /* Verify */
        assertNotNull(customerDAO);
        assertNotNull(itemDAO);
        assertNotNull(orderDAO);
        assertNotNull(orderDetailDAO);
        assertNotNull(orderCustomerDAO);
        assertNotNull(queryDAO);
    }
}