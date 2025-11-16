package maven;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class Request {

	public static final String GET = "GET";
	public static final String POST = "POST";
	
	private String method;
	private String url;
	private Map<String, String> headers = new HashMap<>();
	private Map<String, String> queryParams = new HashMap<>();
	private String body;

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
	
	public String method() {return method;}
	public String url() {return url;}
	public Map<String, String> headers() {return headers;}
	public Map<String, String> queryParams() {return queryParams;}
	public String body() {return body;}

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
			headers.forEach((k, v) -> headers.put(k, v));
			return this;
		}

		public Builder queryParam(String key, String value) {
			this.queryParams.put(key, value);
			return this;
		}
		
		public Builder queryParams(Map<String, String> queryParams) {
			queryParams.forEach((k, v) -> queryParams.put(k, v));
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
