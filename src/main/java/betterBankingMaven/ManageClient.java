package betterBankingMaven;

// IMPORT STATEMENTS
import java.util.Scanner;
import org.bson.Document;
import com.mongodb.client.model.Filters;

/**
* This class implements various methods to manage a BetterBanking Client account.
* @author Tristan Parry
* @version 1.0
*/
public class ManageClient {
	/**
	 * This method requests user input for first/last name, username,
	 * and password, and creates a BetterBanking Client account from these parameters.
	 */
	public static void createClient() {
		// VARIABLE INITIALIZATIONS
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		Document clientInfo = new Document();
		String temp;
		
		// USER INPUT FOR FIRST NAME
		System.out.println("NEW CLIENT:\n-----------");
		while (true) {
			System.out.print("Enter your first name: ");
			temp = scan.nextLine();
			if (temp.isBlank()) {
				System.out.println("An error occurred/your input was invalid. Try again.");
				continue;
			} else {
				clientInfo.append("FirstName", Character.toUpperCase(temp.charAt(0)) + temp.substring(1).toLowerCase());
				break;
			}
		}
		// USER INPUT FOR LAST NAME
		while (true) {
			System.out.print("Enter your last name: ");
			temp = scan.nextLine();
			if (temp.isBlank()) {
				System.out.println("An error occurred/your input was invalid. Try again.");
				continue;
			} else {
				clientInfo.append("LastName", Character.toUpperCase(temp.charAt(0)) + temp.substring(1).toLowerCase());
				break;
			}
		}
		// USER INPUT FOR USERNAME
		while (true) {
			System.out.print("Enter your username (CASE SENSITIVE): ");
			temp = scan.nextLine();
			if (temp.isBlank()) {
				System.out.println("An error occurred/your input was invalid. Try again.");
				continue;
			} else {
				if (Utilities.connectToMongoDB().getCollection("clientFiles").find(new Document("Username", temp)).first() != null) {
					System.out.println("Username already exists. Enter a different username.");
					continue;
				}
				clientInfo.append("Username", temp);
				break;
			}
		}
		// USER INPUT FOR PASSWORD
		while (true) {
			System.out.print("Enter your password (CASE SENSITIVE): ");
			temp = scan.nextLine();
			if (temp.isBlank()) {
				System.out.println("An error occurred/your input was invalid. Try again.");
				continue;
			} else {
				clientInfo.append("Password", temp);
				break;
			}
		}
		System.out.println("\n\n\n");
		clientInfo.append("Chequing", false).append("Savings", false).append("Investment", false).append("ClientFunds", 0.00);
		
		// CREATE + ADD TO MONGODB
		Utilities.connectToMongoDB().getCollection("clientFiles").insertOne(clientInfo);
	}
	
	
	
	/**
	 * This method accesses/deletes a BetterBanking Client account, as well as its associated Account files.
	 * @param c Client object used to access/delete the corresponding BetterBanking Client account
	 */
	public static void deleteClient(Client c) {
		// DELETE THE CLIENT'S ACCOUNT FILES
		Utilities.connectToMongoDB().getCollection("accountFiles").deleteMany(Filters.eq("Username", c.getUsername()));
		
		// DELETE THE CLIENT FILE
		Utilities.connectToMongoDB().getCollection("clientFiles").deleteOne(Filters.eq("Username", c.getUsername()));
		
		// TERMINATE THE PROGRAM
		System.out.println("\n\n\n\nThe client and all of its associated accounts have been deleted. Your session has now been ended.");
		System.exit(0);
	}
	
	
	
	/**
	 * This method requests user input for username and password, accesses a BetterBanking
	 * Client file, and creates a Client object to manipulate in the program execution.
	 */
	public static void loginClient() {
		// VARIABLE INITIALIZATIONS
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String tempUsername = "", tempPassword;
		int loginAttempts = 0;
		boolean usernameCheck = false, passwordCheck = false;
		
		// USER INPUT FOR USERNAME + PASSWORD
		System.out.println("LOGIN:\n------");
		while (loginAttempts <= 4) {
			loginAttempts++;
			if (loginAttempts == 4) {
				System.out.println("\n\n\n\nToo many incorrect login attempts. Your session has now been ended.");
				System.exit(0);
			}
			System.out.print("Enter your username (CASE SENSITIVE): ");
			tempUsername = scan.nextLine();
			try {
				if (Utilities.connectToMongoDB().getCollection("clientFiles").find(new Document("Username", tempUsername)) != null) {
					usernameCheck = true;
				}
			} catch (NullPointerException e) {}
			System.out.print("Enter your password (CASE SENSITIVE): ");
			tempPassword = scan.nextLine();
			try {
				if (Utilities.connectToMongoDB().getCollection("clientFiles").find(new Document("Username", tempUsername)).first().getString("Password").equals(tempPassword)) {
					passwordCheck = true;
				}
			} catch (NullPointerException e) {}
			
			// CHECK USERNAME + PASSWORD COMBINATION
			if (usernameCheck && passwordCheck) { break; }
			else {
				System.out.println("Invalid username or password. Try again.\n");
				continue;
			}
		}
		
		// CREATE A CLIENT OBJECT
		Client newClient = new Client(Utilities.connectToMongoDB().getCollection("clientFiles").find(new Document("Username", tempUsername)).first());
		ManageClient.clientOptions(newClient);
	}
	
	
	
	/**
	 * This method requests user input for the manipulation of a BetterBanking
	 * Client, allowing the user to create or access a BetterBanking Account.
	 * @param c Client object used to access the corresponding BetterBanking Client account
	 */
	public static void clientOptions(Client c) {
		// VARIABLE INITIALIZATIONS
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String temp;
		
		// USER ACTION INPUT
		System.out.println("\n\n\n" + c.toString());
		while (true) {
			if (c.updateAccounts("Chequing") && c.updateAccounts("Savings") && c.updateAccounts("Investment")) {
				System.out.print("\nWould you like to Delete the client, or Access an account? [D - Delete, A - Access, E - Exit]: ");
			} else if (!c.updateAccounts("Chequing") && !c.updateAccounts("Savings") && !c.updateAccounts("Investment")) {
				System.out.print("\nWould you like to Delete the client, or Create an account? [D - Delete, C - Create, E - Exit]: ");
			} else {
				System.out.print("\nWould you like to Delete the client, Create a new account, or Access an account? [D - Delete, C - Create, A - Access, E - Exit]: ");
			}
			temp = scan.nextLine().toLowerCase();
			
			if (temp.isBlank()) {
				System.out.println("Invalid input. Try again.\n\n\n");
				continue;
			}
			if (temp.equals("e")) {
				System.out.println("\n\n\n\nThank you for choosing BetterBanking. Your session has now been ended.");
				System.exit(0);
			} else if (temp.equals("d")) {
				System.out.println("\n\n\n");
				ManageClient.deleteClient(c);
			} else if (temp.equals("c") && (!c.updateAccounts("Chequing") || !c.updateAccounts("Savings") || !c.updateAccounts("Investment"))) {
				System.out.println("\n\n\n");
				ManageAccount.createAccount(c);
			} else if (temp.equals("a") && (c.updateAccounts("Chequing") || c.updateAccounts("Savings") || c.updateAccounts("Investment"))) {
				System.out.println("\n\n\n");
				ManageAccount.accessAccount(c);
			} else {
				System.out.println("Invalid input. Try again.\n\n\n");
				continue;
			}
		}
	}
}