package maven;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MonDB {
	public static Date getDate(String dates) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = null;
		try {
			date = df.parse(dates);
		} catch (ParseException e) {
			System.out.println("Could not parse date.");
			System.exit(0);
		}
		return date;
	}
	
	public static Set<String> findAllMeterNos(MongoCollection<Document> meterDetailsColl) {
		FindIterable<Document> data = meterDetailsColl.find();
		Set<String> meterNos = new TreeSet<>();
		for (Document doc: data)
			meterNos.add(doc.getString("meterNo"));
		return meterNos;
	}
	
	public static BigDecimal roundUP(BigDecimal num, int decimalCount) {
		if (num == null) return null;
		return num.setScale(decimalCount, RoundingMode.HALF_UP);
	}
	
	public static void getData(MongoDatabase database, String meterNo, String date, String param) throws Exception {
		MongoCollection<Document> meterDataColl = database.getCollection("pnPR_VEELoadSuveyData");
		Date forDate = getDate(date);
		Bson dateFilter = Filters.and(Filters.gte("forDate", forDate),
				Filters.lte("forDate", forDate));
		Bson meterFilter = Filters.eq("meterNo", meterNo);
		Bson filter = Filters.and(dateFilter, meterFilter);
		FindIterable<Document> meterData = meterDataColl.find(filter);
		for (Document doc: meterData) {
			Document dc = (Document) doc.get(param);
			Map<Integer, BigDecimal> blockWiseQtms = new TreeMap<>();
			dc.entrySet().forEach(elem -> blockWiseQtms.put(Integer.valueOf(elem.getKey()), new BigDecimal((String)elem.getValue())));
			for (BigDecimal qtm: blockWiseQtms.values())
				System.out.println(roundUP(qtm.divide(new BigDecimal("1000.0")), 5));
		}
	}
	
	public static void getData2(MongoDatabase database) throws Exception {
		List<String> lines = Files.lines(Path.of("/home/dkumar/nvkn.scada.csv")).toList();
		List<ObjectId> ids = new ArrayList<>();
		for (String line: lines) {
			if (line.length()==24)
			ids.add(new ObjectId(line));
			else ids.add(new ObjectId(line.split("#")[0]));
		}
		PrintWriter wr = new PrintWriter("/home/dkumar/data.txt");
		List<String> result = new ArrayList<>();
		MongoCollection<Document> meterDataColl = database.getCollection("scada");
		Bson filter = Filters.in("_id", ids);
		System.out.println(meterDataColl.countDocuments(filter));
		FindIterable<Document> meterData = meterDataColl.find(filter);
		int count = 0;
		for (Document doc: meterData) {
			System.out.printf("Processing: %d\n", count++);
			Document dc = (Document) doc.get("wind_speed");
			Collection<Object> values = dc.values();
			for (Object value: values) {
				if (value != null && !value.toString().contains(".")) {
					wr.println(doc.get("_id").toString());
					break;
				}
			}
		}
		wr.flush();
		wr.close();
	}
	
	public static void meterData(MongoCollection<Document> meterDataColl, Set<String> meterNos, String from, String to) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date fromDate = df.parse(from);
		Date toDate = df.parse(to);
		Bson dateFilter = Filters.and(Filters.gte("forDate", fromDate),
				Filters.lte("forDate", toDate));
		Bson meterFilter = Filters.in("meterNo", meterNos);
		Bson filter = Filters.and(dateFilter, meterFilter);
		FindIterable<Document> meterData = meterDataColl.find(filter);
		for (Document doc: meterData) {
			System.out.println(doc.toJson());
		}
		System.out.println(meterNos);
	}
	
	public static void meterData2(MongoCollection<Document> meterDataColl, Set<String> meterNos, String from, String to) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date fromDate = df.parse(from);
		Date toDate = df.parse(to);
		Bson dateFilter = Filters.and(Filters.gte("forDate", fromDate),
				Filters.lte("forDate", toDate));
		Bson meterFilter = Filters.in("meterNo", meterNos);
		Bson filter = Filters.and(dateFilter, meterFilter);
		FindIterable<Document> meterData = meterDataColl.find(filter);
		for (Document doc: meterData) {
			System.out.println(doc.toJson());
		}
		System.out.println(meterNos);
	}

	public static void main(final String[] args) throws Exception {
		// 2025-05-25T18:30:00.000+00:00
		String uri = "mongodb://sneha:Sneha%401212@3.6.233.10:27017/?authSource=nvkn";
		try (MongoClient mongoClient = MongoClients.create(uri)) {
			MongoDatabase database = mongoClient.getDatabase("nvkn");
			//MongoCollection<Document> meterDetailsColl = database.getCollection("SecureMeterDetails");
			//MongoCollection<Document> meterDataColl = database.getCollection("pnPR_VEELoadSuveyData");
			//Set<String> meterNos = findAllMeterNos(meterDetailsColl);
			getData2(database);
		}
	}
}
