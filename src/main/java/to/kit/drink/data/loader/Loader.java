package to.kit.drink.data.loader;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import to.kit.drink.data.util.MD5;

/**
 * データ読み込み.
 * @param <T> DTO
 * @author H.Sasai
 */
abstract class Loader<T> implements Loadable {
	/** Thumbnail size. */
	private static final int THUMBNAIL_SIZE = 64;
	/** Base64. */
	private Encoder base64 = Base64.getEncoder();
	/** MD5. */
	private MD5 md5 = MD5.getInstance();

	private String conjectureKindId(Sheet sheet) {
		String name = sheet.getSheetName();
		String[] names = name.split("#");

		if (names.length == 1) {
			return null;
		}
		return md5digest(names[1]);
	}

	/**
	 * シート読み込み.
	 * @param sheet シート
	 * @return Map を格納している List
	 */
	protected List<Map<String, Object>> loadSheet(Sheet sheet) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		List<String> headerList = new ArrayList<>();
		String kindId = conjectureKindId(sheet);

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

	private byte[] getImage(String name) {
		byte[] result = null;
		final String[] targets = {".png", ".jpg"};

		for (String ext : targets) {
			String filename = name + ext;
			URL url = Loader.class.getResource(filename);

			if (url == null) {
				continue;
			}
			try (InputStream in = Loader.class.getResourceAsStream(filename)) {
				result = IOUtils.toByteArray(in);
			} catch (IOException e) {
				// nop
			}
		}
		return result;
	}

	/**
	 * 画像データを取得.
	 * @param path
	 * @param cd
	 * @return Base64形式の画像データ
	 */
	protected String getImage(String path, String cd) {
		String result = null;
		String name = path + File.separator + cd.toLowerCase();
		byte[] bytes = getImage(name);

		if (bytes != null) {
			result = this.base64.encodeToString(bytes);
		}
		return result;
	}

	private byte[] makeThumbnail(byte[] bytes) {
		byte[] result = null;
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			try (InputStream in = new ByteArrayInputStream(bytes)) {
				BufferedImage srcImg = ImageIO.read(in);
				Image thumbImg = srcImg.getScaledInstance(THUMBNAIL_SIZE,
						THUMBNAIL_SIZE, Image.SCALE_SMOOTH);
				BufferedImage dstImg = new BufferedImage(THUMBNAIL_SIZE,
						THUMBNAIL_SIZE, srcImg.getType());
				Graphics2D g = dstImg.createGraphics();

				g.drawImage(thumbImg, 0, 0, null);
				ImageIO.write(dstImg, "jpeg", out);
			}
			result = out.toByteArray();
		} catch (IOException e) {
			// nop
		}
		return result;
	}

	/**
	 * サムネイルを取得.
	 * @param path
	 * @param cd
	 * @return Base64形式の画像データ
	 */
	protected String getThumbnail(String path, String cd) {
		String result = null;
		String name = path + File.separator + cd.toLowerCase();
		byte[] bytes = getImage(name);

		if (bytes != null) {
			result = this.base64.encodeToString(makeThumbnail(bytes));
		}
		return result;
	}

	protected String md5digest(String str) {
		return this.md5.digest(str);
	}
}
