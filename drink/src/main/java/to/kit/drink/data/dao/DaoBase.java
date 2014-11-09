package to.kit.drink.data.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import to.kit.drink.data.DBConnection;

/**
 * Data access object.
 * @param <T> DTO
 * @author H.Sasai
 */
abstract class DaoBase<T> {
	/** A connection with a specific database. */
	private Connection conn = DBConnection.getInstance().getConnection();
	/** Column list of table. */
	private List<String> columns = new ArrayList<>();

	protected List<String> getColumnList(Class<?> clazz) {
		if (this.columns.isEmpty()) {
			for (Field field : clazz.getDeclaredFields()) {
				this.columns.add(field.getName());
			}
		}
		return this.columns;
	}

	protected List<String> getKeyList(Class<?> clazz) {
		List<String> list = new ArrayList<>();
		for (Field field : clazz.getDeclaredFields()) {
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				list.add(field.getName());
			}
		}
		return list;
	}

	private String getEntityName(Class<?> clazz) {
		Entity entity = clazz.getAnnotation(Entity.class);

		return entity.name();
	}

	private String makeInsertStatement(T rec) {
		Class<?> clazz = rec.getClass();
		List<String> columnList = getColumnList(clazz);
		List<String> valueList = new ArrayList<>();
		String entityName = getEntityName(clazz);

		for (String column : columnList) {
			String prop = null;
			try {
				prop = BeanUtils.getProperty(rec, column);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				// nop
			}
			String value;
			if (prop == null) {
				value = "null";
			} else {
				value = "'" + prop + "'";
			}
			valueList.add(value);
		}
		StringBuilder buff = new StringBuilder();

		buff.append("insert into ");
		buff.append(entityName);
		buff.append("(");
		buff.append(StringUtils.join(columnList, ','));
		buff.append(") values (");
		buff.append(StringUtils.join(valueList, ','));
		buff.append(")");
		return buff.toString();
	}

//	private String makeWhereClause(T criteria) {
//		return null;
//	}

	public int insert(T rec) throws SQLException {
		String sql = makeInsertStatement(rec);

		try (Statement stmt = this.conn.createStatement()) {
			return stmt.executeUpdate(sql);
		}
	}

	public int delete(T rec) throws SQLException {
		List<String> keyList = getKeyList(rec.getClass());
		StringBuilder buff = new StringBuilder();
		Class<?> clazz = rec.getClass();
		String entityName = getEntityName(clazz);
		List<String> condList = new ArrayList<>();

		for (String column : keyList) {
			String prop = null;
			try {
				prop = BeanUtils.getProperty(rec, column);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				// nop
			}
			if (prop != null) {
				String cond = column + "='" + prop + "'";
				condList.add(cond);
			}
		}
		buff.append("delete from ");
		buff.append(entityName);
		buff.append(" where ");
		buff.append(StringUtils.join(condList, " AND "));
		try (Statement stmt = this.conn.createStatement()) {
			return stmt.executeUpdate(buff.toString());
		}
	}
}
