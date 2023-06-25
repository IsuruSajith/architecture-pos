package lk.ijse.dep10.pos.dao.util;

import java.util.HashMap;
import java.util.Map;

public class GeneratedKeyHolder implements KeyHolder{

    private final Map<String, Object> keys = new HashMap<>();

    @Override
    public Number getKey() {
        return (Number) keys.get("1");
    }

    @Override
    public Map<String, Object> getKeys() {
        return keys;
    }
}
