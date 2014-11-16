package to.kit.drink.data.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * `アイテムタグ`.
 */
@Entity(name="item_tags")
public final class ItemTags {
	/** . */
	@Id
	@Column(name="itemId", columnDefinition="char")
	private String itemId;
	/** . */
	@Id
	@Column(name="tagId", columnDefinition="char")
	private String tagId;

	/** [char(32)]. */
	public String getItemId() {
		return this.itemId;
	}
	/** [char(32)]. */
	public void setItemId(String value) {
		this.itemId = value;
	}
	/** [char(32)]. */
	public String getTagId() {
		return this.tagId;
	}
	/** [char(32)]. */
	public void setTagId(String value) {
		this.tagId = value;
	}
}
