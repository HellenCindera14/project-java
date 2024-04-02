// package bdki.project.services;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.jdbc.core.JdbcTemplate;

// public class CreateSchemaService {

//     @Autowired
//     private final JdbcTemplate jdbcTemplate;

//     public CreateSchemaService(JdbcTemplate jdbcTemplate) {
//         this.jdbcTemplate = jdbcTemplate;
//     }

//     public void createSchema(List<String> schemaName) {
//         for(int i = 0; i < schemaName.size() ; i++){
//             String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName.get(i);
//             jdbcTemplate.execute(sql);
//         }
//     }
    
// }
