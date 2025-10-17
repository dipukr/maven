package maven;

import java.util.HashMap;
import java.util.Map;

public class Response {

	public int statusCode;
	public String reasonPhrase;
	public Map<String, String> headers;
	public String body;

	private Response(Builder builder) {
		this.statusCode = builder.statusCode;
		this.reasonPhrase = builder.reasonPhrase;
		this.headers = builder.headers;
		this.body = builder.body;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private int statusCode;
		private String reasonPhrase;
		private Map<String, String> headers = new HashMap<>();
		private String body;

		public Builder statusCode(int statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		public Builder reasonPhrase(String reasonPhrase) {
			this.reasonPhrase = reasonPhrase;
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

		public Builder body(String body) {
			this.body = body;
			return this;
		}

		public Response build() {
			if (statusCode == 0)
				throw new IllegalStateException("HTTP Status code must be provided");
			if (reasonPhrase == null)
				throw new IllegalStateException("HTTP Reason phrase must be provided");
			return new Response(this);
		}
	}
}
