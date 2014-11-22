package to.kit.drink.data.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import to.kit.drink.data.dto.Attr;
import to.kit.drink.data.dto.Noun;

@Component
public final class AttrDao extends DaoBase<Attr> {
	public List<Noun> getAttrList() throws SQLException {
		List<Noun> resultList = new ArrayList<>();
		String query = "SELECT"
			+ "   noun.nounId,"
			+ "   noun.noun"
			+ " FROM attr"
			+ " INNER JOIN noun ON"
			+ "   noun.nounId = attr.attrId"
			+ "   AND noun.lang = 'en'"
			;

		for (Map<String, Object> map : select(query)) {
			Noun noun = new Noun();

			try {
				BeanUtils.populate(noun, map);
			} catch (IllegalAccessException | InvocationTargetException e) {
				// nop
			}
			resultList.add(noun);
		}
		return resultList;
	}
}
