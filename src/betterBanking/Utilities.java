package betterBanking;

// IMPORT STATEMENTS
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements various recurring utilities for the BetterBanking software.
 * @author Tristan Parry
 * @version 1.0
 */
public class Utilities {
	/**
	 * This method reads from the BetterBanking database, and
	 * rewrites specified lines from the given .txt filepath.
	 * @param filepath Path parsed from the BetterBanking internal database
	 * @param desiredLineNumber File line number to overwrite
	 * @param modifiedString String to write to the database file
	 * @throws IOException Exception as a result of I/O failure
	 */
	public static void writeToDatabase(Path filepath, int desiredLineNumber, String modifiedString) throws IOException { // https://cutt.ly/anfHZRf
		// VARIABLE INITIALIZATIONS
		List<String> file = new ArrayList<>(Files.readAllLines(filepath, StandardCharsets.UTF_8)); // Reads the specified file as a List<String>
		
		// PARSE + WRITE FILE
		file.get(desiredLineNumber); // Obtains the user's specified line
		file.set(desiredLineNumber, modifiedString); // Sets the List<String> value at the specified index
		Files.write(filepath, file, StandardCharsets.UTF_8); // Write to the file (uses StandardCharsets.UTF_8 for functionality/.txt Charset)
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
}
