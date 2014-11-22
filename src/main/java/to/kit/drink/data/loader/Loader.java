package to.kit.drink.data.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64.Encoder;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import to.kit.drink.data.dto.Iso3166;
import to.kit.drink.data.util.MD5;

/**
 * データ読み込み.
 * @param <T> DTO
 * @author H.Sasai
 */
abstract class Loader<T> implements Loadable {
	/** Base64. */
	Encoder base64 = Base64.getEncoder();
	/** MD5. */
	private MD5 md5 = MD5.getInstance();

	/**
	 * シート読み込み.
	 * @param sheet シート
	 * @return Map を格納している List
	 */
	protected List<Map<String, Object>> loadSheet(Sheet sheet) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		List<String> headerList = new ArrayList<>();
		String kindId = null;

		for (Row row : sheet) {
			boolean isHeader = headerList.isEmpty();
			if (isHeader) {
				for (Cell cell : row) {
					String value = cell.getStringCellValue();
					headerList.add(value);
				}
				continue;
			}
			Cell firstCell = row.getCell(0);
			if (firstCell.getCellType() == Cell.CELL_TYPE_STRING) {
				String value = firstCell.getStringCellValue();
				if (value != null && value.startsWith("#")) {
					String kind = value.substring(1);
					kindId = md5digest(kind);
					continue;
				}
			}
			Map<String, Object> rec = new HashMap<>();
			if (kindId != null) {
				rec.put("kindId", kindId);
			}
			for (Cell cell : row) {
				int index = cell.getColumnIndex();
				String name = headerList.get(index);
				int type = cell.getCellType();
				Object value = null;

				if (type == Cell.CELL_TYPE_STRING) {
					value = cell.getStringCellValue();
				} else if (type == Cell.CELL_TYPE_NUMERIC) {
					value = Double.valueOf(cell.getNumericCellValue());
				}
				rec.put(name, value);
			}
			resultList.add(rec);
		}
		return resultList;
	}

	/**
	 * 画像データを取得.
	 * @param path
	 * @param cd
	 * @return Base64形式の画像データ
	 */
	protected String getImage(String path, String cd) {
		String result = null;
		String name = path + File.separator + cd.toLowerCase() + ".png";
		URL url = Iso3166Loader.class.getResource(name);
		if (url == null) {
			return null;
		}
		try (InputStream stream = Iso3166.class.getResourceAsStream(name)) {
			byte[] bytes = IOUtils.toByteArray(stream);

			result = this.base64.encodeToString(bytes);
		} catch (IOException e) {
			// nop
		}
		return result;
	}

	protected String md5digest(String str) {
		return this.md5.digest(str);
	}
}
