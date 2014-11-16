package to.kit.drink.data.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * `タグ`.
 */
@Entity(name="tags")
public final class Tags {
	/** タグ種類. */
	@Id
	@Column(name="tagId", columnDefinition="char")
	private String tagId;
	/** タグID. */
	@Column(name="kindId", columnDefinition="char")
	private String kindId;

	/** タグ種類[char(32)]. */
	public String getTagId() {
		return this.tagId;
	}
	/** タグ種類[char(32)]. */
	public void setTagId(String value) {
		this.tagId = value;
	}
	/** タグID[char(32)]. */
	public String getKindId() {
		return this.kindId;
	}
	/** タグID[char(32)]. */
	public void setKindId(String value) {
		this.kindId = value;
	}
}
