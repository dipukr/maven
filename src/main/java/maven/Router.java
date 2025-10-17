package maven;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Router {
	
	private Map<String, Map<String, Handler>> routes;
	
	public Router() {
		this.routes = new ConcurrentHashMap<>();
		this.routes.put("GET", new HashMap<>());
		this.routes.put("POST", new HashMap<>());
		this.routes.put("PUT", new HashMap<>());
		this.routes.put("DELETE", new HashMap<>());
	}
	
	public void register(String method, String path, Handler handler) {
		if (routes.containsKey(method))
			routes.get(method).put(path, handler);
	}
	
	public Optional<Handler> find(String method, String path) {
		if (routes.containsKey(method)) {
			Map<String, Handler> paths = routes.get(method);
			if (paths.containsKey(path))
				return Optional.ofNullable(paths.get(path));
			for (var elem: paths.entrySet()) {
				String key = elem.getKey();
				Handler val = elem.getValue();
				if (key.startsWith(path))
					return Optional.ofNullable(val);
			}
		}
		return Optional.empty();
	}
}
