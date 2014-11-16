package to.kit.drink.data.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * `名詞`.
 */
@Entity(name="noun")
public final class Noun {
	/** 名詞ID. */
	@Id
	@Column(name="nounId", columnDefinition="char")
	private String nounId;
	/** 言語コード. */
	@Id
	@Column(name="lang", columnDefinition="char")
	private String lang;
	/** 名詞. */
	@Column(name="noun", columnDefinition="text")
	private String noun;

	/** 名詞ID[char(32)]. */
	public String getNounId() {
		return this.nounId;
	}
	/** 名詞ID[char(32)]. */
	public void setNounId(String value) {
		this.nounId = value;
	}
	/** 言語コード[char(2)]. */
	public String getLang() {
		return this.lang;
	}
	/** 言語コード[char(2)]. */
	public void setLang(String value) {
		this.lang = value;
	}
	/** 名詞[text]. */
	public String getNoun() {
		return this.noun;
	}
	/** 名詞[text]. */
	public void setNoun(String value) {
		this.noun = value;
	}
}
