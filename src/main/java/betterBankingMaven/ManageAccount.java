package betterBankingMaven;

// IMPORT STATEMENTS
import java.util.Scanner;
import org.bson.Document;
import java.util.ArrayList;
import java.text.DecimalFormat;

/**
* This class implements various methods to manage a BetterBanking Account (Chequing/Savings/Investment).
* @author Tristan Parry
* @version 1.0
*/
public class ManageAccount {
	/**
	 * This method requests user input for a valid Account type (that is not
	 * currently in use), and creates a BetterBanking Account from this parameter.
	 * @param c Client object used to access the corresponding BetterBanking Client account
	 */
	public static void createAccount(Client c) {
		// VARIABLE INITIALIZATIONS
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		Document accountInfo = new Document(), updateDoc = Utilities.connectToMongoDB().getCollection("clientFiles").find(new Document("Username", c.getUsername())).first();
		ArrayList<String> validAccounts = new ArrayList<String>();
		String formattedAccounts, temp;
		
		// CHECKING CREATED CLIENT ACCOUNTS
		if (!c.updateAccounts("Chequing")) { validAccounts.add("C - Chequing"); }
		if (!c.updateAccounts("Savings")) { validAccounts.add("S - Savings"); }
		if (!c.updateAccounts("Investment")) { validAccounts.add("I - Investment"); }
		formattedAccounts = validAccounts.toString().replace("[", "").replace("]", "");
		
		// USER INPUT FOR ACCOUNT TYPE
		accountInfo.append("Username", c.getUsername());
		accountInfo.append("AccountBalance", 0.00);
		System.out.println("NEW ACCOUNT:\n------------");
		while (validAccounts.size() >= 1) {
			System.out.print("Enter your preferred account type [" + formattedAccounts + "]: ");
			temp = scan.nextLine().toLowerCase();
			
			if (temp.isBlank()) {
				System.out.println("An error occurred/your input was invalid. Try again.");
				continue;
			}
			if (temp.equals("c") && !c.updateAccounts("Chequing")) {
				accountInfo.append("AccountType", "Chequing");
				accountInfo.append("InterestRate", 0.03);
				accountInfo.append("WrittenCheques", new ArrayList<Object>());
				Utilities.writeToDatabase("clientFiles", updateDoc, "Chequing", true);
				break;
			} else if (temp.equals("s") && !c.updateAccounts("Savings")) {
				accountInfo.append("AccountType", "Savings");
				accountInfo.append("InterestRate", 0.05);
				Utilities.writeToDatabase("clientFiles", updateDoc, "Savings", true);
				break;
			} else if (temp.equals("i") && !c.updateAccounts("Investment")) {
				accountInfo.append("AccountType", "Investment");
				accountInfo.append("InterestRate", 0.10);
				accountInfo.append("PurchasedStocks", new ArrayList<Object>());
				Utilities.writeToDatabase("clientFiles", updateDoc, "Investment", true);
				break;
			} else {
				System.out.println("An error occurred/your input was invalid. Try again.");
				continue;
			}
		}
		System.out.println();
		
		// CREATE + ADD TO MONGODB
		Utilities.connectToMongoDB().getCollection("accountFiles").insertOne(accountInfo);
	}
	
	
	
	/**
	 * This method requests user input for an existing Account (assigned to a
	 * BetterBanking Client), and accesses this Account to perform internal operations.
	 * @param c Client object used to access the corresponding BetterBanking Client account
	 */
	public static void accessAccount(Client c) {
		// VARIABLE INITIALIZATIONS
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		ArrayList<String> validAccounts = new ArrayList<String>();
		String formattedAccounts, temp;
		
		// CHECKING CREATED CLIENT ACCOUNTS
		if (c.updateAccounts("Chequing")) { validAccounts.add("C - Chequing"); }
		if (c.updateAccounts("Savings")) { validAccounts.add("S - Savings"); }
		if (c.updateAccounts("Investment")) { validAccounts.add("I - Investment"); }
		formattedAccounts = validAccounts.toString().replace("[", "").replace("]", "");
		
		// USER INPUT FOR ACCOUNT ACCESS
		System.out.println("ACCOUNT ACCESS:\n---------------");
		while (validAccounts.size() >= 1) {
			System.out.print("Enter the account to access [" + formattedAccounts + "]: ");
			temp = scan.nextLine().toLowerCase();
			
			if (temp.isBlank()) {
				System.out.println("An error occurred/your input was invalid. Try again.");
				continue;
			}
			if (temp.equals("c") && c.updateAccounts("Chequing")) {
				Account clientChequingAccount = new Chequing(Utilities.connectToMongoDB().getCollection("accountFiles").find(new Document("Username", c.getUsername()).append("AccountType", "Chequing")).first());
				System.out.println("\n\n\n");
				manipulateAccount(c, clientChequingAccount);
			} else if (temp.equals("s") && c.updateAccounts("Savings")) {
				Account clientSavingsAccount = new Savings(Utilities.connectToMongoDB().getCollection("accountFiles").find(new Document("Username", c.getUsername()).append("AccountType", "Savings")).first());
				System.out.println("\n\n\n");
				manipulateAccount(c, clientSavingsAccount);
			} else if (temp.equals("i") && c.updateAccounts("Investment")) {
				Account clientInvestmentAccount = new Investment(Utilities.connectToMongoDB().getCollection("accountFiles").find(new Document("Username", c.getUsername()).append("AccountType", "Investment")).first());
				System.out.println("\n\n\n");
				manipulateAccount(c, clientInvestmentAccount);
			} else {
				System.out.println("An error occurred/your input was invalid. Try again.");
				continue;
			}
		}
	}
	
	
	
