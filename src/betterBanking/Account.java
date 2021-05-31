package betterBanking;

// IMPORT STATEMENTS
import java.util.List;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;

/**
 * This abstract class defines a BetterBanking Account object,
 * and implements various methods to manipulate this Account.
 * @author Tristan Parry
 * @version 1.0
 */
public abstract class Account {
	// VARIABLE INITIALIZATIONS
	protected String accountType, username, fileName;
	protected double accountBalance, accountInterestRate;
	protected static double clientFunds;
	protected DecimalFormat df = new DecimalFormat("#0.##");
	
	/**
	 * This constructor creates a BetterBanking Account with a specified
	 * account type, Client username, balance, interest rate, and file path.
	 * @param accountInfo List<String> used to read the required Account parameters from an associated database file
	 * @throws IOException Exception as a result of I/O failure
	 */
	public Account(List<String> accountInfo) throws IOException {
		this.accountType = accountInfo.get(0);
		this.username = accountInfo.get(1);
		this.accountBalance = Double.parseDouble(df.format(Double.valueOf(accountInfo.get(2))));
		this.accountInterestRate = Double.parseDouble(df.format(Double.valueOf(accountInfo.get(3))));
		this.fileName = "accountFiles/" + this.username + "_" + this.accountType.toLowerCase() + ".txt";
		clientFunds = Double.parseDouble(df.format(Double.valueOf(Files.readAllLines(Paths.get("clientFiles/" + this.username + ".txt")).get(7))));
	}
	
	/**
	 * This accessor method returns the Account object's accountType instance field.
	 * @return The Account's accountType field value
	 */
	public String getAccountType() {
		return this.accountType;
	}
	
	/**
	 * This mutator method calculates and applies a user deposit into a BetterBanking
	 * Account, and returns the validity of the operation (success/failure).
	 * @param depositArg Double value being deposited into the Account
	 * @return The success/failure of the deposit method
	 * @throws IOException Exception as a result of I/O failure
	 */
	public boolean deposit(double depositArg) throws IOException {
		if ((depositArg > 0) && (!Double.isInfinite(depositArg)) && (!Double.isNaN(depositArg)) && (!String.valueOf(depositArg).isBlank())) {
			this.accountBalance += depositArg;
			clientFunds += depositArg;
			Utilities.writeToDatabase(Paths.get(this.fileName), 2, String.valueOf(df.format(Double.valueOf(this.accountBalance))));
			Utilities.writeToDatabase(Paths.get("clientFiles/" + this.username + ".txt"), 7, String.valueOf(df.format(Double.valueOf(clientFunds))));
			return true;
		} else { return false; }
	}
	
	/**
	 * This mutator method calculates and applies a user withdrawal from a BetterBanking
	 * Account, and returns the validity of the operation (success/failure).
	 * @param withdrawArg Double value being withdrawn from the Account
	 * @return The success/failure of the withdraw method
	 * @throws IOException Exception as a result of I/O failure
	 */
	public boolean withdraw(double withdrawArg) throws IOException {
		if ((withdrawArg <= this.accountBalance) && (withdrawArg > 0)  && (!Double.isInfinite(withdrawArg)) && (!Double.isNaN(withdrawArg)) && (!String.valueOf(withdrawArg).isBlank())) {
			this.accountBalance -= withdrawArg;
			clientFunds -= withdrawArg;
			Utilities.writeToDatabase(Paths.get(this.fileName), 2, String.valueOf(df.format(Double.valueOf(this.accountBalance))));
			Utilities.writeToDatabase(Paths.get("clientFiles/" + this.username + ".txt"), 7, String.valueOf(df.format(Double.valueOf(clientFunds))));
			return true;
		} else { return false; }
	}
	
	/**
	 * This accessor method returns the Account object's accountBalanace instance field.
	 * @return The Account's accountBalanace field value
	 */
	public double balance() {
		return this.accountBalance;
	}
	
	/**
	 * This accessor method returns the Account object's accountInterestRate instance field.
	 * @return The Account's accountInterestRate field value
	 */
	public double getInterestRate() {
		return this.accountInterestRate;
	}
	
