package lk.ijse.dep10.pos.business.custom.impl;

import lk.ijse.dep10.pos.business.custom.OrderBO;
import lk.ijse.dep10.pos.business.exception.BusinessException;
import lk.ijse.dep10.pos.business.exception.BusinessExceptionType;
import lk.ijse.dep10.pos.business.util.Transformer;
import lk.ijse.dep10.pos.dao.DAOFactory;
import lk.ijse.dep10.pos.dao.DAOType;
import lk.ijse.dep10.pos.dao.custom.*;
import lk.ijse.dep10.pos.dto.ItemDTO;
import lk.ijse.dep10.pos.dto.OrderDTO;
import lk.ijse.dep10.pos.dto.OrderDTO2;
import lk.ijse.dep10.pos.entity.Item;
import lk.ijse.dep10.pos.entity.Order;
import lk.ijse.dep10.pos.entity.OrderCustomer;
import lk.ijse.dep10.pos.entity.OrderDetail;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

public class OrderBOImpl implements OrderBO {

    private final DataSource dataSource;
    private final OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
    private final OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER_DETAIL);
    private final ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
    private final CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
    private final OrderCustomerDAO orderCustomerDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER_CUSTOMER);
    private final QueryDAO queryDAO = DAOFactory.getInstance().getDAO(DAOType.QUERY);
    private final Transformer transformer = new Transformer();

    public OrderBOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Integer placeOrder(OrderDTO orderDTO) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            orderDAO.setConnection(connection);
            customerDAO.setConnection(connection);
            itemDAO.setConnection(connection);
            orderDetailDAO.setConnection(connection);
            orderCustomerDAO.setConnection(connection);

            /* Let's start transaction */
            connection.setAutoCommit(false);

            try {
                /* First of all let's save the order */
                Order order = orderDAO.save(new Order(Timestamp.valueOf(orderDTO.getDateTime())));

                /* Let's find out whether the order is associated with a customer */
                if (orderDTO.getCustomer() != null) {

                    /* If so, then let's find out whether that customer exists in the DB */
                    if (!customerDAO.findById(orderDTO.getCustomer().getId())
                            .map(transformer::fromCustomerEntity)
                            .orElseThrow(() -> new BusinessException(BusinessExceptionType.RECORD_NOT_FOUND,
                                    "Order failed: Customer ID: " + orderDTO.getCustomer().getId() + " does not exist"))
                            .equals(orderDTO.getCustomer()))
                        throw new BusinessException(BusinessExceptionType.INTEGRITY_VIOLATION,
                                "Order failed: Provided customer data does not match");

                    /* Okay everything seems fine with this customer, let's associate customer with the order then */
                    orderCustomerDAO.save(new OrderCustomer(order.getId(), orderDTO.getCustomer().getId()));
                }

                /* Let's save order details */
                for (ItemDTO itemDTO : orderDTO.getItemList()) {

                    /* Let's find out whether the item exists in the database */
                    Item item = itemDAO.findById(itemDTO.getCode()).orElseThrow(() ->
                            new BusinessException(BusinessExceptionType.RECORD_NOT_FOUND,
                                    "Order failed: Item code: " + itemDTO.getCode() + " does not exist"));

                    /* If the item exists, then let's check for the integrity */
                    if (!(item.getDescription().equals(itemDTO.getDescription()) &&
                            item.getUnitPrice().equals(itemDTO.getUnitPrice())))
                        throw new BusinessException(BusinessExceptionType.INTEGRITY_VIOLATION,
                                "Order failed: Provided item data for Item code:" + itemDTO.getCode() + " does not match");

                    /* Okay, then let's find out whether the requested quantity can be satisfied */
                    if (item.getQty() < itemDTO.getQty())
                        throw new BusinessException(BusinessExceptionType.INTEGRITY_VIOLATION,
                                "Order failed: Insufficient stock for the Item code: " + itemDTO.getQty());

                    /* If so, let's save the order detail */
                    OrderDetail orderDetailEntity = transformer.toOrderDetailEntity(itemDTO);
                    orderDetailEntity.getOrderDetailPK().setOrderId(order.getId());
                    orderDetailDAO.save(orderDetailEntity);

                    /* Let's update the stock */
                    item.setQty(item.getQty() - itemDTO.getQty());
                    itemDAO.update(item);
                }

                /* If everything goes well, then let's commit */
                connection.commit();
                return order.getId();
            } catch (Throwable t) {
                /* If something goes bad in between, let's roll back the transaction */
                connection.rollback();

                if (t instanceof BusinessException) throw t;
                else throw new BusinessException(BusinessExceptionType.BUSINESS,
                        "Order failed for some unknown reason", t);
            } finally {
                /* Let's reset the connection */
                connection.setAutoCommit(true);
            }

        }
    }

    @Override
    public List<OrderDTO2> searchOrders(String query) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            queryDAO.setConnection(connection);
            return queryDAO.findOrdersByQuery(query);
        }
    }
}
