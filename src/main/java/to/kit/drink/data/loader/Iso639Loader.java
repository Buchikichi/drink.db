package to.kit.drink.data.loader;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import to.kit.drink.data.dao.Iso639Dao;
import to.kit.drink.data.dto.Iso639;

/**
 * 言語コードの読み込み.
 * @author H.Sasai
 */
@Component
public final class Iso639Loader extends Loader<Iso639> {
	@Autowired
	private Iso639Dao iso639Dao;

	@Override
	public void load(Sheet sheet) throws SQLException {
		List<Iso639> recList = new ArrayList<>();

		for (Map<String, Object> map : loadSheet(sheet)) {
			Iso639 rec = new Iso639();

			try {
				BeanUtils.populate(rec, map);
			} catch (IllegalAccessException | InvocationTargetException e) {
				// nop
			}
			recList.add(rec);
		}
		for (Iso639 rec : recList) {
			this.iso639Dao.delete(rec);
			this.iso639Dao.insert(rec);
		}
	}
}
