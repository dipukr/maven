package maven;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
	private static void dumpUser(ResultSet rs) throws Exception {
		System.out.printf("%-5d%-10s%-20s%-10s\n",
			rs.getInt("id"),
			rs.getString("name"),
			rs.getString("email"),
			rs.getString("password"));
	}
	
	private static void dump(ResultSet rs) throws Exception {
		System.out.println(rs.getString("oa_number"));
	}
	
	public static void main(String[] args) throws Exception {
		String url = "jdbc:mysql://216.48.181.146:3306/samast";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, "dipu", "Dipu@1212");
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery("select oa_number from distribution_outage");
		while (rs.next()) {
			String oaNo = rs.getString("oa_number");
			Statement stat2 = con.createStatement();
			ResultSet rsCD = stat2.executeQuery(String.format("select CONTRACT_DEFINATION_UID from contract_defination where APPLICATION_NUMBER_ALIAS = '%s' order by issue_date desc", oaNo));
			Integer cdID = null;
			while (rsCD.next()) {
				cdID = rsCD.getInt("CONTRACT_DEFINATION_UID");
				break;
			}
			stat2.executeUpdate(String.format("update distribution_outage set CONTRACT_DEFINATION_UID = %d where oa_number = '%s'", cdID, oaNo));
		}
	}
}
