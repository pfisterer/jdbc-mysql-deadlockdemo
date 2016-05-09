package de.dhbw.dbtechnik;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import de.uniluebeck.itm.util.logging.LogLevel;
import de.uniluebeck.itm.util.logging.Logging;

public class Main {

	static {
		Logging.setLoggingDefaults(LogLevel.DEBUG, "[%-5p; %c{1}::%M] %m%n");
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println(
					"Supply arguments jdbc-url user password (e.g., 'jdbc:mysql://localhost/beispieldatenbank?verifyServerCertificate=false' 'root' 'pass'");
			System.exit(1);
		}

		String url = args[0];
		String user = args[1];
		String pw = args[2];

		new Thread(() -> {
			try {
				Connection databaseConnection1 = DriverManager.getConnection(url, user, pw);
				databaseConnection1.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				databaseConnection1.setAutoCommit(false);
				Statement neuesStatementCon1 = databaseConnection1.createStatement();

				neuesStatementCon1.executeQuery("SELECT * FROM Artikel;");
				System.out.println("1a");
				Thread.sleep(100);
				neuesStatementCon1.executeUpdate("UPDATE Filiale set Ort='Stuttgart' where Filialnummer='F1';");
				System.out.println("1b");

				databaseConnection1.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> {
			try {
				Connection databaseConnection2 = DriverManager.getConnection(url, user, pw);
				databaseConnection2.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				databaseConnection2.setAutoCommit(false);
				Statement neuesStatementCon2 = databaseConnection2.createStatement();

				neuesStatementCon2.executeQuery("SELECT * FROM Filiale;");
				System.out.println("2a");
				Thread.sleep(200);
				neuesStatementCon2.executeUpdate("UPDATE Artikel set Preis='1200' where Artikelnummer='A1';");
				System.out.println("2b");
				databaseConnection2.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

	}

}
