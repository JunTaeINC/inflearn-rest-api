package hello.study.restapi.event;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class DatabaseTest {

	@Autowired
	private ApplicationContext context;

	@Test
	public void printDatabaseInfo() {
		HikariDataSource dataSource = context.getBean(HikariDataSource.class);
		System.out.println("DataSource: " + dataSource);
		System.out.println("JDBC URL: " + dataSource.getJdbcUrl());
	}
}