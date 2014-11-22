package to.kit.drink.data.dao;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.stereotype.Component;

import to.kit.drink.data.dto.Tags;

@Component
public final class TagsDao extends DaoBase<Tags> {
	public String getTagId(String tag) throws SQLException {
		String tagId = null;
		String query = "SELECT"
			+ "   tags.tagId"
			+ " FROM tags"
			+ " INNER JOIN noun ON"
			+ "   noun.nounId = tags.tagId"
			+ "   AND noun.lang = 'en'"
			+ "   AND noun.noun = '" + tag + "'"
			;

		for (Map<String, Object> map : select(query)) {
			tagId = (String) map.get("tagId");
			break;
		}
		return tagId;
	}
}
