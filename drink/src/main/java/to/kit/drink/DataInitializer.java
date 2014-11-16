package to.kit.drink;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import to.kit.drink.data.loader.AttrLoader;
import to.kit.drink.data.loader.Iso3166Loader;
import to.kit.drink.data.loader.Iso639Loader;
import to.kit.drink.data.loader.KindLoader;
import to.kit.drink.data.loader.OrganizationLoader;
import to.kit.drink.data.loader.TagsLoader;

/**
 * DataInitializer.
 * @author H.Sasai
 */
@Configuration
@ComponentScan
@Component
public class DataInitializer {
	@Autowired
	private Iso639Loader iso639Loader;
	@Autowired
	private Iso3166Loader iso3166Loader;
	@Autowired
	private KindLoader kindLoader;
	@Autowired
	private AttrLoader attrLoader;
	@Autowired
	private TagsLoader tagsLoader;
	@Autowired
	private OrganizationLoader organizationLoader;

	public void execute() throws SQLException, IOException {
		this.iso639Loader.load();
		this.iso3166Loader.load();
		this.kindLoader.load();
		this.attrLoader.load();
		this.tagsLoader.load();
		this.organizationLoader.load();
	}

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(
				DataInitializer.class);
		DataInitializer app = context.getBean(DataInitializer.class);

		app.execute();
	}
}
