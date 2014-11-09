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
	@Column(name="itemId")
	private String itemId;
	/** 名称. */
	@Column(name="nounId")
	private String nounId;
	/** 同義語. */
	@Column(name="synonym")
	private String synonym;

	/** アイテムID[char(32)]. */
	public String getItemId() {
		return this.itemId;
	}
	/** アイテムID[char(32)]. */
	public void setItemId(String value) {
		this.itemId = value;
	}
	/** 名称[char(32)]. */
	public String getNounId() {
		return this.nounId;
	}
	/** 名称[char(32)]. */
	public void setNounId(String value) {
		this.nounId = value;
	}
	/** 同義語[text]. */
	public String getSynonym() {
		return this.synonym;
	}
	/** 同義語[text]. */
	public void setSynonym(String value) {
		this.synonym = value;
	}
}
