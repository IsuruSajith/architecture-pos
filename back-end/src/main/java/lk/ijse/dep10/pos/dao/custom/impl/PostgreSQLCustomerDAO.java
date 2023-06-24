package lk.ijse.dep10.pos.dao.custom.impl;

import lk.ijse.dep10.pos.dao.custom.CustomerDAO;
import lk.ijse.dep10.pos.entity.Customer;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class PostgreSQLCustomerDAO implements CustomerDAO {
    @Override
    public long count() throws Exception {
        return 0;
    }

    @Override
    public Customer save(Customer entity) throws Exception {
        return null;
    }

    @Override
    public void update(Customer entity) throws Exception {

    }

    @Override
    public void deleteById(Integer pk) throws Exception {

    }

    @Override
    public Optional<Customer> findById(Integer pk) throws Exception {
        return Optional.empty();
    }

    @Override
    public List<Customer> findAll() throws Exception {
        return null;
    }

    @Override
    public boolean existsById(Integer pk) throws Exception {
        return false;
    }

    @Override
    public void setConnection(Connection connection) {

    }

    @Override
    public List<Customer> findCustomers(String query) throws Exception {
        return null;
    }
}
