package betterBanking;

// IMPORT STATEMENTS
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

/**
 * This class defines a BetterBanking Client object, and
 * implements various methods to manipulate this Client.
 * @author Tristan Parry
 * @version 1.0
 */
public class Client {
	// VARIABLE INITIALIZATIONS
	private String firstName, lastName, username, password, fileName;
	
	/**
	 * This constructor creates a BetterBanking Client with a
	 * specified first name, last name, username, and password.
	 * @param clientInfo List<String> used to read the required Client parameters from an associated database file
	 */
	public Client(List<String> clientInfo) {
		this.firstName = clientInfo.get(0);
		this.lastName = clientInfo.get(1);
		this.username = clientInfo.get(2);
		this.password = clientInfo.get(3);
		this.fileName = "clientFiles/" + this.username + ".txt";
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
	 * @throws IOException Exception as a result of I/O failure
	 */
	public boolean updateAccounts(String accountTypeArg) throws IOException {
		try {
			if ((accountTypeArg.equals(String.valueOf(Files.readAllLines(Paths.get(this.fileName)).get(4)))) ||
				(accountTypeArg.equals(String.valueOf(Files.readAllLines(Paths.get(this.fileName)).get(5)))) ||
				(accountTypeArg.equals(String.valueOf(Files.readAllLines(Paths.get(this.fileName)).get(6))))) {
				return true;
			} else { return false; }
		} catch (NoSuchFileException e) { return false; }
	}
}