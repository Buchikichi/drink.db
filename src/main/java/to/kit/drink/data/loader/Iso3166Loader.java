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

import to.kit.drink.data.dao.Iso3166Dao;
import to.kit.drink.data.dao.NounDao;
import to.kit.drink.data.dto.Iso3166;
import to.kit.drink.data.dto.Noun;

/**
 * 国名コードの読み込み.
 * @author H.Sasai
 */
@Component
public final class Iso3166Loader extends Loader<Iso3166> {
	/** イメージパス名. */
	private static final String IMG_PATH = "/img/iso3166";
	@Autowired
	private NounDao nounDao;
	@Autowired
	private Iso3166Dao iso3166Dao;

	@Override
	public void load(Sheet sheet) throws SQLException {
		List<Noun> nounList = new ArrayList<>();
		List<Iso3166> iso3166List = new ArrayList<>();

		for (Map<String, Object> map : loadSheet(sheet)) {
			Iso3166 rec = new Iso3166();
			try {
				BeanUtils.populate(rec, map);
			} catch (IllegalAccessException | InvocationTargetException e) {
				// nop
			}
			String en = (String) map.get("en");
			String id = md5digest(en);
			String ja = (String) map.get("ja");
			String synonym = (String) map.get("synonym");

			rec.setNounId(id);
			rec.setSynonym(synonym);
			rec.setFlag(getImage(IMG_PATH, rec.getCountryCd()));
			iso3166List.add(rec);
			// English
			Noun enNoun = new Noun();
			enNoun.setNounId(id);
			enNoun.setLang("en");
			enNoun.setNoun(en);
			nounList.add(enNoun);
			// Japanese
			Noun jaNoun = new Noun();
			jaNoun.setNounId(id);
			jaNoun.setLang("ja");
			jaNoun.setNoun(ja);
			nounList.add(jaNoun);
		}
		for (Noun rec : nounList) {
			this.nounDao.delete(rec);
			this.nounDao.insert(rec);
		}
		for (Iso3166 rec : iso3166List) {
			this.iso3166Dao.delete(rec);
			this.iso3166Dao.insert(rec);
		}
	}
}
