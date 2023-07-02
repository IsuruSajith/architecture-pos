package lk.ijse.dep10.pos.business.custom.impl;

import lk.ijse.dep10.pos.business.custom.ItemBO;
import lk.ijse.dep10.pos.business.exception.BusinessException;
import lk.ijse.dep10.pos.business.exception.BusinessExceptionType;
import lk.ijse.dep10.pos.business.util.Transformer;
import lk.ijse.dep10.pos.dao.DAOFactory;
import lk.ijse.dep10.pos.dao.DAOType;
import lk.ijse.dep10.pos.dao.custom.ItemDAO;
import lk.ijse.dep10.pos.dao.custom.OrderDetailDAO;
import lk.ijse.dep10.pos.dto.ItemDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBOImpl implements ItemBO {

    private final DataSource dataSource;
    private final ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
    private final OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER_DETAIL);
    private final Transformer transformer = new Transformer();

    public ItemBOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveItem(ItemDTO itemDTO) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            itemDAO.setConnection(connection);

            if (itemDAO.existsById(itemDTO.getCode())) throw new BusinessException(BusinessExceptionType.DUPLICATE_RECORD,
                    "Save failed: Item code: " + itemDTO.getCode() + " already exists");

            itemDAO.save(transformer.toItemEntity(itemDTO));
        }
    }

    @Override
    public void updateItem(ItemDTO itemDTO) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            itemDAO.setConnection(connection);

            itemDAO.update(transformer.toItemEntity(itemDTO));
        }
    }

    @Override
    public void deleteItemByCode(String itemCode) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            itemDAO.setConnection(connection);
            orderDetailDAO.setConnection(connection);

            if (orderDetailDAO.existsOrderDetailByItemCode(itemCode)) throw new BusinessException(BusinessExceptionType.INTEGRITY_VIOLATION,
                    "Delete failed: Item code: " + itemCode + " already associated with some orders");

            itemDAO.deleteById(itemCode);
        }
    }

    @Override
    public ItemDTO findItemByCode(String itemCode) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            itemDAO.setConnection(connection);

            return itemDAO.findById(itemCode).map(transformer::fromItemEntity).orElseThrow(()-> new BusinessException(BusinessExceptionType.RECORD_NOT_FOUND,
                    "No item record found for the code: " + itemCode));
        }
    }

    @Override
    public List<ItemDTO> findItems(String query) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            itemDAO.setConnection(connection);

            return itemDAO.findAll().stream().map(transformer::fromItemEntity).collect(Collectors.toList());
        }
    }
}
