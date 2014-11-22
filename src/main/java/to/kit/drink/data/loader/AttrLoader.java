package to.kit.drink.data.loader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import to.kit.drink.data.dao.AttrDao;
import to.kit.drink.data.dao.NounDao;
import to.kit.drink.data.dto.Attr;
import to.kit.drink.data.dto.Noun;

/**
 * 「種類」の読み込み.
 * @author H.Sasai
 */
@Component
public final class AttrLoader extends Loader<Attr> {
	/** Type. */
	private static final String TYPE = Attr.class.getSimpleName();
	@Autowired
	private AttrDao attrDao;
	@Autowired
	private NounDao nounDao;

	@Override
	public void load(Sheet sheet) throws SQLException {
		List<Noun> nounList = new ArrayList<>();
		List<Attr> attrList= new ArrayList<>();

		for (Map<String, Object> map : loadSheet(sheet)) {
			Attr rec = new Attr();
			String en = (String) map.get("en");
			String ja = (String) map.get("ja");
			int presentation = ((Double) map.get("presentation")).intValue();
			String id = md5digest(TYPE + en);

			rec.setAttrId(id);
			rec.setPresentation(presentation);
			attrList.add(rec);
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
		for (Attr rec : attrList) {
			this.attrDao.delete(rec);
			this.attrDao.insert(rec);
		}
	}
}
