package to.kit.drink.data.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import to.kit.drink.data.dao.AttrDao;
import to.kit.drink.data.dao.NounDao;
import to.kit.drink.data.dto.Attr;
import to.kit.drink.data.dto.Noun;
import to.kit.drink.data.util.MD5;

/**
 * 「種類」の読み込み.
 * @author H.Sasai
 */
@Component
public final class AttrLoader implements Loadable {
	/** Type. */
	private static final String TYPE = Attr.class.getSimpleName();
	/** ファイル名. */
	private static final String RESOURCE = "/attr.txt";
	@Autowired
	private AttrDao attrDao;
	@Autowired
	private NounDao nounDao;

	private List<Attr> loadResource(List<Noun> nounList)
			throws IOException {
		List<Attr> resultList = new ArrayList<>();
		MD5 md5 = MD5.getInstance();

		try (InputStream stream = AttrLoader.class
				.getResourceAsStream(RESOURCE);
				Reader reader = new InputStreamReader(stream);
				BufferedReader in = new BufferedReader(reader)) {
			for (;;) {
				String line = in.readLine();
				if (line == null) {
					break;
				}
				if (line.startsWith("#")) {
					continue;
				}
				String[] elements = line.split("[\t]");
				String en = elements[0];
				String ja = elements[1];
				int presentation = NumberUtils.toInt(elements[2]);
				String id = md5.digest(TYPE + en);
				Attr rec = new Attr();

				rec.setAttrId(id);
				rec.setPresentation(presentation);
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
		List<Attr> attrList = loadResource(nounList);

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
