package maven;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Hibernate {
	public static void main(String[] args) throws Exception {
		var config = new Configuration();
		config.addProperties(null);
		SessionFactory sf = HBUtils.sessionFactory();
		System.out.println(sf);
	}
}
