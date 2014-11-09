package to.kit.drink;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import to.kit.drink.data.loader.Iso3166Loader;

/**
 * DataInitializer.
 * @author H.Sasai
 */
@Configuration
@ComponentScan
@Component
public class DataInitializer {
	@Autowired
	private Iso3166Loader loader;

	public void execute() throws SQLException, IOException {
		this.loader.load();
	}

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(
				DataInitializer.class);
		DataInitializer app = context.getBean(DataInitializer.class);

		app.execute();
	}
}
