package lk.ijse.dep10.pos.dao.custom.impl;

import lk.ijse.dep10.pos.dao.custom.CustomerDAO;
import lk.ijse.dep10.pos.dao.util.GeneratedKeyHolder;
import lk.ijse.dep10.pos.dao.util.JdbcTemplate;
import lk.ijse.dep10.pos.dao.util.KeyHolder;
import lk.ijse.dep10.pos.entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static lk.ijse.dep10.pos.dao.util.Mappers.CUSTOMER_ROW_MAPPER;

public class CustomerDAOImpl implements CustomerDAO {

    private JdbcTemplate jdbcTemplate;

    public void setConnection(Connection connection) {
        jdbcTemplate = new JdbcTemplate(connection);
    }

    @Override
    public long count() throws Exception {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customer", Long.class);
    }

    @Override
    public Customer save(Customer customer) throws Exception {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stm = con.prepareStatement("INSERT INTO customer (name, address, contact) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, customer.getName());
            stm.setString(2, customer.getAddress());
            stm.setString(3, customer.getContact());
            return stm;
        }, keyHolder);
        customer.setId(keyHolder.getKey().intValue());
        return customer;
    }

    @Override
    public void update(Customer customer) throws Exception {
        jdbcTemplate.update("UPDATE customer SET name=?, address=?, contact=? WHERE id=?", customer.getName(), customer.getAddress(), customer.getContact(), customer.getId());
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        jdbcTemplate.update("DELETE FROM customer WHERE id=?", id);
    }

    public Optional<Customer> findById(Integer id) throws Exception {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM customer WHERE id=?", CUSTOMER_ROW_MAPPER, id));
    }

    public List<Customer> findAll() throws Exception {
        return jdbcTemplate.query("SELECT * FROM customer", CUSTOMER_ROW_MAPPER);
    }

    public boolean existsById(Integer id) throws Exception {
        return findById(id).isPresent();
    }

    public List<Customer> findCustomers(String query) throws Exception {
        query = "%" + query + "%";
        return jdbcTemplate.query("SELECT * FROM customer WHERE id LIKE ? OR name LIKE ? OR address LIKE ? OR contact LIKE ?", CUSTOMER_ROW_MAPPER,
                query, query, query, query);
    }

    @Override
    public boolean existsCustomerByContact(String contact) throws Exception {
        return jdbcTemplate.queryForObject("SELECT * FROM customer WHERE contact=?",
                CUSTOMER_ROW_MAPPER, contact) != null;
    }

    @Override
    public boolean existsCustomerByContactAndNotId(String contact, Integer id) throws Exception {
        return jdbcTemplate.queryForObject("SELECT * FROM customer WHERE contact = ? AND id <> ?",
                CUSTOMER_ROW_MAPPER, contact, id) != null;
    }

    @Override
    public Optional<Customer> findCustomerByIdOrContact(String idOrContact) throws Exception {
        return Optional.ofNullable(jdbcTemplate
                .queryForObject("SELECT * FROM customer WHERE id=? OR contact=?",
                CUSTOMER_ROW_MAPPER, idOrContact, idOrContact));
    }
}
