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

import to.kit.drink.data.dao.KindDao;
import to.kit.drink.data.dao.NounDao;
import to.kit.drink.data.dto.Kind;
import to.kit.drink.data.dto.Noun;
import to.kit.drink.data.util.MD5;

/**
 * 「種類」の読み込み.
 * @author H.Sasai
 */
@Component
public final class KindLoader implements Loadable {
	/** Type. */
	private static final String TYPE = Kind.class.getSimpleName();
	/** ファイル名. */
	private static final String RESOURCE = "/kind.txt";
	@Autowired
	private KindDao kindDao;
	@Autowired
	private NounDao nounDao;

	private List<Kind> loadResource(List<Noun> nounList)
			throws IOException {
		List<Kind> resultList = new ArrayList<>();
		MD5 md5 = MD5.getInstance();

		try (InputStream stream = KindLoader.class
				.getResourceAsStream(RESOURCE);
				Reader reader = new InputStreamReader(stream);
				BufferedReader in = new BufferedReader(reader)) {
			for (;;) {
				String line = in.readLine();
				if (line == null) {
					break;
				}
				String[] elements = line.split("[\t]");
				String en = elements[0];
				String ja = elements[1];
				String id = md5.digest(TYPE + en);
				Kind rec = new Kind();

				rec.setKindId(id);
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
		List<Kind> kindList = loadResource(nounList);

		for (Noun rec : nounList) {
			this.nounDao.delete(rec);
			this.nounDao.insert(rec);
		}
		for (Kind rec : kindList) {
			this.kindDao.delete(rec);
			this.kindDao.insert(rec);
		}
	}
}
