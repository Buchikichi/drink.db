package to.kit.drink.data.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * ``.
 */
@Entity(name="item_attr")
public final class ItemAttr {
	/** アイテムID. */
	@Id
	@Column(name="itemId", columnDefinition="char")
	private String itemId;
	/** 属性ID. */
	@Id
	@Column(name="attrId", columnDefinition="char")
	private String attrId;
	/** 属性値. */
	@Column(name="val", columnDefinition="text")
	private String val;

	/** アイテムID[char(32)]. */
	public String getItemId() {
		return this.itemId;
	}
	/** アイテムID[char(32)]. */
	public void setItemId(String value) {
		this.itemId = value;
	}
	/** 属性ID[char(32)]. */
	public String getAttrId() {
		return this.attrId;
	}
	/** 属性ID[char(32)]. */
	public void setAttrId(String value) {
		this.attrId = value;
	}
	/** 属性値[text]. */
	public String getVal() {
		return this.val;
	}
	/** 属性値[text]. */
	public void setVal(String value) {
		this.val = value;
	}
}
