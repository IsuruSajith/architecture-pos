package lk.ijse.dep10.pos.dao.custom;

import lk.ijse.dep10.pos.dao.CrudDAO;
import lk.ijse.dep10.pos.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO extends CrudDAO<Customer, Integer> {

    List<Customer> findCustomers(String query) throws Exception;

    Optional<Customer> findCustomerByIdOrContact(String idOrContact) throws Exception;

    default boolean existsCustomerByContact(String contact) throws Exception {
        throw new IllegalStateException("Method is yet to be implemented");
    }

    boolean existsCustomerByContactAndNotId(String contact, Integer id) throws Exception;
}
