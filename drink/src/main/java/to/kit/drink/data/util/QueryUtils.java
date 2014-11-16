package to.kit.drink.data.util;

public final class QueryUtils {
	public static String escape(String value) {
		String result = value.replace("\\", "\\\\");

		result = result.replace("'", "\\'");
		return result;
	}

	public static String quote(String value) {
		return "'" + escape(value) + "'";
	}
}
