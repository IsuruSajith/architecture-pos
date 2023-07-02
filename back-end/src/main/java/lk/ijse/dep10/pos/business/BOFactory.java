package lk.ijse.dep10.pos.business;

import lk.ijse.dep10.pos.business.custom.impl.CustomerBOImpl;
import lk.ijse.dep10.pos.business.custom.impl.ItemBOImpl;
import lk.ijse.dep10.pos.business.custom.impl.OrderBOImpl;

import javax.sql.DataSource;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        return (boFactory == null) ? (boFactory = new BOFactory()) : boFactory;
    }

    public <T extends SuperBO> T getBO(BOType boType, DataSource dataSource) {
        switch (boType) {
            case CUSTOMER:
                return (T) new CustomerBOImpl(dataSource);
            case ITEM:
                return (T) new ItemBOImpl(dataSource);
            case ORDER:
                return (T) new OrderBOImpl(dataSource);
            default:
                throw new RuntimeException("Invalid BOType");
        }
    }
}
