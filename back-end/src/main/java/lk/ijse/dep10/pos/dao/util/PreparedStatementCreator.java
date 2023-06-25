package lk.ijse.dep10.pos.dao.util;

import java.sql.Connection;
import java.sql.PreparedStatement;

@FunctionalInterface
public interface PreparedStatementCreator {

    PreparedStatement createPreparedStatement(Connection con) throws Exception;
}
