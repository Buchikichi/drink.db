package to.kit.drink.data.loader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import to.kit.drink.data.dao.NounDao;
import to.kit.drink.data.dao.TagsDao;
import to.kit.drink.data.dto.Noun;
import to.kit.drink.data.dto.Tags;

/**
 * タグの読み込み.
 * @author H.Sasai
 */
@Component
public final class TagsLoader extends Loader<Tags> {
	/** Type. */
	private static final String TYPE = Tags.class.getSimpleName();
	@Autowired
	private TagsDao tagsDao;
	@Autowired
	private NounDao nounDao;

	@Override
	public void load(Sheet sheet) throws SQLException {
		List<Tags> tagsList = new ArrayList<>();
		List<Noun> nounList = new ArrayList<>();

		for (Map<String, Object> map : loadSheet(sheet)) {
			String en = (String) map.get("en");
			String ja = (String) map.get("ja");
			String id = md5digest(TYPE + en);
			String kindId = (String) map.get("kindId");
			Tags rec = new Tags();

			rec.setTagId(id);
			rec.setKindId(kindId);
			tagsList.add(rec);
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
		for (Tags rec : tagsList) {
			this.tagsDao.delete(rec);
			this.tagsDao.insert(rec);
		}
	}
}
