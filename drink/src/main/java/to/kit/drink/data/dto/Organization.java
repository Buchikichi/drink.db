package to.kit.drink.data.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * `組織`.
 */
@Entity(name="organization")
public final class Organization {
	/** 組織ID. */
	@Id
	@Column(name="orgId")
	private String orgId;
	/** 国名コード. */
	@Column(name="countryCd")
	private String countryCd;
	/** 名称. */
	@Column(name="nounId")
	private String nounId;
	/** 読み. */
	@Column(name="reading")
	private String reading;

	/** 組織ID[char(32)]. */
	public String getOrgId() {
		return this.orgId;
	}
	/** 組織ID[char(32)]. */
	public void setOrgId(String value) {
		this.orgId = value;
	}
	/** 国名コード[char(3)]. */
	public String getCountryCd() {
		return this.countryCd;
	}
	/** 国名コード[char(3)]. */
	public void setCountryCd(String value) {
		this.countryCd = value;
	}
	/** 名称[char(32)]. */
	public String getNounId() {
		return this.nounId;
	}
	/** 名称[char(32)]. */
	public void setNounId(String value) {
		this.nounId = value;
	}
	/** 読み[text]. */
	public String getReading() {
		return this.reading;
	}
	/** 読み[text]. */
	public void setReading(String value) {
		this.reading = value;
	}
}
