package maven;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class Request {

	public static final String GET = "GET";
	public static final String POST = "POST";
	
	private final String method;
	private final String url;
	private final Map<String, String> headers;
	private final Map<String, String> queryParams;
	private final String body;

	private Request(Builder builder) {
		this.method = builder.method;
		this.url = builder.url;
		this.headers = builder.headers;
		this.queryParams = builder.queryParams;
		this.body = builder.body;
	}

	public Optional<String> header(String name) {
		return Optional.ofNullable(headers.get(name.toLowerCase(Locale.ROOT)));
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String method;
		private String url;
		private Map<String, String> headers = new HashMap<>();
		private Map<String, String> queryParams = new HashMap<>();
		private String body;

		public Builder method(String method) {
			this.method = method;
			return this;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Builder header(String key, String value) {
			this.headers.put(key, value);
			return this;
		}
		
		public Builder headers(Map<String, String> headers) {
			this.headers = headers;
			return this;
		}

		public Builder queryParam(String key, String value) {
			this.queryParams.put(key, value);
			return this;
		}
		
		public Builder queryParams(Map<String, String> queryParams) {
			this.queryParams = queryParams;
			return this;
		}

		public Builder body(String body) {
			this.body = body;
			return this;
		}

		public Request build() {
			if (method == null || url == null)
				throw new IllegalStateException("HTTP Method and URL must be provided");
			return new Request(this);
		}
	}
}
