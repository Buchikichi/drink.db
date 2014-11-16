package to.kit.drink.data.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import to.kit.drink.data.dao.NounDao;
import to.kit.drink.data.dao.OrganizationDao;
import to.kit.drink.data.dto.Noun;
import to.kit.drink.data.dto.Organization;
import to.kit.drink.data.util.MD5;

/**
 * 組織の読み込み.
 * @author H.Sasai
 */
@Component
public final class OrganizationLoader implements Loadable {
	/** Type. */
	private static final String TYPE = Organization.class.getSimpleName();
	/** ファイル名. */
	private static final String RESOURCE = "/organization.txt";
//	/** イメージパス名. */
//	private static final String IMAGE = "/img/organization/";
	@Autowired
	private OrganizationDao organizationDao;
	@Autowired
	private NounDao nounDao;

/*
	private String getImage(String cd) throws IOException {
		String name = IMAGE + cd.toLowerCase() + ".png";
		URL url = OrganizationLoader.class.getResource(name);
		if (url == null) {
			return null;
		}
		Encoder base64 = Base64.getEncoder();
		try (InputStream stream = OrganizationLoader.class
				.getResourceAsStream(name)) {
			byte[] bytes = IOUtils.toByteArray(stream);
			return base64.encodeToString(bytes);
		}
	}
//*/

	private List<Organization> loadResource(List<Noun> nounList)
			throws IOException {
		List<Organization> resultList = new ArrayList<>();
		MD5 md5 = MD5.getInstance();

		try (InputStream stream = OrganizationLoader.class
				.getResourceAsStream(RESOURCE);
				Reader reader = new InputStreamReader(stream);
				BufferedReader in = new BufferedReader(reader)) {
			for (;;) {
				String line = in.readLine();
				if (line == null) {
					break;
				}
				String[] elements = line.split("[\t]");
				String en = elements[1];
				String id = md5.digest(TYPE + en);
				String ja = elements[2];
				Organization rec = new Organization();

				rec.setOrgId(id);
				rec.setCountryCd(elements[0]);
				rec.setNounId(id);
				rec.setReading(elements[3]);
				resultList.add(rec);
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
		}
		return resultList;
	}

	@Override
	public void load() throws IOException, SQLException {
		List<Noun> nounList = new ArrayList<>();
		List<Organization> organizationList = loadResource(nounList);

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
