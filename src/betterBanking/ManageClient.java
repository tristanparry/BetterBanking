package betterBanking;

// IMPORT STATEMENTS
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

/**
 * This class implements various methods to manage a BetterBanking Client account.
 * @author Tristan Parry
 * @version 1.0
 */
public class ManageClient {
	/**
	 * This method requests user input for first/last name, username,
	 * and password, and creates a BetterBanking Client account from these parameters.
	 * @throws IOException Exception as a result of I/O failure
	 */
	public static void createClient() throws IOException {
		// VARIABLE INITIALIZATIONS
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		ArrayList<String> clientInfo = new ArrayList<String>(Arrays.asList("ACCOUNT1", "ACCOUNT2", "ACCOUNT3", "0.00"));
		String temp;
		File clientFile;
		
		// USER INPUT FOR FIRST NAME
		System.out.println("NEW CLIENT:\n-----------");
		while (true) {
			System.out.print("Enter your first name: ");
			temp = scan.nextLine();
			if (temp.isBlank()) {
				System.out.println("An error occurred/your input was invalid. Try again.");
				continue;
			} else {
				clientInfo.add(clientInfo.size() - 4, Character.toUpperCase(temp.charAt(0)) + temp.substring(1).toLowerCase());
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
				clientInfo.add(clientInfo.size() - 4, Character.toUpperCase(temp.charAt(0)) + temp.substring(1).toLowerCase());
				break;
			}
		}
		// USER INPUT FOR USERNAME
		externalLoop: while (true) {
			System.out.print("Enter your username (CASE SENSITIVE): ");
			temp = scan.nextLine();
			if (temp.isBlank()) {
				System.out.println("An error occurred/your input was invalid. Try again.");
				continue;
			} else {
				File clientFilesFolder = new File("clientFiles/");
				File[] clientFilesArray = clientFilesFolder.listFiles();
				for (File i : clientFilesArray) {
				    if (i.isFile()) {
				    	if (((temp + ".txt").toLowerCase().equals(i.getName().toLowerCase())) || (temp.equals(Files.readAllLines(Paths.get(i.getPath())).get(2)))) {
				    		System.out.println("Username already exists. Enter a different username.");
				    		continue externalLoop;
				    	}
				    }
				}
				clientInfo.add(clientInfo.size() - 4, temp);
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
				clientInfo.add(clientInfo.size() - 4, temp);
				break;
			}
		}
		System.out.println("\n\n\n");
		
		// CREATE + WRITE TO .TXT FILE
		clientFile = new File("clientFiles/" + clientInfo.get(2) + ".txt");
		PrintWriter pw = new PrintWriter(clientFile);
		for (String i : clientInfo) {
			pw.println(i);
		}
		pw.close();
	}
	
	
	
	/**
	 * This method accesses/deletes a BetterBanking Client account, as well as its associated Account files.
	 * @param c Client object used to access/delete the corresponding BetterBanking Client account
	 * @throws IOException Exception as a result of I/O failure
	 */
	public static void deleteClient(Client c) throws IOException {
		// VARIABLE INITIALIZATIONS
		File clientFile = new File("clientFiles/" + c.getUsername() + ".txt"),
				chequingFile = new File("accountFiles/" + c.getUsername() + "_chequing.txt"),
				savingsFile = new File("accountFiles/" + c.getUsername() + "_savings.txt"),
				investmentFile = new File("accountFiles/" + c.getUsername() + "_investment.txt");
		
		// DELETE THE CLIENT'S ACCOUNT FILES
		if (c.updateAccounts("Chequing")) { chequingFile.delete(); }
		if (c.updateAccounts("Savings")) { savingsFile.delete(); }
		if (c.updateAccounts("Investment")) { investmentFile.delete(); }
		
		// DELETE THE CLIENT FILE
		clientFile.delete();
		
		// TERMINATE THE PROGRAM
		System.out.println("\n\n\n\nThe client and all of its associated accounts have been deleted. Your session has now been ended.");
		System.exit(0);
	}
	
	
	
	/**
	 * This method requests user input for username and password, accesses a BetterBanking
	 * Client file, and creates a Client object to manipulate in the program execution.
	 * @throws IOException Exception as a result of I/O failure
	 */
	public static void loginClient() throws IOException {
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
			try { usernameCheck = tempUsername.equals(Files.readAllLines(Paths.get("clientFiles/" + tempUsername + ".txt")).get(2)); } catch (NoSuchFileException e) {}
			System.out.print("Enter your password (CASE SENSITIVE): ");
			tempPassword = scan.nextLine();
			try { passwordCheck = tempPassword.equals(Files.readAllLines(Paths.get("clientFiles/" + tempUsername + ".txt")).get(3)); } catch (NoSuchFileException e) {}
			
			// CHECK USERNAME + PASSWORD COMBINATION
			if (usernameCheck && passwordCheck) { break; }
			else {
				System.out.println("Invalid username or password. Try again.\n");
				continue;
			}
		}
		
		// CREATE A CLIENT OBJECT
		Client newClient = new Client(Files.readAllLines(Paths.get("clientFiles/" + tempUsername + ".txt")));
		ManageClient.clientOptions(newClient);
	}
	
	
	
	/**
	 * This method requests user input for the manipulation of a BetterBanking
	 * Client, allowing the user to create or access a BetterBanking Account.
	 * @param c Client object used to access the corresponding BetterBanking Client account
	 * @throws IOException Exception as a result of I/O failure
	 */
	public static void clientOptions(Client c) throws IOException {
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