package maven;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

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
	
	public static void meterData(MongoCollection<Document> meterDataColl, Set<String> meterNos, String from, String to) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date fromDate = df.parse(from);
		Date toDate = df.parse(to);
		Bson dateFilter = Filters.and(Filters.gte("forDate", fromDate), Filters.lte("forDate", toDate));
		Bson meterFilter = Filters.in("meterNo", meterNos);
		Bson filter = Filters.and(dateFilter, meterFilter);
		FindIterable<Document> meterData = meterDataColl.find(filter);
		for (Document doc: meterData)
			System.out.println(doc.toJson());
		System.out.println(meterNos);
	}

	public static void main(String[] args) throws Exception {
		String uri = "mongodb://samast_user:Samast%401212@192.168.101.24:27017/?authSource=samast";
		try (MongoClient mongoClient = MongoClients.create(uri)) {
			MongoDatabase database = mongoClient.getDatabase("samast");
			MongoCollection<Document> meterDetailsColl = database.getCollection("SecureMeterDetails");
			MongoCollection<Document> meterDataColl = database.getCollection("pnPR_VEELoadSuveyData");
			Set<String> meterNos = findAllMeterNos(meterDetailsColl);
		}
	}
}
