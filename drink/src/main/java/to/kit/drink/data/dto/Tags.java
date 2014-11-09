package to.kit.drink.data.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * `タグ`.
 */
@Entity(name="tags")
public final class Tags {
	/** タグID. */
	@Id
	@Column(name="tagId")
	private String tagId;
	/** タグ名. */
	@Column(name="nounId")
	private String nounId;

	/** タグID[char(32)]. */
	public String getTagId() {
		return this.tagId;
	}
	/** タグID[char(32)]. */
	public void setTagId(String value) {
		this.tagId = value;
	}
	/** タグ名[char(32)]. */
	public String getNounId() {
		return this.nounId;
	}
	/** タグ名[char(32)]. */
	public void setNounId(String value) {
		this.nounId = value;
	}
}
