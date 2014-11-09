package to.kit.drink.data.schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Schema implements Iterable<EntityInfo> {
	/** Entity list. */
	private List<EntityInfo> entityList = new ArrayList<>();

	@Override
	public Iterator<EntityInfo> iterator() {
		return this.entityList.iterator();
	}

	public void addEntity(EntityInfo entity) {
		this.entityList.add(entity);
	}
}
