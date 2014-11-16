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
import to.kit.drink.data.dao.TagsDao;
import to.kit.drink.data.dto.Noun;
import to.kit.drink.data.dto.Tags;
import to.kit.drink.data.util.MD5;

/**
 * タグの読み込み.
 * @author H.Sasai
 */
@Component
public final class TagsLoader implements Loadable {
	/** Type. */
	private static final String TYPE = Tags.class.getSimpleName();
	/** ファイル名. */
	private static final String RESOURCE = "/tags.txt";
	@Autowired
	private TagsDao tagsDao;
	@Autowired
	private NounDao nounDao;

	private List<Tags> loadResource(List<Noun> nounList)
			throws IOException {
		List<Tags> resultList = new ArrayList<>();
		String kindId = null;
		MD5 md5 = MD5.getInstance();

		try (InputStream stream = TagsLoader.class
				.getResourceAsStream(RESOURCE);
				Reader reader = new InputStreamReader(stream);
				BufferedReader in = new BufferedReader(reader)) {
			for (;;) {
				String line = in.readLine();
				if (line == null) {
					break;
				}
				if (line.startsWith("#")) {
					kindId = md5.digest(line.substring(1));
					continue;
				}
				String[] elements = line.split("[\t]");
				String en = elements[0];
				String ja = elements[1];
				String id = md5.digest(TYPE + en);
				Tags rec = new Tags();

				rec.setTagId(id);
				rec.setKindId(kindId);
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
		List<Tags> tagsList = loadResource(nounList);

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
