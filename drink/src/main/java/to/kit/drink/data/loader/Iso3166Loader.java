package to.kit.drink.data.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import to.kit.drink.data.dao.Iso3166Dao;
import to.kit.drink.data.dao.NounDao;
import to.kit.drink.data.dto.Iso3166;
import to.kit.drink.data.dto.Noun;
import to.kit.drink.data.util.MD5;

/**
 * 国名コードの読み込み.
 * @author H.Sasai
 */
@Component
public final class Iso3166Loader {
	/** ファイル名. */
	private static final String RESOURCE = "/iso3166.txt";
	/** イメージパス名. */
	private static final String IMAGE = "/img/iso3166/";
	@Autowired
	private NounDao nounDao;
	@Autowired
	private Iso3166Dao iso3166Dao;

	private String getImage(String cd) throws IOException {
		String name = IMAGE + cd.toLowerCase() + ".png";
		URL url = Iso3166.class.getResource(name);
		if (url == null) {
			return null;
		}
		Encoder base64 = Base64.getEncoder();
		try (InputStream stream = Iso3166.class.getResourceAsStream(name)) {
			byte[] bytes = IOUtils.toByteArray(stream);
			return base64.encodeToString(bytes);
		}
	}

	private List<Iso3166> loadResource(List<Noun> nounList) throws IOException {
		List<Iso3166> resultList = new ArrayList<>();
		MD5 md5 = MD5.getInstance();

		try (InputStream stream = Iso3166.class.getResourceAsStream(RESOURCE);
				Reader reader = new InputStreamReader(stream);
				BufferedReader in = new BufferedReader(reader)) {
			for (;;) {
				String line = in.readLine();
				if (line == null) {
					break;
				}
				String[] elements = line.split("[\t]");
				String cd = elements[0];
				String en = elements[1];
				String id = md5.digest(en);
				String ja = elements[2];
				Iso3166 rec = new Iso3166();

				rec.setCountryCd(cd);
				rec.setNounId(id);
				rec.setSynonym(elements[3]);
				rec.setFlag(getImage(cd));
				resultList.add(rec);
				// USA
				Noun enNoun = new Noun();
				enNoun.setNounId(id);
				enNoun.setCountryCd("USA");
				enNoun.setNoun(en);
				nounList.add(enNoun);
				// JPN
				Noun jaNoun = new Noun();
				jaNoun.setNounId(id);
				jaNoun.setCountryCd("JPN");
				jaNoun.setNoun(ja);
				nounList.add(jaNoun);
			}
		}
		return resultList;
	}

	public void load() throws IOException, SQLException {
		List<Noun> nounList = new ArrayList<>();
		List<Iso3166> countryList = loadResource(nounList);

		for (Noun rec : nounList) {
			this.nounDao.delete(rec);
			this.nounDao.insert(rec);
		}
		for (Iso3166 rec : countryList) {
			this.iso3166Dao.delete(rec);
			this.iso3166Dao.insert(rec);
		}
	}
}
