package to.kit.drink.data.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * `ISO 639-1`.
 */
@Entity(name="iso639")
public final class Iso639 {
	/** 言語コード. */
	@Id
	@Column(name="lang", columnDefinition="char")
	private String lang;
	/** 原語名. */
	@Column(name="name", columnDefinition="text")
	private String name;

	/** 言語コード[char(2)]. */
	public String getLang() {
		return this.lang;
	}
	/** 言語コード[char(2)]. */
	public void setLang(String value) {
		this.lang = value;
	}
	/** 原語名[text]. */
	public String getName() {
		return this.name;
	}
	/** 原語名[text]. */
	public void setName(String value) {
		this.name = value;
	}
}
