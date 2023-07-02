package lk.ijse.dep10.pos.dao.custom;

import lk.ijse.dep10.pos.dao.CrudDAO;
import lk.ijse.dep10.pos.entity.OrderCustomer;

public interface OrderCustomerDAO extends CrudDAO<OrderCustomer, Integer> {

    boolean existsOrderByCustomerId(int customerId) throws Exception;
}
