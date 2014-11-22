package to.kit.drink.data.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * `種類`.
 */
@Entity(name="kind")
public final class Kind {
	/** . */
	@Id
	@Column(name="kindId", columnDefinition="char")
	private String kindId;

	/** [char(32)]. */
	public String getKindId() {
		return this.kindId;
	}
	/** [char(32)]. */
	public void setKindId(String value) {
		this.kindId = value;
	}
}
