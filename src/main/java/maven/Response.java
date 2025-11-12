package maven;

import java.util.HashMap;
import java.util.Map;

public class Response {

	private int statusCode;
	private String reason;
	private Map<String, String> headers;
	private String body;

	private Response(Builder builder) {
		this.statusCode = builder.statusCode;
		this.reason = builder.reason;
		this.headers = builder.headers;
		this.body = builder.body;
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public int statusCode() {return statusCode;}
	public String reason() {return reason;}
	public Map<String, String> headers() {return headers;}
	public String body() {return body;}

	public static class Builder {
		private int statusCode;
		private String reason;
		private Map<String, String> headers = new HashMap<>();
		private String body;

		public Builder statusCode(int statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		public Builder reason(String reason) {
			this.reason = reason;
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
			if (reason == null)
				throw new IllegalStateException("HTTP Reason phrase must be provided");
			return new Response(this);
		}
	}
}
