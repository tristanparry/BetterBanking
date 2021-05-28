package betterBanking;

// IMPORT STATEMENTS
import java.util.Scanner;
import java.io.*;

/** This class implements a main run method for the BetterBanking software.
 * @author Tristan Parry
 * @version 1.0
*/
public class Main {
	/**
	 * This method displays a welcome message to the user, and allows
	 * them to navigate from the main menu of the BetterBanking software.
	 * @param args String[] arguments passed as part of execution
	 * @throws IOException Exception as a result of I/O failure
	 */
	public static void main(String[] args) throws IOException {
		// VARIABLE INITIALIZATIONS
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String temp;
		
		// MAIN PRORGAM RUN LOOP
		System.out.println("---------------------------------------------------\n|            Welcome to BetterBanking!            |\n---------------------------------------------------\n\n");
		while (true) {
			System.out.print("Would you like to Create a new client, or Login to a client? [C - Create, L - Login, E - Exit]: ");
			temp = scan.nextLine().toLowerCase();
			
			if (temp.isBlank()) {
				System.out.println("Invalid input. Try again.\n\n\n");
				continue;
			}
			if (temp.equals("e")) {
				System.out.println("\n\n\n\nThank you for choosing BetterBanking. Your session has now been ended.");
				System.exit(0);
			} else if (temp.equals("c")) {
				System.out.println("\n\n\n");
				ManageClient.createClient();
				continue;
			} else if (temp.equals("l")) {
				System.out.println("\n\n\n");
				ManageClient.loginClient();
			} else {
				System.out.println("Invalid input. Try again.\n\n\n");
				continue;
			}
		}
	}
}