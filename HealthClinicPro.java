package fianl;

import java.util.Base64;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HealthClinicPro {

	public static void main(String[] args) {
		Login();
	}
	
	private static void Login()
	{
		String ANSI_GREEN = "\u001B[32m";
		String ANSI_RED = "\u001B[31m";
		String ANSI_RESET = "\u001B[0m";
		System.out.println(ANSI_GREEN + "" + "██╗      ██████╗  ██████╗ ██╗███╗   ██╗\r\n"
				+ "██║     ██╔═══██╗██╔════╝ ██║████╗  ██║\r\n" + "██║     ██║   ██║██║  ███╗██║██╔██╗ ██║\r\n"
				+ "██║     ██║   ██║██║   ██║██║██║╚██╗██║\r\n" + "███████╗╚██████╔╝╚██████╔╝██║██║ ╚████║\r\n"
				+ "╚══════╝ ╚═════╝  ╚═════╝ ╚═╝╚═╝  ╚═══╝\r\n" + "                                       "
				+ ANSI_RESET);
		System.out.println();

		Scanner in = new Scanner(System.in);
		int count = 0;
		String username, password;

		while (count < 3) { // 3 times to login
			System.out.println(ANSI_RED + "Username: ");
			username = in.nextLine();
			System.out.println();
			System.out.println("Password: ");
			password = in.nextLine();

			System.out.println(ANSI_RESET);

			String Hashedpassword = getHash(password);
			String userType = checkUserType(username, Hashedpassword);

			if (userType != null) {
				if (userType.equals("Registrar")) {
					MyLogger.writeToLog("The user " + username + " has Loged in successfuly");
					Regestrar(username);
				} else if (userType.equals("Doctor")) {
					MyLogger.writeToLog("The user " + username + " has Loged in");
					Doctor(username);
				} else if (userType.equals("Patient")) {
					MyLogger.writeToLog("The user " + username + " has Loged in");
					Patient(username);
				}

				break; // Break out of the loop on successful login
			} else {
				System.out.println("Incorrect username or password. Try again.");
				count++;
			}
			if (count == 3) {
				MyLogger.writeToLog("The user " + username + " tried to login with password");
				System.out.println("Too many failed login attempts. Please wait for 2 minute.");
				try {
					Thread.sleep(180000); // Wait for 3 minute (180,000 milliseconds) (Every 60,000 milliseconds =1
											// minute)
				} catch (InterruptedException e) {
					System.err.println("Error");
					MyLogger.writeToLog("Error", e);
				}
				count = 0; // Reset the count after waiting
			}
		}
		// Close the Scanner after the loop if you don't need it anymore
		in.close();
	}

	private static String getHash(String value) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			result = encode(md.digest(value.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			System.err.println("The Algorithm doesn't exist");
		}
		return result;
	}

	private static String encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	private static String FileRead(String fileName, String username, String password) {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(fileName));

			String line;
			while ((line = br.readLine()) != null) {

				String[] columns = line.split("\\|");
				if (columns.length >= 2 && columns[0].equalsIgnoreCase(username) && columns[1].equals(password)) {
					// Return the type of user found
					if (fileName.contains("Doctor")) {
						return "Doctor";
					} else if (fileName.contains("Patient")) {
						return "Patient";
					} else if (fileName.contains("Registrar"))
						return "Registrar";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null; // No match found
	}

	private static String checkUserType(String username, String password) {
		String userType = null;

		// Check Registrar.txt
		if (FileRead("C:\\Users\\user\\Desktop\\SecureCoding\\SecureCoding\\src\\fianl\\Registrar.txt", username,
				password) != null) {
			userType = "Registrar";
		}
		// Check Doctor.txt
		else if (FileRead("C:\\Users\\user\\Desktop\\SecureCoding\\SecureCoding\\src\\fianl\\Doctor.txt", username,
				password) != null) {
			userType = "Doctor";
		}
		// Check Patient.txt
		else if (FileRead("C:\\Users\\user\\Desktop\\SecureCoding\\SecureCoding\\src\\fianl\\Patient.txt", username,
				password) != null) {
			userType = "Patient";
		}
		return userType;
	}

	private static boolean checkPolicy(String password) {
		// Password must contain at least one capital letter, one symbol, one number,
		// and be limited to 15 characters
		String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{1,15}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	private static void writeToFile(String fileName, String username, String password, Long phoneNumber, String gender,
			int age, String UserType, String RegName) {
		BufferedReader br = null;
		BufferedWriter writer = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			// Check if the username or phone number already exists in the file
			String line;
			while ((line = br.readLine()) != null) {
				String[] columns = line.split("\\|");
				if (columns.length >= 1 && columns[0].equalsIgnoreCase(username)) {
					System.out.println("Username already exists. User not added.");
					MyLogger.writeToLog("Error while Registering the New user" + "Cause : Username already exists");
					return;
				}
				if (columns.length >= 3 && columns[2].equals(String.valueOf(phoneNumber))) {
					System.out.println("Phone number already exists. User not added.");
					MyLogger.writeToLog("Error while Registering the New user" + "Cause : Phone number already exists");
					return;
				}
			}
			// If the username and phone number don't exist, proceed to add the new user
			writer = new BufferedWriter(new FileWriter(fileName, true));
			// Append the new user information to the file
			writer.write(username + "|" + password + "|" + phoneNumber + "|" + gender + "|" + age + "|" + UserType);
			writer.newLine(); // Move to the next line for the next entry
			MyLogger.writeToLog("The Regestrar " + RegName + " has Registered the " + UserType + " " + username);
			System.out.println("User added successfully.");
		} catch (IOException | NumberFormatException e) {
			System.err.println("Error");
			System.err.println("Error writing to the file: ");
			System.err.println("Error While writing the file Or Wrong Number format");
			MyLogger.writeToLog("Error: writing the file Or Wrong Number format", e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				System.err.println("Error Closing the reader or writer ");
				MyLogger.writeToLog("Error: Closing the reader or writer", e);
			}
		}
	}

	// function for Regestrar
	private static void Regestrar(String username) {
		String ANSI_GREEN = "\u001B[32m";
		String ANSI_RESET = "\u001B[0m";
		System.out.println("Login successful! Welcome: " + username);
		Scanner Reg = new Scanner(System.in);
		System.out.println(ANSI_GREEN
				+ "███████╗███████╗ ██████╗ ██╗███████╗████████╗███████╗██████╗     ██╗   ██╗███████╗███████╗██████╗ \r\n"
				+ "██╔══██╗██╔════╝██╔════╝ ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗    ██║   ██║██╔════╝██╔════╝██╔══██╗\r\n"
				+ "██████╔╝█████╗  ██║  ███╗██║███████╗   ██║   █████╗  ██████╔╝    ██║   ██║███████╗█████╗  ██████╔╝\r\n"
				+ "██╔══██╗██╔══╝  ██║   ██║██║╚════██║   ██║   ██╔══╝  ██╔══██╗    ██║   ██║╚════██║██╔══╝  ██╔══██╗\r\n"
				+ "██║  ██║███████╗╚██████╔╝██║███████║   ██║   ███████╗██║  ██║    ╚██████╔╝███████║███████╗██║  ██║\r\n"
				+ "╚═╝  ╚═╝╚══════╝ ╚═════╝ ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝     ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝\r\n"
				+ "                                                                                                   "
				+ ANSI_RESET);

		String Username, Password, Gender, UserType;
		int Age;
		long PhoneNumber;

		System.out.println("Enter the information for user");

		System.out.println("Patient Or Doctor ?");
		UserType = Reg.nextLine().toLowerCase();

		try {
			System.out.println("Name:");
			Username = Reg.nextLine();

			// Password Policy Check
			do {
				System.out.println("Password (at least one capital letter, one symbol, one number, max length 15):");
				Password = Reg.nextLine();

				if (checkPolicy(Password)) {
					break;
				} else {
					System.out.println("Password does not meet the criteria. Try again.");
				}
			} while (true);

			System.out.println("Gender:");
			Gender = Reg.nextLine();

			System.out.println("Age:");
			Age = Reg.nextInt();

			System.out.println("Phonenumber:");
			PhoneNumber = Reg.nextLong();

			// Constructing file path based on user type
			String filePath = "C:\\Users\\user\\Desktop\\SecureCoding\\SecureCoding\\src\\fianl\\" + UserType + ".txt";

			if (numberExists(filePath, PhoneNumber)) {
				System.out.println("Phone number already exists. Registration failed.");
			} else {
				writeToFile(filePath, Username, getHash(Password), PhoneNumber, Gender, Age, UserType, username);
				System.out.println("Phone Added successfully!");
			}

			// Check if the username already exists in the file
			if (!usernameExists(filePath, username)) {
				System.out.println("Registration successful!");
			} else {
				writeToFile(filePath, Username, getHash(Password), PhoneNumber, Gender, Age, UserType, username);
				System.out.println("Username already exists. Registration failed.");
			}
		} catch (InputMismatchException e) {
			MyLogger.writeToLog("Invalid input", e);
			System.err.println("Invalid input");
		} finally {
			Reg.close();
		}
	}

	// Function to check if the username exists in the specified file
	private static boolean usernameExists(String filePath, String username) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// Assuming the username is the first field in each line
				if (line.split("\\s+")[0].equals(username)) {
					return true; // Username already exists
				}
			}
		} catch (IOException e) {
			MyLogger.writeToLog("Error reading file", e);
			System.err.println("Error reading file");
		}
		return false; // Username does not exist
	}

	private static boolean numberExists(String filePath, long phoneNumber) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] columns = line.split("\\|");
				if (columns.length >= 3 && Long.parseLong(columns[2]) == phoneNumber) {
					return true; // Phone number already exists
				}
			}
		} catch (IOException | NumberFormatException e) {

		}
		return false; // Phone number does not exist
	}

	// End of Regestrar Function
	// function for Patient
	private static void Patient(String username) {
		String userType = "Patient";

		System.out.println("Login successful! Welcome: " + username);
		Scanner Pat = new Scanner(System.in);
		System.out.println("1-View Patient information");
		System.out.println("2-View medical information");
		int PatientChoice = Pat.nextInt();
		switch (PatientChoice) {
		case 1:
			MyLogger.writeToLog("The patient " + username + " Has Viewed his info");
			viewInformation(username, userType);
			break;
		case 2:
			MyLogger.writeToLog("The patient " + username + " Has Viewed his Medical info");
			viewMedicalRecord(username);
			break;
		default:
			System.out.println("Invalid choice. Please enter 1 or 2.");
		}
	}

	private static void viewInformation(String username, String userType) {
		BufferedReader brPat = null;
		BufferedReader brDoc = null;

		try {
			brPat = new BufferedReader(
					new FileReader("C:\\Users\\user\\Desktop\\SecureCoding\\SecureCoding\\src\\fianl\\patient.txt"));
			brDoc = new BufferedReader(
					new FileReader("C:\\Users\\user\\Desktop\\SecureCoding\\SecureCoding\\src\\fianl\\doctor.txt"));
			String line;

			if ("Patient".equals(userType)) {
				while ((line = brPat.readLine()) != null) {
					String[] columns = line.split("\\|");
					if (columns.length >= 5 && columns[0].equals(username)) {
						String patientName = columns[0];
						Long phoneNumber = Long.parseLong(columns[2]);
						String gender = columns[3];
						int age = Integer.parseInt(columns[4]);

						System.out.println("Patient Information:");
						System.out.println("Name: " + patientName);
						System.out.println("Phone Number: " + phoneNumber);
						System.out.println("Gender: " + gender);
						System.out.println("Age: " + age);

						break; // Exit the loop after finding the patient's information
					}
				}
			} else if ("Doctor".equals(userType)) {
				while ((line = brDoc.readLine()) != null) {
				    String[] columns = line.split("\\|");
				    if (columns.length >= 5 && columns[0].equalsIgnoreCase(username)) {
				        String doctorName = columns[0];
				        Long phoneNumber = Long.parseLong(columns[2]);
				        String gender = columns[3];
				        int age = Integer.parseInt(columns[4]);

				        System.out.println("Doctor Information:");
				        System.out.println("Name: " + doctorName);
				        System.out.println("Phone Number: " + phoneNumber);
				        System.out.println("Gender: " + gender);
				        System.out.println("Age: " + age);

				        // You can add more information as needed
				        break;
				    }
				}

			} else {
				System.err.println("invalid input");
			}
		} catch (IOException e) {
			System.err.println("Error Reading from file");
			MyLogger.writeToLog("Error: Reading from file", e);
		} finally {
			try {
				if (brPat != null && brDoc != null) {
					brPat.close();
					brDoc.close();
				}
			} catch (IOException e) {
				System.err.println("Error Closing the reader");
				MyLogger.writeToLog("Error: Closing the reader", e);
			}
		}
	}

	private static void viewMedicalRecord(String username) {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(
					"C:\\Users\\user\\Desktop\\SecureCoding\\SecureCoding\\src\\fianl\\MedicalInfo.txt"));

			String line;
			while ((line = br.readLine()) != null) {
				String[] columns = line.split("\\|");

				if (columns.length >= 3 && columns[1].equals(username)) {
					String medicalSituation = columns[2];
					String medicalTreatment = columns[3];

					System.out.println("Medical Record:");
					System.out.println("Medical Situation: " + medicalSituation);
					System.out.println("Medical Treatment: " + medicalTreatment);

					// You can add more information as needed
					break; // Exit the loop after finding the medical record
				}
			}
		} catch (IOException e) {
			System.err.println("Error Reading from file");
			MyLogger.writeToLog("Error: Reading from file", e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				System.err.println("Error Closing the reader");
				MyLogger.writeToLog("Error: Closing the reader", e);
			}
		}
	}

	// End for Patient function

	// Start Of Doctor Methods and Features
	private static void Doctor(String username) {
		String userType = "Doctor";
		System.out.println("Login successful! Welcome: " + username);
		Scanner Doc = new Scanner(System.in);
		System.out.println("1-View Doc information");
		System.out.println("2-enter medical information of patient");
		int DocChoice = Doc.nextInt();
		switch (DocChoice) {
		case 1:
			MyLogger.writeToLog("The Doctor " + username + " Has Viewed his info");
			viewInformation(username, userType);
			break;
		case 2:
			MyLogger.writeToLog("The Doctor " + username + " Has Enterd Medical info");
			enterMedicalInformation(username);
			break;
		default:
			System.out.println("Invalid choice. Please enter 1 or 2.");
		}
	}

	private static void enterMedicalInformation(String doctorName) {
		Scanner input = new Scanner(System.in);

		System.out.println("Enter patient's name: ");
		String patientName = input.nextLine();

		System.out.println("Enter medical situation: ");
		String medicalSituation = input.nextLine();

		System.out.println("Enter medical treatment: ");
		String medicalTreatment = input.nextLine();

		// Save the medical information to a file
		writeMedicalInfoToFile("C:\\Users\\user\\Desktop\\SecureCoding\\SecureCoding\\src\\fianl\\MedicalInfo.txt",
				doctorName, patientName, medicalSituation, medicalTreatment);
		System.out.println("Successful Saved");
	}

	private static void writeMedicalInfoToFile(String fileName, String doctorName, String patientName,
			String medicalSituation, String medicalTreatment) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
			// Append the new medical information to the file
			writer.write(doctorName + "|" + patientName + "|" + medicalSituation + "|" + medicalTreatment);
			writer.newLine(); // Move to the next line for the next entry
		} catch (IOException e) {
			System.err.println("Error writing medical information to the file: ");
			MyLogger.writeToLog("Error: writing medical information to the file", e);
		}
	}
	// End Of Doctor Methods and Features
}
