package lk.ijse.dep10.pos.business;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BOFactoryTest {

    @Test
    void getInstance() {
        BOFactory instance1 = BOFactory.getInstance();
        BOFactory instance2 = BOFactory.getInstance();
        BOFactory instance3 = BOFactory.getInstance();

        assertEquals(instance1, instance2);
        assertEquals(instance2, instance3);
    }
}