package maven;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.bson.Document;
import org.bson.conversions.Bson;

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
	
	public static void getData(MongoDatabase database, String meterNo, String date) throws Exception {
		MongoCollection<Document> meterDataColl = database.getCollection("pnPR_VEELoadSuveyData");
		Date forDate = getDate(date);
		Bson dateFilter = Filters.and(Filters.gte("forDate", forDate),
				Filters.lte("forDate", forDate));
		Bson meterFilter = Filters.eq("meterNo", meterNo);
		Bson filter = Filters.and(dateFilter, meterFilter);
		FindIterable<Document> meterData = meterDataColl.find(filter);
		System.out.println(meterDataColl.countDocuments(filter));
		System.out.println("hello");
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

	public static void main(final String[] args) throws Exception {
		// 2025-05-25T18:30:00.000+00:00
		String uri = "mongodb://samast_user:Samast%401212@192.168.101.24:27017";
		try (MongoClient mongoClient = MongoClients.create(uri)) {
			MongoDatabase database = mongoClient.getDatabase("samast");
			MongoCollection<Document> meterDetailsColl = database.getCollection("SecureMeterDetails");
			MongoCollection<Document> meterDataColl = database.getCollection("pnPR_VEELoadSuveyData");
			//Set<String> meterNos = findAllMeterNos(meterDetailsColl);
			getData(database, "PBB48440", "2025-04-28T18:30:00.000Z");
		}
	}
}
