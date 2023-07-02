package lk.ijse.dep10.pos.dao.custom;

import lk.ijse.dep10.pos.dao.CrudDAO;
import lk.ijse.dep10.pos.entity.Item;

import java.util.List;

public interface ItemDAO extends CrudDAO<Item, String> {

    List<Item> findItems(String query) throws Exception;
}
