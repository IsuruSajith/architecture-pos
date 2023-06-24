package lk.ijse.dep10.pos.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.dep10.pos.dto.CustomerDTO;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerWSHandler extends TextWebSocketHandler {

    @Autowired
    private BasicDataSource pool;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection
                    .prepareStatement("SELECT * FROM customer WHERE id=? OR contact=?");
            stm.setString(1, message.getPayload().strip());
            stm.setString(2, message.getPayload().strip());
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String contact = resultSet.getString("contact");
                CustomerDTO customer = new CustomerDTO(id, name, address, contact);
                String customerJSON = objectMapper.writeValueAsString(customer);
                session.sendMessage(new TextMessage(customerJSON));
            }
        }
    }
}
