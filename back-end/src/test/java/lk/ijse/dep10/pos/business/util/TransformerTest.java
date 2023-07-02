package lk.ijse.dep10.pos.business.util;

import lk.ijse.dep10.pos.dto.CustomerDTO;
import lk.ijse.dep10.pos.dto.ItemDTO;
import lk.ijse.dep10.pos.entity.Customer;
import lk.ijse.dep10.pos.entity.Item;
import lk.ijse.dep10.pos.entity.OrderDetail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransformerTest {

    private final Transformer transformer = new Transformer();

    @NullSource
    @ValueSource(ints = {1, 2, 5, 10})
    @ParameterizedTest
    void toCustomerEntity(Integer id) {
        CustomerDTO customerDTO = new CustomerDTO(id, "Kasun", "Galle", "078-1234567");
        Customer customerEntity = transformer.toCustomerEntity(customerDTO);
        System.out.println(customerEntity);

        if (customerDTO.getId() == null){
            assertEquals(0, customerEntity.getId());
        }else{
            assertEquals(customerDTO.getId(), customerEntity.getId());
        }
        assertEquals(customerDTO.getName(), customerEntity.getName());
        assertEquals(customerDTO.getAddress(), customerEntity.getAddress());
        assertEquals(customerDTO.getContact(), customerEntity.getContact());
    }

    @Test
    void fromCustomerEntity() {
        Customer customerEntity = new Customer(1, "Kasun", "Galle", "078-1234567");
        CustomerDTO customerDTO = transformer.fromCustomerEntity(customerEntity);

        assertEquals(customerEntity.getId(), customerDTO.getId());
        assertEquals(customerEntity.getName(), customerDTO.getName());
        assertEquals(customerEntity.getAddress(), customerDTO.getAddress());
        assertEquals(customerEntity.getContact(), customerDTO.getContact());
    }

    @Test
    void toItemEntity() {
        ItemDTO itemDTO = new ItemDTO("123444", "Atlas Black Pen", 5, new BigDecimal("250.00"));
        Item itemEntity = transformer.toItemEntity(itemDTO);

        assertEquals(itemDTO.getCode(), itemEntity.getCode());
        assertEquals(itemDTO.getDescription(), itemEntity.getDescription());
        assertEquals(itemDTO.getQty(), itemEntity.getQty());
        assertEquals(itemDTO.getUnitPrice(), itemEntity.getUnitPrice());
    }

    @Test
    void fromItemEntity() {
        Item itemEntity = new Item("123444", "Atlas Black Pen", 5, new BigDecimal("250.00"));
        ItemDTO itemDTO = transformer.fromItemEntity(itemEntity);

        assertEquals(itemDTO.getCode(), itemEntity.getCode());
        assertEquals(itemDTO.getDescription(), itemEntity.getDescription());
        assertEquals(itemDTO.getQty(), itemEntity.getQty());
        assertEquals(itemDTO.getUnitPrice(), itemEntity.getUnitPrice());
    }

    @Test
    void toOrderDetailEntity() {
        ItemDTO itemDTO = new ItemDTO("123456", "Atlas Black Pen",
                20, new BigDecimal("200.00"));

        OrderDetail orderDetailEntity = transformer.toOrderDetailEntity(itemDTO);

        assertEquals(orderDetailEntity.getUnitPrice(), itemDTO.getUnitPrice());
        assertEquals(orderDetailEntity.getQty(), itemDTO.getQty());
        assertEquals(orderDetailEntity.getOrderDetailPK().getItemCode(), itemDTO.getCode());
    }
}