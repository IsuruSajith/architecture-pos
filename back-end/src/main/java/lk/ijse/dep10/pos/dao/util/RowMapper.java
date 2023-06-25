package lk.ijse.dep10.pos.dao.util;

import java.sql.ResultSet;

@FunctionalInterface
public interface RowMapper<T> {

    T mapRow(ResultSet rs, int rowNum) throws Exception;
}
