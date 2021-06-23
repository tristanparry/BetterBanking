package betterBankingMaven;

//IMPORT STATEMENTS
import java.text.DecimalFormat;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
* This class implements various recurring utilities for the BetterBanking software.
* @author Tristan Parry
* @version 1.0
*/
public class Utilities {
	/**
	 * This method reads from the BetterBanking database, and
	 * rewrites specified lines from the MongoDB database.
	 * @param collection Collection parsed from the MongoDB internal database
	 * @param updateDoc MongoDB Document to update
	 * @param key String key to update in the MongoDB Document
	 * @param value String value to update in the MongoDB Document
	 */
	public static void writeToDatabase(String collection, Document updateDoc, String key, Object value) {
		connectToMongoDB().getCollection(collection).updateOne(updateDoc, new Document("$set", new Document(key, value)));
	}
	
	
	
	/**
	 * This method reads from a user's input, and effectively
	 * returns a formatted double for use in Account methods.
	 * @param stringArg String to read a user's monetary input
	 * @return A formatted double to be used in BetterBanking Account methods
	 */
	public static double readDoubleFrom(String stringArg) {
		DecimalFormat df = new DecimalFormat("#0.##");
		double tempDouble = 0;
		try { tempDouble = Double.parseDouble(df.format(Double.valueOf(stringArg))); } catch (NumberFormatException e) { tempDouble = 0; }
		return tempDouble;
	}
	
	
	/**
	 * This method connects to/returns the BetterBanking MongoDB database
	 * (MongoDatabase) on the BetterBanking hosted cluster (MongoDB Atlas)
	 * @return The MongoDB BetterBanking database
	 */
	public static MongoDatabase connectToMongoDB() {
		// CONNECT APPLICATION TO MONGODB DATABASE
		MongoClient mongoClient = new MongoClient(new MongoClientURI(System.getenv("MONGO_URI")));
		MongoDatabase database = mongoClient.getDatabase("betterBanking");
		return database;
	}
}