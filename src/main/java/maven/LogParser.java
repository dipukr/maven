package maven;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

public class LogParser {
	record SummaryData(String companyName, String userName, int likes, int dislikes) {}
	record DataElem(String companyName, String userName, boolean liked) {}
	record Key(String companyName, String userName) {}

	private static String extractBlock(String src, String start, String end) {
		int s = src.indexOf(start);
		if (s < 0)
			return null;
		s += start.length();
		int e = src.indexOf(end, s);
		if (e < 0)
			e = src.length();
		return src.substring(s, e);
	}

	private static String extractHeaderValue(String headers, String key) {
		Pattern p = Pattern.compile(key + "=([^,]+(?:,(?!\\s*[a-zA-Z0-9-]+=)[^,]*)*)");
		Matcher m = p.matcher(headers);
		return m.find() ? m.group(1).trim() : null;
	}

	public static List<DataElem> ingestData(List<String> requestLines) throws Exception {
		var data = new ArrayList<DataElem>();
		var objectMapper = new ObjectMapper();

		for (String requestData : requestLines) {
			String bodyJson = extractBlock(requestData, "body=[", "]");
			JsonNode body = objectMapper.readTree(bodyJson);

			JsonNode whNode = body.get("wasHelpful");
			Boolean wasHelpful = (whNode == null || whNode.isNull()) ? null : whNode.asBoolean();

			String headers = extractBlock(requestData, "headers=[", "}]");
			String token = extractHeaderValue(headers, "authorization");

			String unsignedToken = token.substring(0, token.lastIndexOf('.') + 1);
			
			Jwt<?, Claims> jwt = Jwts.parserBuilder()
					.setClock(() -> new Date(0L))
					.build()
					.parseClaimsJwt(unsignedToken);
			
			Claims claims = jwt.getBody();

			String userName = claims.getSubject();
			String companyName = claims.get("companyName", String.class);
			String participantType = claims.get("participantType", String.class);

			if ("Financier".equals(participantType) && wasHelpful != null)
				data.add(new DataElem(companyName, userName, wasHelpful));
		}
		return data;
	}

	public static List<SummaryData> getSummary(List<DataElem> data) {
		if (data == null || data.isEmpty()) 
			return Collections.emptyList();

		Map<Key, int[]> counts = new LinkedHashMap<>();

		for (DataElem elem: data) {
			var key = new Key(elem.companyName(), elem.userName());
			int[] c = counts.computeIfAbsent(key, k -> new int[2]);
			if (elem.liked()) c[0]++;
			else c[1]++;
		}

		return counts.entrySet().stream().map(en -> 
				new SummaryData(
						en.getKey().companyName(),
						en.getKey().userName(),
						en.getValue()[0],
						en.getValue()[1])
		).toList();
	}

	public static void main(String[] args) throws Exception {
		Predicate<String> pred1 = (arg) -> arg.contains("REQUEST");
		Predicate<String> pred2 = (arg) -> arg.contains("/whatsNewDocument/feedback");
		Predicate<String> cond = pred1.and(pred2);

		Path path = Path.of("portal-27-05-2026-0.log");
		List<String> requestLines = Files.lines(path).filter(cond).toList();
		//System.out.println(requestLines.size());

		List<DataElem> data = ingestData(requestLines);
		//data.forEach(System.out::println);
		
		List<SummaryData> summaryData = getSummary(data);
		summaryData.forEach(System.out::println);
	}
}
