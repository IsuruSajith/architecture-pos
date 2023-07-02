package lk.ijse.dep10.pos.business.custom;

import lk.ijse.dep10.pos.business.SuperBO;
import lk.ijse.dep10.pos.dto.CustomerDTO;

import java.util.List;

public interface CustomerBO extends SuperBO {

    CustomerDTO saveCustomer(CustomerDTO customerDTO) throws Exception;

    void updateCustomer(CustomerDTO customerDTO) throws Exception;

    void deleteCustomerByCode(int customerId) throws Exception;

    CustomerDTO findCustomerByIdOrContact(String idOrContact) throws Exception;

    List<CustomerDTO> findCustomers(String query) throws Exception;
}
