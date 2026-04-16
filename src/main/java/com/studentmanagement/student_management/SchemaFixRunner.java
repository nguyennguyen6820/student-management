package com.studentmanagement.student_management;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SchemaFixRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public SchemaFixRunner(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("====== FIXING FOREIGN KEY CONSTRAINTS ======");
        try {
            // Drop any foreign key constraints on 'student' or 'instructor' that point to the 'users' table
            String dropFkQuery = "DO $$ DECLARE r RECORD; BEGIN " +
                    "FOR r IN (" +
                    "   SELECT tc.table_name, tc.constraint_name " +
                    "   FROM information_schema.table_constraints AS tc " +
                    "   JOIN information_schema.key_column_usage AS kcu " +
                    "     ON tc.constraint_name = kcu.constraint_name " +
                    "     AND tc.table_schema = kcu.table_schema " +
                    "   JOIN information_schema.constraint_column_usage AS ccu " +
                    "     ON ccu.constraint_name = tc.constraint_name " +
                    "     AND ccu.table_schema = tc.table_schema " +
                    "   WHERE tc.constraint_type = 'FOREIGN KEY' " +
                    "     AND ccu.table_name = 'users' " +
                    ") LOOP " +
                    "   EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' DROP CONSTRAINT ' || quote_ident(r.constraint_name); " +
                    "END LOOP; END $$;";
            jdbcTemplate.execute(dropFkQuery);
            System.out.println("Dropped legacy foreign keys pointing to 'users'.");
            
            System.out.println("====== DONE FIXING CONSTRAINTS ======");
        } catch (Exception e) {
            System.err.println("Error dropping schema constraints (might already be dropped): " + e.getMessage());
        }
    }
}
