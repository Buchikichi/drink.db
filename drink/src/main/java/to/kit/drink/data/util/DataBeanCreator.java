package to.kit.drink.data.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

import to.kit.drink.data.schema.AttrInfo;
import to.kit.drink.data.schema.EntityInfo;
import to.kit.drink.data.schema.Schema;
import to.kit.drink.data.schema.SchemaLoader;

public final class DataBeanCreator {
	private static final String BASE_PATH = "src/main/java";
	private static final String PACKAGE = "to.kit.drink.data.dto";
	private static final String INDENT = "\t";
	private static final String LF = "\n";

	private String makeSetter(AttrInfo attr) {
		StringBuilder buff = new StringBuilder();
		String name = attr.getName();
		String capName = StringUtils.capitalize(attr.getName());
		String argName = "value";

		buff.append(INDENT);
		buff.append("/** ");
		buff.append(attr.getComment());
		buff.append("[");
		buff.append(attr.getType());
		String size = attr.getSize();
		if (!StringUtils.isEmpty(size)) {
			buff.append(String.format("(%s)", size));
		}
		buff.append("]");
		buff.append(". */");
		buff.append(LF);
		buff.append(INDENT);
		buff.append("public void set");
		buff.append(capName);
		buff.append("(");
		buff.append(attr.getJavaType());
		buff.append(" ");
		buff.append(argName);
		buff.append(") {");
		buff.append(LF);
		buff.append(INDENT);
		buff.append(INDENT);
		buff.append("this.");
		buff.append(name);
		buff.append(" = ");
		buff.append(argName);
		buff.append(";");
		buff.append(LF);
		buff.append(INDENT);
		buff.append("}");
		buff.append(LF);
		return buff.toString();
	}

	private String makeGetter(AttrInfo attr) {
		StringBuilder buff = new StringBuilder();
		String name = attr.getName();
		String capName = StringUtils.capitalize(attr.getName());

		buff.append(INDENT);
		buff.append("/** ");
		buff.append(attr.getComment());
		buff.append("[");
		buff.append(attr.getType());
		String size = attr.getSize();
		if (!StringUtils.isEmpty(size)) {
			buff.append(String.format("(%s)", size));
		}
		buff.append("]");
		buff.append(". */");
		buff.append(LF);
		buff.append(INDENT);
		buff.append("public ");
		buff.append(attr.getJavaType());
		buff.append(" get");
		buff.append(capName);
		buff.append("() {");
		buff.append(LF);
		buff.append(INDENT);
		buff.append(INDENT);
		buff.append("return this.");
		buff.append(name);
		buff.append(";");
		buff.append(LF);
		buff.append(INDENT);
		buff.append("}");
		buff.append(LF);
		return buff.toString();
	}

	private String makeFields(EntityInfo entity) {
		StringBuilder buff = new StringBuilder();

		for (AttrInfo attr : entity) {
			String name = attr.getName();

			buff.append(INDENT);
			buff.append("/** ");
			buff.append(attr.getComment());
			buff.append(". */");
			buff.append(LF);
			// annotation
			if (attr.isPk()) {
				buff.append(INDENT);
				buff.append("@Id");
				buff.append(LF);
			}
			buff.append(INDENT);
			buff.append("@Column(name=\"");
			buff.append(name);
			buff.append("\", columnDefinition=\"");
			buff.append(attr.getType());
			buff.append("\")");
			buff.append(LF);
			// definition
			buff.append(INDENT);
			buff.append("private ");
			buff.append(attr.getJavaType());
			buff.append(" ");
			buff.append(name);
			buff.append(";");
			buff.append(LF);
		}
		return buff.toString();
	}

	private String make(EntityInfo entity) {
		StringBuilder buff = new StringBuilder();
		String name = entity.getName();
		String className = getClassName(name);

		buff.append("package ");
		buff.append(PACKAGE);
		buff.append(";");
		buff.append(LF);
		buff.append(LF);
		buff.append("import javax.persistence.Column;");
		buff.append(LF);
		buff.append("import javax.persistence.Entity;");
		buff.append(LF);
		buff.append("import javax.persistence.Id;");
		buff.append(LF);
		buff.append(LF);
		buff.append("/**");
		buff.append(LF);
		buff.append(" * `");
		buff.append(entity.getComment());
		buff.append("`.");
		buff.append(LF);
		buff.append(" */");
		buff.append(LF);
		buff.append("@Entity(name=\"");
		buff.append(name);
		buff.append("\")");
		buff.append(LF);
		buff.append(String.format("public final class %s {", className));
		buff.append(LF);
		buff.append(makeFields(entity));
		buff.append(LF);
		for (AttrInfo attr : entity) {
			buff.append(makeGetter(attr));
			buff.append(makeSetter(attr));
		}
		buff.append("}");
		buff.append(LF);
		return buff.toString();
	}

	private String getClassName(String name) {
		StringBuilder result = new StringBuilder();
		boolean upper = true;

		for (char ch : name.toCharArray()) {
			if (ch == '_') {
				upper = true;
				continue;
			}
			if (upper) {
				result.append(Character.toUpperCase(ch));
				upper = false;
			} else {
				result.append(ch);
			}
		}
		return result.toString();
	}

	private String makeFilename(String name) {
		StringBuilder buff = new StringBuilder();
		String pkg = PACKAGE.replace('.', File.separatorChar);
		String className = getClassName(name);

		buff.append(BASE_PATH);
		buff.append(File.separator);
		buff.append(pkg);
		buff.append(File.separator);
		buff.append(className);
		buff.append(".java");
		return buff.toString();
	}

	public void execute() throws SQLException, IOException {
		SchemaLoader loader = new SchemaLoader();
		Schema schema = loader.loadSchema();

		for (EntityInfo entity : schema) {
			String content = make(entity);
			String name = entity.getName();
			String fileName = makeFilename(name);
			File file = new File(fileName);

			try (FileWriter out = new FileWriter(file)) {
				out.write(content);
			}
		}
		System.out.println("Done.");

	}

	public static void main(String[] args) throws Exception {
		DataBeanCreator app = new DataBeanCreator();

		app.execute();
	}

}
