package lk.ijse.dep10.pos.dao.custom;

import lk.ijse.dep10.pos.dao.SuperDAO;
import lk.ijse.dep10.pos.dto.OrderDTO2;

import java.util.List;

public interface QueryDAO extends SuperDAO {

    List<OrderDTO2> findOrdersByQuery(String query) throws Exception;
}
