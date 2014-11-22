package to.kit.drink.data.loader;

import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * Data loader.
 * @author H.Sasai
 */
public interface Loadable {
	void load(Sheet sheet) throws SQLException;
}