	/**
	 * This accessor method returns the Account clientFunds static field.
	 * @return The Account clientFunds static field value
	 */
	public double getClientFunds() {
		return clientFunds;
	}
	
	/**
	 * This mutator method calculates/applies an Account object's interest
	 * rate, and writes the resulting balances to corresponding database files
	 * @throws IOException Exception as a result of I/O failure
	 */
	public void applyInterest() throws IOException {
		this.accountBalance += this.accountBalance * (this.accountInterestRate / 100);
		clientFunds += this.accountBalance * (this.accountInterestRate / 100);
		Utilities.writeToDatabase(Paths.get(this.fileName), 2, String.valueOf(df.format(Double.valueOf(this.accountBalance))));
		Utilities.writeToDatabase(Paths.get("clientFiles/" + this.username + ".txt"), 7, String.valueOf(df.format(Double.valueOf(clientFunds))));
	}
	
	/**
	 * This method returns a formatted String of the Account object's field values.
	 */
	public String toString() {
		return this.username.toUpperCase() + "'S " + this.accountType.toUpperCase() + " ACCOUNT:\n"
				+ "---------------------------------------\n"
				+ "Balance: $" + df.format(Double.valueOf(this.accountBalance)) + "\n"
				+ "Interest Rate: " + this.accountInterestRate + "%\n"
				+ "Total Client Funds: $" + df.format(Double.valueOf(clientFunds));
	}
}



/**
 * This class defines a BetterBanking Chequing object, inheriting from the parent
 * Account class and implementing various methods to manipulate this object.
 * @author Tristan Parry
 * @version 1.0
 */
class Chequing extends Account {
	// VARIABLE INITIALIZATIONS
	private String writtenCheques = "";
	
	/**
	 * This constructor creates a Chequing Account with a specified
	 * account type, Client username, balance, interest rate, and file path.
	 * @param accountInfo List<String> used to read the required Account parameters from an associated database file
	 * @throws IOException Exception as a result of I/O failure
	 */
	public Chequing(List<String> accountInfo) throws IOException {
		super(accountInfo);
		for (int i = 4; i < Files.lines(Paths.get(this.fileName), Charset.defaultCharset()).count(); i++) {
			this.writtenCheques += Files.readAllLines(Paths.get(this.fileName)).get(i) + "\n\t\t ";
		}
	}
	
	/**
	 * This mutator method writes a cheque from the Chequing Account, records the details to
	 * a corresponding database file, and returns the validity of the operation (success/failure).
	 * @param recipientArg String name of the written cheque's recipient
	 * @param amountArg Double value being written to a cheque/withdrawn from the Account
	 * @return The success/failure of the writeCheque method
	 * @throws IOException Exception as a result of I/O failure
	 */
	public boolean writeCheque(String recipientArg, double amountArg) throws IOException {
		if ((!recipientArg.isBlank()) && (amountArg <= this.accountBalance) && (amountArg > 0) && (!Double.isInfinite(amountArg)) && (!Double.isNaN(amountArg)) && (!String.valueOf(amountArg).isBlank())) {
			withdraw(amountArg);
			this.writtenCheques += recipientArg.toUpperCase() + ": $" + df.format(Double.valueOf(amountArg)) + "\n\t\t ";
			Files.write(Paths.get(this.fileName), (recipientArg.toUpperCase() + ": $" + df.format(Double.valueOf(amountArg))).getBytes(), StandardOpenOption.APPEND);
			// https://cutt.ly/TnfS0hK - Encodes the passed String as a byte sequence, and uses StandardOpenOption.APPEND to write to the END of the file
			return true;
		} else { return false; }
	}
	
	/**
	 * This method returns a formatted String of the Chequing Account object's field values (includes written cheques).
	 */
	public String toString() {		
		return this.username.toUpperCase() + "'S " + this.accountType.toUpperCase() + " ACCOUNT:\n"
				+ "---------------------------------------\n"
				+ "Balance: $" + df.format(Double.valueOf(this.accountBalance)) + "\n"
				+ "Interest Rate: " + this.accountInterestRate + "%\n"
				+ "Total Client Funds: $" + df.format(Double.valueOf(clientFunds)) + "\n"
				+ "Written Cheques: " + this.writtenCheques;
	}
}



