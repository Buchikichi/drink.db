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

import to.kit.drink.data.dao.Iso639Dao;
import to.kit.drink.data.dto.Iso639;

/**
 * 言語コードの読み込み.
 * @author H.Sasai
 */
@Component
public final class Iso639Loader implements Loadable {
	/** ファイル名. */
	private static final String RESOURCE = "/iso639.txt";
	@Autowired
	private Iso639Dao iso639Dao;

	private List<Iso639> loadResource() throws IOException {
		List<Iso639> resultList = new ArrayList<>();

		try (InputStream stream = Iso639.class.getResourceAsStream(RESOURCE);
				Reader reader = new InputStreamReader(stream);
				BufferedReader in = new BufferedReader(reader)) {
			for (;;) {
				String line = in.readLine();
				if (line == null) {
					break;
				}
				String[] elements = line.split("[\t]");
				String lang = elements[0];
				String name = elements[2];
				Iso639 rec = new Iso639();

				rec.setLang(lang);
				rec.setName(name);
				resultList.add(rec);
			}
		}
		return resultList;
	}

	@Override
	public void load() throws IOException, SQLException {
		List<Iso639> countryList = loadResource();

		for (Iso639 rec : countryList) {
			this.iso639Dao.delete(rec);
			this.iso639Dao.insert(rec);
		}
	}
}
