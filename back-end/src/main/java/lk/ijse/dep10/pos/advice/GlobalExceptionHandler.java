package lk.ijse.dep10.pos.advice;

import lk.ijse.dep10.pos.business.exception.BusinessException;
import lk.ijse.dep10.pos.business.exception.BusinessExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessExceptions(BusinessException exp){
        log.error(exp.getMessage(), exp);
        Map<String, Object> errorAttributes = null;
        if (exp.getType() == BusinessExceptionType.DUPLICATE_RECORD){
            errorAttributes = getCommonErrorAttributes(HttpStatus.CONFLICT);
        }else if (exp.getType() == BusinessExceptionType.RECORD_NOT_FOUND){
            errorAttributes = getCommonErrorAttributes(HttpStatus.NOT_FOUND);
        } else if (exp.getType() == BusinessExceptionType.INTEGRITY_VIOLATION){
            errorAttributes = getCommonErrorAttributes(HttpStatus.BAD_REQUEST);
        }else {
            errorAttributes = getCommonErrorAttributes(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        errorAttributes.put("message", exp.getMessage());
        return new ResponseEntity<>(errorAttributes,
                HttpStatus.valueOf((Integer) errorAttributes.get("status")));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleDataValidationExceptions(Exception exp){
        log.error("Validation Failure", exp);
        Map<String, Object> errorAttributes = getCommonErrorAttributes(HttpStatus.BAD_REQUEST);

        if (exp instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException mExp = (MethodArgumentNotValidException) exp;
            List<Map<String, Object>> errorList = new ArrayList<>();

            for (FieldError fieldError : mExp.getFieldErrors()) {
                Map<String, Object> errorMap = new LinkedHashMap<>();
                errorMap.put("field", fieldError.getField());
                errorMap.put("message", fieldError.getDefaultMessage());
                errorMap.put("rejected", fieldError.getRejectedValue());
                errorList.add(errorMap);
            }

            errorAttributes.put("message", "Data Validation Failed");
            errorAttributes.put("errors", errorList);
        }else if (exp instanceof ConstraintViolationException){
            ConstraintViolationException cExp = (ConstraintViolationException) exp;

            List<Map<String, Object>> errorList = new ArrayList<>();

            for (ConstraintViolation<?> constraintViolation : cExp.getConstraintViolations()) {
                Map<String, Object> errorMap = new LinkedHashMap<>();
                errorMap.put("message", constraintViolation.getMessage());
                errorMap.put("rejected", constraintViolation.getInvalidValue());
                errorList.add(errorMap);
            }

            errorAttributes.put("message", "Data Validation Failed");
            errorAttributes.put("errors", errorList);
        }else {
            MethodArgumentTypeMismatchException mExp = (MethodArgumentTypeMismatchException) exp;
            errorAttributes.put("message", "Invalid value provided for " + mExp.getName());
        }

        return errorAttributes;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public Map<String, Object> handleExceptions(Throwable t){
        log.error(t.getMessage(), t);
        Map<String, Object> errorAttributes = getCommonErrorAttributes(HttpStatus.INTERNAL_SERVER_ERROR);
        errorAttributes.put("message", t.getMessage());
        return errorAttributes;
    }

    private Map<String, Object> getCommonErrorAttributes(HttpStatus status){
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", LocalDateTime.now().toString());
        errorAttributes.put("status", status.value());
        errorAttributes.put("error", status);
        return errorAttributes;
    }
}