/**
 * This class defines a BetterBanking Savings object, inheriting from the parent
 * Account class and implementing various methods to manipulate this object.
 * @author Tristan Parry
 * @version 1.0
 */
class Savings extends Account {
	/**
	 * This constructor creates a Savings Account with a specified
	 * account type, Client username, balance, interest rate, and file path.
	 * @param accountInfo List<String> used to read the required Account parameters from an associated database file
	 * @throws IOException Exception as a result of I/O failure
	 */
	public Savings(List<String> accountInfo) throws IOException {
		super(accountInfo);
	}
	
	/**
	 * This mutator method transfers money from a Savings Account to a Chequing/
	 * Investment account, and returns the validity of the operation (success/failure).
	 * @param amountArg Double value being transferred from the Savings Account
	 * @param a Account object to transfer funds to (Chequing/Investment)
	 * @return The success/failure of the moveMoney method
	 * @throws IOException Exception as a result of I/O failure
	 */
	public boolean moveMoney(double amountArg, Account a) throws IOException {
		if ((amountArg <= this.accountBalance) && (amountArg > 0) && (!Double.isInfinite(amountArg)) && (!Double.isNaN(amountArg)) && (!String.valueOf(amountArg).isBlank())) {
			withdraw(amountArg);
			a.deposit(amountArg);
			return true;
		} else { return false; }
	}
}



/**
 * This class defines a BetterBanking Investment object, inheriting from the parent
 * Account class and implementing various methods to manipulate this object.
 * @author Tristan Parry
 * @version 1.0
 */
class Investment extends Account {
	// VARIABLE INITIALIZATIONS
	private String purchasedStocks = "";
	
	/**
	 * This constructor creates an Investment Account with a specified
	 * account type, Client username, balance, interest rate, and file path.
	 * @param accountInfo List<String> used to read the required Account parameters from an associated database file
	 * @throws IOException Exception as a result of I/O failure
	 */
	public Investment(List<String> accountInfo) throws IOException {
		super(accountInfo);
		for (int i = 4; i < Files.lines(Paths.get(this.fileName), Charset.defaultCharset()).count(); i++) {
			this.purchasedStocks += Files.readAllLines(Paths.get(this.fileName)).get(i) + "\n\t\t  ";
		}
	}
	
	/**
	 * This mutator method buys a stock through the Investment Account, records the details to
	 * a corresponding database file, and returns the validity of the operation (success/failure).
	 * @param companyName String name of the purchased stock's company
	 * @param investmentAmountArg Double value being bought in stocks/withdrawn from the Account
	 * @return The success/failure of the buyStock method
	 * @throws IOException Exception as a result of I/O failure
	 */
	public boolean buyStock(String companyName, double investmentAmountArg) throws IOException {
		if ((!companyName.isBlank()) && (investmentAmountArg <= this.accountBalance) && (investmentAmountArg > 0) && (!Double.isInfinite(investmentAmountArg)) && (!Double.isNaN(investmentAmountArg)) && (!String.valueOf(investmentAmountArg).isBlank())) {
			withdraw(investmentAmountArg);
			this.purchasedStocks += companyName.toUpperCase() + ": $" + df.format(Double.valueOf(investmentAmountArg)) + "\n\t\t  ";
			Files.write(Paths.get(this.fileName), (companyName.toUpperCase() + ": $" + df.format(Double.valueOf(investmentAmountArg))).getBytes(), StandardOpenOption.APPEND);
			return true;
		} else { return false; }
	}
	
	/**
	 * This method returns a formatted String of the Investment Account object's field values (includes purchased stocks).
	 */
	public String toString() {		
		return this.username.toUpperCase() + "'S " + this.accountType.toUpperCase() + " ACCOUNT:\n"
				+ "---------------------------------------\n"
				+ "Balance: $" + df.format(Double.valueOf(this.accountBalance)) + "\n"
				+ "Interest Rate: " + this.accountInterestRate + "%\n"
				+ "Total Client Funds: $" + df.format(Double.valueOf(clientFunds)) + "\n"
				+ "Purchased Stocks: " + this.purchasedStocks;
	}
}