	/**
	 * This method requests user input for a valid BetterBanking Account operation,
	 * and performs the manipulation on the provided Account object.
	 * @param c Client object used to access the corresponding BetterBanking Client account
	 * @param a Account object used to access the corresponding BetterBanking Account (Chequing/Savings/Investment)
	 */
	public static void manipulateAccount(Client c, Account a) {
		// VARIABLE INITIALIZATIONS
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		ArrayList<String> validAccounts = new ArrayList<String>();
		String temp, formattedAccounts;
		double tempDouble = 0;
		Account clientAccount = null;
		DecimalFormat df = new DecimalFormat("#0.##");
		
		// USER INPUT FOR ACCOUNT ACCESS
		System.out.println(a.toString() + "\n\n");
		while (true) {
			System.out.print("Choose an option [1 - Deposit, 2 - Withdraw, 3 - Balance, 4 - Interest Rate,\n\t\t  5 - Total Funds, ");
			if (a instanceof Chequing) {
				System.out.print("6 - Write Cheque, ");
			} else if (a instanceof Savings) {
				System.out.print("6 - Move Money, ");
			} else if (a instanceof Investment) {
				System.out.print("6 - Buy Stock, ");
			}
			System.out.print("7 - Account Details, B - Back, E - Exit]: ");
			temp = scan.nextLine().toLowerCase();
			if (temp.isBlank()) {
				System.out.println("Invalid input. Try again.\n\n\n");
				continue;
			}
			
			// SWITCH/CASE STATEMENT DEFINING ALL POSSIBLE ACCOUNT OPERATIONS
			switch(temp) {
			case "b":
				ManageClient.clientOptions(c);
			case "e":
				a.applyInterest();
				System.out.println("\n\n\n\nThank you for choosing BetterBanking. Your session has now been ended.");
				System.exit(0);
			case "1":
				System.out.print("\n\n\nEnter an amount to deposit: ");
				tempDouble = Utilities.readDoubleFrom(scan.nextLine());
				System.out.println(a.deposit(tempDouble) ? "Deposit successful.\n\n" : "Cannot deposit funds. Try a valid amount.\n\n");
				break;
			case "2":
				System.out.print("\n\n\nEnter an amount to withdraw: ");
				tempDouble = Utilities.readDoubleFrom(scan.nextLine());
				System.out.println(a.withdraw(tempDouble) ? "Withdrawal successful.\n\n" : "Cannot withdraw funds. Try a valid amount.\n\n");
				break;
			case "3":
				System.out.println("\n\n\nYour " + a.getAccountType() + " account balance is: $" + df.format(a.balance()) + "\n\n");
				break;
			case "4":
				System.out.println("\n\n\nYour " + a.getAccountType() + " account interest rate is: " + a.getInterestRate() + "%\n\n");
				break;
			case "5":
				System.out.println("\n\n\nYour client's total funds are: $" + df.format(a.getClientFunds()) + "\n\n");
				break;
			case "6":
				System.out.println("\n\n\n");
				if (a instanceof Chequing) {
					System.out.print("\n\n\nEnter the cheque recipient's name: ");
					temp = scan.nextLine();
					System.out.print("Enter an amount to write to cheque: ");
					tempDouble = Utilities.readDoubleFrom(scan.nextLine());
					System.out.println(((Chequing) a).writeCheque(temp, tempDouble) ? "Cheque written successfully.\n\n" : "Cannot write cheque. Try a valid amount.\n\n");
				} else if (a instanceof Savings) {
					if (c.updateAccounts("Chequing")) { validAccounts.add("C - Chequing"); }
					if (c.updateAccounts("Investment")) { validAccounts.add("I - Investment"); }
					formattedAccounts = validAccounts.toString().replace("[", "").replace("]", "");
					
					if (validAccounts.size() >= 1) {
						System.out.print("\n\n\nEnter the receiving account [" + formattedAccounts + "]: ");
						temp = scan.nextLine().toLowerCase();
						System.out.print("Enter the amount to move: ");
						tempDouble = Utilities.readDoubleFrom(scan.nextLine());
						if (temp.equals("c") && c.updateAccounts("Chequing")) {
							clientAccount = new Chequing(Utilities.connectToMongoDB().getCollection("accountFiles").find(new Document("Username", c.getUsername()).append("AccountType", "Chequing")).first());
							System.out.println(((Savings) a).moveMoney(tempDouble, clientAccount) ? "Money transferred successfully.\n\n" : "Cannot transfer funds. Try a valid amount.\n\n");
						}
						else if (temp.equals("i") && c.updateAccounts("Investment")) {
							clientAccount = new Investment(Utilities.connectToMongoDB().getCollection("accountFiles").find(new Document("Username", c.getUsername()).append("AccountType", "Investment")).first());
							System.out.println(((Savings) a).moveMoney(tempDouble, clientAccount) ? "Money transferred successfully.\n\n" : "Cannot transfer funds. Try a valid amount.\n\n");
						} else {
							System.out.println("Cannot transfer funds.\n\n");
						}
						validAccounts.clear();
					} else {
						System.out.println("There are no accounts to transfer funds to.\n\n");
						continue;
					}
				} else if (a instanceof Investment) {
					System.out.print("\n\n\nEnter the company name: ");
					temp = scan.nextLine();
					System.out.print("Enter an amount to invest: ");
					tempDouble = Utilities.readDoubleFrom(scan.nextLine());
					System.out.println(((Investment) a).buyStock(temp, tempDouble) ? "Stock purchased successfully.\n\n" : "Cannot invest funds. Try a valid amount.\n\n");
				}
				break;
			case "7":
				System.out.println("\n\n\n" + a.toString() + "\n\n");
				break;
			default:
				System.out.println("\n\nInvalid input. Try again.\n\n\n");
				continue;
			}
		}
	}
}