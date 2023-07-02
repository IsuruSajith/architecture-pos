package lk.ijse.dep10.pos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class TestConfig {

    @Bean
    public EmbeddedDatabase embeddedDatabase(){
        EmbeddedDatabaseBuilder edb = new EmbeddedDatabaseBuilder();
        return edb.setType(EmbeddedDatabaseType.H2)
                .addScript("/schema.sql")
                .addScript("/test-data.sql")
                .build();
    }
}
