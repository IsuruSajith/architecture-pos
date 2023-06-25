package lk.ijse.dep10.pos.business;

import lk.ijse.dep10.pos.dao.DAOFactory;
import lk.ijse.dep10.pos.dao.DAOType;
import lk.ijse.dep10.pos.dao.custom.CustomerDAO;
import lk.ijse.dep10.pos.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.dep10.pos.dto.CustomerDTO;
import lk.ijse.dep10.pos.entity.Customer;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;

public class BusinessLogic {

    public static CustomerDTO saveCustomer(CustomerDTO c) throws Exception {
        CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
        Connection connection = null;
        customerDAO.setConnection(connection);
        Customer customerEntity = new Customer(c.getName(), c.getAddress(), c.getContact());
        Customer customer = customerDAO.save(customerEntity);
        c.setId(customer.getId());
        connection.close();
        return c;
    }
}
