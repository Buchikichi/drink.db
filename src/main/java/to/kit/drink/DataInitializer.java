package to.kit.drink;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.SQLException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import to.kit.drink.data.loader.AttrLoader;
import to.kit.drink.data.loader.Iso3166Loader;
import to.kit.drink.data.loader.Iso639Loader;
import to.kit.drink.data.loader.ItemLoader;
import to.kit.drink.data.loader.KindLoader;
import to.kit.drink.data.loader.Loadable;
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
	/** ファイル名. */
	private static final String RESOURCE = "/drink.kit.to.xlsx";

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
	@Autowired
	private ItemLoader itemLoader;

	private Loadable selectLoader(String name) {
		Loadable result = null;
		String[] names = name.split("#");
		String fieldName = names[0] + "Loader";

		try {
			Field field = DataInitializer.class.getDeclaredField(fieldName);
			result = (Loadable) field.get(this);
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			// nop
		}
		return result;
	}

	public void execute() throws IOException, InvalidFormatException,
			SQLException {
		try (InputStream in = DataInitializer.class
				.getResourceAsStream(RESOURCE)) {
			Workbook wb = WorkbookFactory.create(in);
			int numberOfSheets = wb.getNumberOfSheets();

			for (int index = 0; index < numberOfSheets; index++) {
				Sheet sheet = wb.getSheetAt(index);
				String name = sheet.getSheetName();
				Loadable loader = selectLoader(name);

				if (loader != null) {
					loader.load(sheet);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(
				DataInitializer.class);
		DataInitializer app = context.getBean(DataInitializer.class);

		app.execute();
	}
}
