package to.kit.drink.data.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * `アイテム`.
 */
@Entity(name="item")
public final class Item {
	/** アイテムID. */
	@Id
	@Column(name="itemId", columnDefinition="char")
	private String itemId;
	/** 種類ID. */
	@Column(name="kindId", columnDefinition="char")
	private String kindId;
	/** 国名コード. */
	@Column(name="countryCd", columnDefinition="char")
	private String countryCd;
	/** 名称. */
	@Column(name="noteId", columnDefinition="char")
	private String noteId;
	/** 同義語. */
	@Column(name="synonym", columnDefinition="text")
	private String synonym;
	/** サムネイル. */
	@Column(name="thumbnail", columnDefinition="text")
	private String thumbnail;
	/** イメージ. */
	@Column(name="imgsrc", columnDefinition="mediumtext")
	private String imgsrc;

	/** アイテムID[char(32)]. */
	public String getItemId() {
		return this.itemId;
	}
	/** アイテムID[char(32)]. */
	public void setItemId(String value) {
		this.itemId = value;
	}
	/** 種類ID[char(32)]. */
	public String getKindId() {
		return this.kindId;
	}
	/** 種類ID[char(32)]. */
	public void setKindId(String value) {
		this.kindId = value;
	}
	/** 国名コード[char(3)]. */
	public String getCountryCd() {
		return this.countryCd;
	}
	/** 国名コード[char(3)]. */
	public void setCountryCd(String value) {
		this.countryCd = value;
	}
	/** 名称[char(32)]. */
	public String getNoteId() {
		return this.noteId;
	}
	/** 名称[char(32)]. */
	public void setNoteId(String value) {
		this.noteId = value;
	}
	/** 同義語[text]. */
	public String getSynonym() {
		return this.synonym;
	}
	/** 同義語[text]. */
	public void setSynonym(String value) {
		this.synonym = value;
	}
	/** サムネイル[text]. */
	public String getThumbnail() {
		return this.thumbnail;
	}
	/** サムネイル[text]. */
	public void setThumbnail(String value) {
		this.thumbnail = value;
	}
	/** イメージ[mediumtext]. */
	public String getImgsrc() {
		return this.imgsrc;
	}
	/** イメージ[mediumtext]. */
	public void setImgsrc(String value) {
		this.imgsrc = value;
	}
}
