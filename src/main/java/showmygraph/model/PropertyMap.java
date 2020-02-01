package showmygraph.model;

import java.util.HashMap;

public class PropertyMap extends HashMap<String, Object> {

	static final String ID_TAG = "id";

	private static final long serialVersionUID = 1299270593497145766L;

	public PropertyMap(String id) {
		this.put(ID_TAG, id);
	}
	
	public String getID() {
		return (String) this.get(ID_TAG);
	}
}
