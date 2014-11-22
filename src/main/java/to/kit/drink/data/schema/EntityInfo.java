package to.kit.drink.data.schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class EntityInfo implements Iterable<AttrInfo> {
	/** Name of entity. */
	private String name;
	/** Comment of entity. */
	private String comment;
	private List<AttrInfo> attrList = new ArrayList<>();

	@Override
	public Iterator<AttrInfo> iterator() {
		return this.attrList.iterator();
	}
	public void addAttr(AttrInfo attr) {
		this.attrList.add(attr);
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the kanji
	 */
	public String getComment() {
		return this.comment;
	}
	/**
	 * @param kanji the kanji to set
	 */
	public void setComment(String kanji) {
		this.comment = kanji;
	}
	/**
	 * @return the attrList
	 */
	public List<AttrInfo> getAttrList() {
		return this.attrList;
	}
	/**
	 * @param attrList the attrList to set
	 */
	public void setAttrList(List<AttrInfo> attrList) {
		this.attrList = attrList;
	}
}
