import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/*
 * This Password Generator will generate a password based on the options provided by the user and output the generated password and parameters used to generate the password.
 * Additionally, a randomly generated set of passwords (between 5 and 30) can be generated if specified by the user.
 *
 * @author Ron Bodnar
 * 
 */
public class PasswordGenerator {
	
	private static int passwordLength;
	private static int numberToGenerate;
	
	private static boolean includeSymbols, includeUppercase, includeNumbers;
	
	private static final File SAVE_FILE = new File("passwords.txt");

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		System.out.println("========== Java Password Generator ==========");
		
		int programRunType = -1;
		
		while (!validLength(programRunType, 1, 2)) {
			System.out.println("Generate a single password with options OR run the generator to get multiple passwords using randomized options?");
			System.out.print("(1 = generate single password, 2 = generate multiple random passwords) ");
			
			try {
				programRunType = input.nextInt();
			} catch (InputMismatchException exception) {
				System.out.println("[INVALID INPUT]: Response must be a numerical digit.\n");
				input.next();
				continue;
			}
			
			if (!validLength(programRunType, 1, 2)) {
				System.out.println("[INVALID INPUT]: Response must either be 1 or 2. \"" + programRunType + "\" is out of range.\n");
			}
		}
		
		switch (programRunType) {
			case 1:
				generateSinglePassword(input);
				break;
				
			case 2:
				generateRandomPasswords(input);
				break;
		}
	}

	/**
	 * Generates a user defined number of passwords with random parameters and length.
	 * 
	 * @param input The user input Scanner being used in the program.
	 */
	public static void generateRandomPasswords(Scanner input) {
		while (!validLength(numberToGenerate, 5, 30)) {
			System.out.print("How many passwords should be generated? (enter a number between 5 and 30) ");
			
			try {
				numberToGenerate = input.nextInt();
			} catch (InputMismatchException exception) {
				System.out.println("[INVALID INPUT]: Number of passwords to generate must be numerical digit.\n");
				input.next();
				continue;
			}
			
			if (!validLength(numberToGenerate, 5, 30)) {
				System.out.println("[INVALID INPUT]: Number of passwords must be between 5 and 30 characters. \"" + passwordLength + "\" is out of range.\n");
			}
		}
		
		Random random = new Random();
		
		ArrayList<String> generatedPasswords = new ArrayList<String>();
		
		for (int i = 0; i < numberToGenerate; i++) {
			passwordLength = random.nextInt(6, 20);
			includeUppercase = random.nextBoolean();
			includeSymbols = random.nextInt(3) == 1;
			includeNumbers = random.nextBoolean();
			
			String generatedPassword = getGeneratedPassword();
			
			generatedPasswords.add(generatedPassword);
		}
		
		int index = 0;
		
		System.out.println("Total number of passwords generated: " + generatedPasswords.size());
		
		for (String password : generatedPasswords) {
			System.out.println("Password " + index++ + " (" + password.length() + " chars): " + password);
		}
		
		try {
			CharSequence[] sequence = generatedPasswords.toArray(new CharSequence[generatedPasswords.size()]);
			
			Files.writeString(SAVE_FILE.toPath(), Arrays.toString(sequence).replaceAll(",", "\n"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
			
			System.out.println("Added passwords to file: " + SAVE_FILE.getAbsolutePath());
		} catch(IOException exception) {
			System.out.println("Error writing passwords to file: " + exception.getMessage());
		}
	}
	
	/**
	 * Generates a single password and prompts users for specific parameters to be used in generation.
	 * This includes: password length and including uppercase, digits, and symbols.
	 * 
	 * @param input The user input Scanner being used in the program.
	 */
	public static void generateSinglePassword(Scanner input) {
		while (!validLength(passwordLength, 6, 20)) {
			System.out.print("How many characters should the password be? (6-20 characters) ");
			
			try {
				passwordLength = input.nextInt();
			} catch (InputMismatchException exception) {
				System.out.println("[INVALID INPUT]: Password length must be a numerical digit.\n");
				input.next();
				continue;
			}
			
			if (!validLength(passwordLength, 6, 20)) {
				System.out.println("[INVALID INPUT]: Password length must be between 6 and 20 characters. \"" + passwordLength + "\" is out of range.\n");
			}
		}
		System.out.println();
		input.nextLine();
		
		String response = null;
		
		boolean validInput = false;
		
		while (!validInput) {
			System.out.print("Should the password contain symbols (eg: ? @ ! %)? (Y/N)");
			
			response = input.nextLine();
			
			if (response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("N")) {
				validInput = true;
				includeSymbols = response.equalsIgnoreCase("Y");
			} else {
				System.out.println("[INVALID INPUT]: You must enter 'Y' for yes or 'N' for no.\n");
			}
		}
		System.out.println();

		validInput = false;
		
		while (!validInput) {
			System.out.print("Should the password contain uppercase letters? (Y/N)");
			
			response = input.nextLine();
			
			if (response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("N")) {
				validInput = true;
				includeUppercase = response.equalsIgnoreCase("Y");
			} else {
				System.out.println("[INVALID INPUT]: You must enter 'Y' for yes or 'N' for no.\n");
			}
		}
		System.out.println();

		validInput = false;
		
		while (!validInput) {
			System.out.print("Should the password contain numbers? (Y/N)");
			
			response = input.nextLine();
			
			if (response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("N")) {
				validInput = true;
				includeNumbers = response.equalsIgnoreCase("Y");
			} else {
				System.out.println("[INVALID INPUT]: You must enter 'Y' for yes or 'N' for no.\n");
			}
		}
		
		input.close();
		
		System.out.println("\n");
		System.out.println("Generating password with the following parameters:");
		System.out.println("Password Length: " + passwordLength);
		System.out.println("Include Symbols: " + (includeSymbols ? "Y" : "N"));
		System.out.println("Include Uppercase: " + (includeUppercase ? "Y" : "N"));
		System.out.println("Include Numbers: " + (includeNumbers ? "Y" : "N"));
		System.out.println();
		System.out.print("Generated Password: ");
		System.out.println(getGeneratedPassword());
	}
	
	/**
	 * Determine whether the provided input is within the specified limits.
	 * 
	 * @param input The input to verify.
	 * @param min The mininum allowed number for the given input.
	 * @param max The maximum allowed number for the given input.
	 * 
	 * @return true if the length is within range, false otherwise.
	 */
	public static boolean validLength(int input, int min, int max) {
		return input >= min && input <= max;
	}

	/**
	 * Generate a password given the parameters provided by user input.
	 * 
	 * @return The password generated.
	 */
	public static String getGeneratedPassword() {
		String lowercaseAlphabet = "abcdefghijklmnopqrstuvwxyz";
		String uppercaseAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String symbols = "!@#$%^&*()-_=+?/.><[]{}|;:'`~*";
		String numbers = "1234567890";
		
		String possibleChars = lowercaseAlphabet;
		
		if (includeUppercase)
			possibleChars += uppercaseAlphabet;
		
		if (includeSymbols)
			possibleChars += symbols;
		
		if (includeNumbers)
			possibleChars += numbers;
		
		Random random = new Random();
		
		String password = "";
		
		for (int i = 0; i < passwordLength; i++) {
			password += possibleChars.charAt(random.nextInt(possibleChars.length()));
		}
		
		return password;
	}

}
