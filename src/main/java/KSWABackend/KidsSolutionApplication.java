package KSWABackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import java.sql.*;

@SpringBootApplication

/**
 * The class  GPS application application
 */
public class KidsSolutionApplication extends SpringBootServletInitializer {
	/**
	 *
	 * Main
	 *
	 * @param args  the args
	 */
	public static void main(String[] args) {
		SpringApplication.run(KidsSolutionApplication.class, args);
		String url = "jdbc:mysql://localhost:3307/kidssolution";
		String user = "root";
		String pass = "";

		try {
			Connection con = DriverManager.getConnection(url, user, pass);
			Statement statement = con.createStatement();
			String querychildren = "SELECT * FROM children";
			ResultSet resultSet = statement.executeQuery(querychildren);

			while (resultSet.next()) {
				System.out.println(resultSet.getString(1) + " " + resultSet.getString(2)
						+ " " + resultSet.getString(3) + " " + resultSet.getString(4));
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Bean
	/**
	 *
	 * Task scheduler
	 *
	 * @return TaskScheduler
	 */
	public TaskScheduler taskScheduler() {
		return new ThreadPoolTaskScheduler();
	}
}
