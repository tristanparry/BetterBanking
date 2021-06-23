package betterBankingMaven;

// IMPORT STATEMENTS
import org.bson.Document;

/**
* This class defines a BetterBanking Client object, and
* implements various methods to manipulate this Client.
* @author Tristan Parry
* @version 1.0
*/
public class Client {
	// VARIABLE INITIALIZATIONS
	private String firstName, lastName, username, password;
	
	/**
	 * This constructor creates a BetterBanking Client with a
	 * specified first name, last name, username, and password.
	 * @param clientInfo Document used to read the required Client parameters from an associated database file
	 */
	public Client(Document clientInfo) {
		this.firstName = clientInfo.getString("FirstName");
		this.lastName = clientInfo.getString("LastName");
		this.username = clientInfo.getString("Username");
		this.password = clientInfo.getString("Password");
	}
	
	/**
	 * This accessor method returns the Client object's username instance field.
	 * @return The Client's username field value
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * This method returns a formatted String of the Client object's field values.
	 */
	public String toString() {
		return "CLIENT DETAILS:\n----------------\n"
				+ "Name: " + this.firstName + " " + this.lastName + "\n"
				+ "Username: " + this.username + "\n"
				+ "Password: " + this.password;
	}
	
	/**
	 * This method checks if a BetterBanking Client holds a specific Account type.
	 * @param accountTypeArg String Account type to read from the Client database file
	 * @return The validity of the passed Account type for the Client object
	 */
	public boolean updateAccounts(String accountTypeArg) {
		return Utilities.connectToMongoDB().getCollection("clientFiles").find(new Document("Username", this.username)).first().getBoolean(accountTypeArg);
	}
}