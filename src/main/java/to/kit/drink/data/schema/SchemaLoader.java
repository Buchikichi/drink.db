package to.kit.drink.data.schema;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import to.kit.drink.data.DBConnection;

/**
 * Schema loader.
 * @author H.Sasai
 */
public final class SchemaLoader {
	private static final String DDL_DIR = "ddl/";

	private List<EntityInfo> getTableList(Statement stmt) throws SQLException {
		List<EntityInfo> result = new ArrayList<>();
		String query = "SHOW TABLE STATUS";

		try (ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				EntityInfo entity = new EntityInfo();
				String tableName = rs.getString("Name");
				String comment = rs.getString("Comment");

				entity.setName(tableName);
				entity.setComment(StringUtils.defaultString(comment));
				result.add(entity);
			}
		}
		return result;
	}

	private void saveTableDefinition(Statement stmt, EntityInfo entity)
			throws SQLException, IOException {
		String name = entity.getName();
		String query = "SHOW CREATE TABLE `" + name + "`";

		try (ResultSet rs = stmt.executeQuery(query)) {
			if (rs.next()) {
				String definition = rs.getString("Create Table");
				File file = new File(DDL_DIR + name + ".dll");
				try (FileWriter out = new FileWriter(file)) {
					out.write(definition);
				}
			}
		}
	}

	public Schema loadSchema() throws SQLException, IOException {
		Schema schema = new Schema();

		try (Connection conn = DBConnection.getInstance().getConnection()) {
			try (Statement stmt = conn.createStatement()) {
				for (EntityInfo entity : getTableList(stmt)) {
					String query = "SHOW FULL COLUMNS FROM `"
							+ entity.getName() + "`";

					try (ResultSet rs = stmt.executeQuery(query)) {
						while (rs.next()) {
							AttrInfo attr = new AttrInfo();
							String name = rs.getString("Field");
							String[] type = rs.getString("Type").split("[()]");
							String nullable = rs.getString("Null");
							String key = rs.getString("Key");
							String comment = rs.getString("Comment");

							attr.setName(name);
							attr.setType(type[0]);
							if (1 < type.length) {
								attr.setSize(type[1]);
							}
							attr.setNullable(nullable.equals("NO"));
							attr.setPk(key.equals("PRI"));
							attr.setComment(StringUtils.defaultString(comment));
							entity.addAttr(attr);
						}
					}
					schema.addEntity(entity);
					saveTableDefinition(stmt, entity);
				}
			}
		}
		return schema;
	}
}
