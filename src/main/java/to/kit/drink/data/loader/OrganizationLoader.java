package to.kit.drink.data.loader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import to.kit.drink.data.dao.NounDao;
import to.kit.drink.data.dao.OrganizationDao;
import to.kit.drink.data.dto.Noun;
import to.kit.drink.data.dto.Organization;

/**
 * 組織の読み込み.
 * @author H.Sasai
 */
@Component
public final class OrganizationLoader extends Loader<Organization> {
	/** Type. */
	private static final String TYPE = Organization.class.getSimpleName();
//	/** イメージパス名. */
//	private static final String IMAGE = "/img/organization/";
	@Autowired
	private OrganizationDao organizationDao;
	@Autowired
	private NounDao nounDao;

	@Override
	public void load(Sheet sheet) throws SQLException {
		List<Noun> nounList = new ArrayList<>();
		List<Organization> organizationList = new ArrayList<>();

		for (Map<String, Object> map : loadSheet(sheet)) {
			Organization rec = new Organization();
			String countryCd = (String) map.get("countryCd");
			String en = (String) map.get("en");
			String ja = (String) map.get("ja");
			String reading = (String) map.get("reading");
			String id = md5digest(TYPE + en);

			rec.setOrgId(id);
			rec.setCountryCd(countryCd);
			rec.setNounId(id);
			rec.setReading(reading);
			organizationList.add(rec);
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
		for (Organization rec : organizationList) {
			this.organizationDao.delete(rec);
			this.organizationDao.insert(rec);
		}
	}
}
