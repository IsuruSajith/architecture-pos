package lk.ijse.dep10.pos.business.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void testBusinessException() {
        assertThrows(BusinessException.class, () -> {
            BusinessException businessException =
                    new BusinessException(BusinessExceptionType.BUSINESS);
            businessException.printStackTrace();
            throw businessException;
        });
    }
}