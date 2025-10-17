package maven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UserDAO {
	
	private Map<Integer, User> data = new HashMap<>();

	public void save(User user) {
		data.put(data.size(), user);
	}

	public List<User> findAll() {
		return new ArrayList<>(data.values());
	}

	public User find(int id) {
		return data.get(id);
	}

	public User find(String username) {
		for (Entry<Integer, User> elem: data.entrySet())
			if (elem.getValue().username().equals(username))
				return elem.getValue();
		return null;
	}
}
