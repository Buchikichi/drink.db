package to.kit.drink.data.loader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import to.kit.drink.data.dao.AttrDao;
import to.kit.drink.data.dao.ItemAttrDao;
import to.kit.drink.data.dao.ItemDao;
import to.kit.drink.data.dao.ItemTagsDao;
import to.kit.drink.data.dao.NounDao;
import to.kit.drink.data.dao.TagsDao;
import to.kit.drink.data.dto.Item;
import to.kit.drink.data.dto.ItemAttr;
import to.kit.drink.data.dto.ItemTags;
import to.kit.drink.data.dto.Noun;
import to.kit.drink.data.dto.Tags;

/**
 * タグの読み込み.
 * @author H.Sasai
 */
@Component
public final class ItemLoader extends Loader<Tags> {
	/** Type. */
	private static final String TYPE = Item.class.getSimpleName();
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private ItemAttrDao itemAttrDao;
	@Autowired
	private ItemTagsDao itemTagsDao;
	@Autowired
	private AttrDao attrDao;
	@Autowired
	private TagsDao tagsDao;
	@Autowired
	private NounDao nounDao;

	@Override
	public void load(Sheet sheet) throws SQLException {
		List<Noun> attrList = this.attrDao.getAttrList();
		List<Item> itemList = new ArrayList<>();
		List<Noun> nounList = new ArrayList<>();
		List<ItemAttr> itemAttrList = new ArrayList<>();
		List<ItemTags> itemTagsList = new ArrayList<>();

		for (Map<String, Object> map : loadSheet(sheet)) {
			Item rec = new Item();
			String en = (String) map.get("en");
			String ja = (String) map.get("ja");
			String itemId = md5digest(TYPE + en);
			String kindId = (String) map.get("kindId");
			String countryCd = (String) map.get("countryCd");
			String tags = (String) map.get("tags");
//			String synonym = (String) map.get("synonym");

			rec.setItemId(itemId);
			rec.setKindId(kindId);
			rec.setCountryCd(countryCd);
			rec.setNoteId(""); // TODO Note
			rec.setSynonym("");
			rec.setThumbnail(null); // TODO Thumbnai
			rec.setImgsrc(null); // TODO Imgsrc
			itemList.add(rec);
			// Attr
			for (Noun noun : attrList) {
				ItemAttr itemAttr = new ItemAttr();
				Object val = map.get(noun.getNoun());

				itemAttr.setItemId(itemId);
				itemAttr.setAttrId(noun.getNounId());
				itemAttr.setVal(String.valueOf(val));
				itemAttrList.add(itemAttr);
			}
			// Tags
			if (tags != null) {
				for (String tag : tags.split(",")) {
					String tagId = this.tagsDao.getTagId(tag);
					if (tagId == null) {
						continue;
					}
					ItemTags itemTags = new ItemTags();
					itemTags.setItemId(itemId);
					itemTags.setTagId(tagId);
					itemTagsList.add(itemTags);
				}
			}
			// English
			Noun enNoun = new Noun();
			enNoun.setNounId(itemId);
			enNoun.setLang("en");
			enNoun.setNoun(en);
			nounList.add(enNoun);
			// Japanese
			Noun jaNoun = new Noun();
			jaNoun.setNounId(itemId);
			jaNoun.setLang("ja");
			jaNoun.setNoun(ja);
			nounList.add(jaNoun);
		}
		for (Noun rec : nounList) {
			this.nounDao.delete(rec);
			this.nounDao.insert(rec);
		}
		for (Item rec : itemList) {
			this.itemDao.delete(rec);
			this.itemDao.insert(rec);
		}
		for (ItemAttr rec : itemAttrList) {
			this.itemAttrDao.delete(rec);
			this.itemAttrDao.insert(rec);
		}
		for (ItemTags rec : itemTagsList) {
			this.itemTagsDao.delete(rec);
			this.itemTagsDao.insert(rec);
		}
	}
}
