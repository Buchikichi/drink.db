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
	@Column(name="nounId")
	private String nounId;
	/** 国名コード. */
	@Id
	@Column(name="countryCd")
	private String countryCd;
	/** 名詞. */
	@Column(name="noun")
	private String noun;

	/** 名詞ID[char(32)]. */
	public String getNounId() {
		return this.nounId;
	}
	/** 名詞ID[char(32)]. */
	public void setNounId(String value) {
		this.nounId = value;
	}
	/** 国名コード[char(3)]. */
	public String getCountryCd() {
		return this.countryCd;
	}
	/** 国名コード[char(3)]. */
	public void setCountryCd(String value) {
		this.countryCd = value;
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
