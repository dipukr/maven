package maven;

import java.util.Map;

//record Node(String host, int port) {}
record User(String name, String password) {}
record Headers(Map<String, String> headers) {}