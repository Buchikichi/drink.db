package to.kit.drink.data.loader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
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
import to.kit.drink.data.util.JpnUtils;

/**
 * タグの読み込み.
 * @author H.Sasai
 */
@Component
public final class ItemLoader extends Loader<Tags> {
	/** イメージパス名. */
	private static final String IMG_PATH = "/img/item/";
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
			ItemData row = new ItemData();

			try {
				BeanUtils.populate(row, map);
			} catch (IllegalAccessException | InvocationTargetException e) {
				// nop
			}
			String en = row.getEn().trim();
			String enNote = row.getEnNote();
			String ja = row.getJa();
			String jaNote = row.getJaNote();
			String itemId = md5digest(TYPE + en);
			String noteId = md5digest(TYPE + "note" + en);
			String tags = row.getTags();
			String brewer = row.getBrewer();
			List<String> synonymList = new ArrayList<>();

			synonymList.add(en.toLowerCase());
			synonymList.add(enNote.toLowerCase());
			synonymList.add(JpnUtils.toHiragana(ja));
			synonymList.add(JpnUtils.toHiragana(jaNote));
			synonymList.add(tags);
			synonymList.add(JpnUtils.toHiragana(brewer.toLowerCase()));
			rec.setItemId(itemId);
			rec.setKindId(row.getKindId());
			rec.setCountryCd(row.getCountryCd());
			rec.setNoteId(noteId);
			String synonym = StringUtils.join(synonymList, '\t');
			synonym = synonym.replaceAll("[ ・]", "");
			rec.setSynonym(synonym);
			String img = row.getImg();
			if (StringUtils.isBlank(img)) {
				img = en.toLowerCase().replaceAll("[\\s]", "");
			}
			String imgpath = IMG_PATH + "beer" + File.separator
					+ row.getCountryCd();
			rec.setThumbnail(getThumbnail(imgpath, img));
			rec.setImgsrc(getImage(imgpath, img));
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
			for (String str : tags.split("[,]")) {
				String tag = str.trim();
				if (tag.isEmpty()) {
					continue;
				}
				String tagId = this.tagsDao.getTagId(tag);
				if (tagId == null) {
					System.err.println("Unknown tag:" + tag);
					continue;
				}
				ItemTags itemTags = new ItemTags();
				itemTags.setItemId(itemId);
				itemTags.setTagId(tagId);
				itemTagsList.add(itemTags);
			}
			// Item name
			nounList.add(NounDao.createNoun(itemId, "en", en));
			nounList.add(NounDao.createNoun(itemId, "ja", ja));
			// Item note
			if (StringUtils.isNotBlank(enNote)) {
				nounList.add(NounDao.createNoun(noteId, "en", enNote));
			}
			if (StringUtils.isNotBlank(jaNote)) {
				nounList.add(NounDao.createNoun(noteId, "ja", jaNote));
			}
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

	public class ItemData {
		private String kindId = StringUtils.EMPTY;
		private String countryCd = StringUtils.EMPTY;
		private String en = StringUtils.EMPTY;
		private String enNote = StringUtils.EMPTY;
		private String ja = StringUtils.EMPTY;
		private String jaNote = StringUtils.EMPTY;
		private String img = StringUtils.EMPTY;
		private String tags = StringUtils.EMPTY;
		private String synonym = StringUtils.EMPTY;
		private String brewer = StringUtils.EMPTY;
		/**
		 * @return the kindId
		 */
		public String getKindId() {
			return this.kindId;
		}
		/**
		 * @param kindId the kindId to set
		 */
		public void setKindId(String kindId) {
			this.kindId = kindId;
		}
		/**
		 * @return the countryCd
		 */
		public String getCountryCd() {
			return this.countryCd;
		}
		/**
		 * @param countryCd the countryCd to set
		 */
		public void setCountryCd(String countryCd) {
			this.countryCd = countryCd;
		}
		/**
		 * @return the en
		 */
		public String getEn() {
			return this.en;
		}
		/**
		 * @param en the en to set
		 */
		public void setEn(String en) {
			this.en = en;
		}
		/**
		 * @return the enNote
		 */
		public String getEnNote() {
			return this.enNote;
		}
		/**
		 * @param enNote the enNote to set
		 */
		public void setEnNote(String enNote) {
			this.enNote = enNote;
		}
		/**
		 * @return the ja
		 */
		public String getJa() {
			return this.ja;
		}
		/**
		 * @param ja the ja to set
		 */
		public void setJa(String ja) {
			this.ja = ja;
		}
		/**
		 * @return the jaNote
		 */
		public String getJaNote() {
			return this.jaNote;
		}
		/**
		 * @param jaNote the jaNote to set
		 */
		public void setJaNote(String jaNote) {
			this.jaNote = jaNote;
		}
		/**
		 * @return the img
		 */
		public String getImg() {
			return this.img;
		}
		/**
		 * @param img the img to set
		 */
		public void setImg(String img) {
			this.img = img;
		}
		/**
		 * @return the tags
		 */
		public String getTags() {
			return this.tags;
		}
		/**
		 * @param tags the tags to set
		 */
		public void setTags(String tags) {
			this.tags = tags;
		}
		/**
		 * @return the synonym
		 */
		public String getSynonym() {
			return this.synonym;
		}
		/**
		 * @param synonym the synonym to set
		 */
		public void setSynonym(String synonym) {
			this.synonym = synonym;
		}
		/**
		 * @return the brewer
		 */
		public String getBrewer() {
			return this.brewer;
		}
		/**
		 * @param brewer the brewer to set
		 */
		public void setBrewer(String brewer) {
			this.brewer = brewer;
		}
	}
}
