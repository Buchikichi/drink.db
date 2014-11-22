package to.kit.drink.data.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * `ISO 3166-1`.
 */
@Entity(name="iso3166")
public final class Iso3166 {
	/** 国名コード. */
	@Id
	@Column(name="countryCd", columnDefinition="char")
	private String countryCd;
	/** 名詞ID. */
	@Column(name="nounId", columnDefinition="char")
	private String nounId;
	/** 同義語. */
	@Column(name="synonym", columnDefinition="text")
	private String synonym;
	/** 旗(base64). */
	@Column(name="flag", columnDefinition="text")
	private String flag;

	/** 国名コード[char(3)]. */
	public String getCountryCd() {
		return this.countryCd;
	}
	/** 国名コード[char(3)]. */
	public void setCountryCd(String value) {
		this.countryCd = value;
	}
	/** 名詞ID[char(32)]. */
	public String getNounId() {
		return this.nounId;
	}
	/** 名詞ID[char(32)]. */
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
	/** 旗(base64)[text]. */
	public String getFlag() {
		return this.flag;
	}
	/** 旗(base64)[text]. */
	public void setFlag(String value) {
		this.flag = value;
	}
}
