package lk.ijse.dep10.pos.business.custom.impl;

import com.github.javafaker.Faker;
import lk.ijse.dep10.pos.TestConfig;
import lk.ijse.dep10.pos.business.BOFactory;
import lk.ijse.dep10.pos.business.BOType;
import lk.ijse.dep10.pos.business.custom.ItemBO;
import lk.ijse.dep10.pos.business.exception.BusinessException;
import lk.ijse.dep10.pos.dao.DAOFactory;
import lk.ijse.dep10.pos.dto.ItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = TestConfig.class)
class ItemBOImplTest {

    @Autowired
    private EmbeddedDatabase embeddedDatabase;
    private ItemBO itemBO;

    @BeforeEach
    void setUp() {
        itemBO = BOFactory.getInstance().getBO(BOType.ITEM, embeddedDatabase);
    }

    @Test
    void saveItem() throws Exception {
        Faker faker = new Faker();
        ItemDTO itemDTO = new ItemDTO(faker.code().ean13(),
                faker.book().title(), faker.number().numberBetween(10, 20),
                BigDecimal.valueOf(faker.number()
                        .randomDouble(2, 200, 500)));

        assertDoesNotThrow(()->itemBO.saveItem(itemDTO));
        assertThrows(BusinessException.class, ()->itemBO.saveItem(itemDTO));
    }
}