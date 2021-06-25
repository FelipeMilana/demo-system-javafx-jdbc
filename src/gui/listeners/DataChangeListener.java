package gui.listeners;

import java.util.List;

public interface DataChangeListener {

	void onDataChanged();
	
	<T> void onDataChangedSearch(List<T> list);
}
