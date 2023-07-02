package lk.ijse.dep10.pos.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.dep10.pos.business.BOFactory;
import lk.ijse.dep10.pos.business.BOType;
import lk.ijse.dep10.pos.business.custom.CustomerBO;
import lk.ijse.dep10.pos.business.exception.BusinessException;
import lk.ijse.dep10.pos.business.exception.BusinessExceptionType;
import lk.ijse.dep10.pos.dto.CustomerDTO;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class CustomerWSHandler extends TextWebSocketHandler {

    @Autowired
    private BasicDataSource pool;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        CustomerBO customerBO = BOFactory.getInstance().getBO(BOType.CUSTOMER, pool);
        try {
            CustomerDTO customer = customerBO
                    .findCustomerByIdOrContact(message.getPayload().trim());
            String customerJSON = objectMapper.writeValueAsString(customer);
            session.sendMessage(new TextMessage(customerJSON));
        } catch (BusinessException be) {
            if (be.getType() != BusinessExceptionType.RECORD_NOT_FOUND) throw be;
        }
    }
}
