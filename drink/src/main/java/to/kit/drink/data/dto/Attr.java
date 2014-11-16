package to.kit.drink.data.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * `属性`.
 */
@Entity(name="attr")
public final class Attr {
	/** 属性ID. */
	@Id
	@Column(name="attrId", columnDefinition="char")
	private String attrId;
	/** プレゼンテーション. */
	@Column(name="presentation", columnDefinition="int")
	private int presentation;

	/** 属性ID[char(32)]. */
	public String getAttrId() {
		return this.attrId;
	}
	/** 属性ID[char(32)]. */
	public void setAttrId(String value) {
		this.attrId = value;
	}
	/** プレゼンテーション[int(11)]. */
	public int getPresentation() {
		return this.presentation;
	}
	/** プレゼンテーション[int(11)]. */
	public void setPresentation(int value) {
		this.presentation = value;
	}
}
