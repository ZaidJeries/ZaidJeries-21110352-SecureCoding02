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

public class HealthClinicPro {

	public static void main(String[] args) {
		String ANSI_GREEN = "\u001B[32m";
		String ANSI_RED = "\u001B[31m";
		String ANSI_RESET = "\u001B[0m";
		System.out.println(ANSI_GREEN + "" + "····································\r\n"
				+ ":  | |     ___    __ _ (_) _ __    :\r\n" + ":  | |    / _ \\  / _` || || '_ \\   :\r\n"
				+ ":  | |___| (_) || (_| || || | | |  :\r\n" + ":  |_____|\\___/  \\__, ||_||_| |_|  :\r\n"
				+ ":                |___/             :\r\n" + "····································" + ANSI_RESET);
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
					Regestrar(username);
				} else if (userType.equals("Doctor")) {
					Doctor(username);
				} else if (userType.equals("Patient")) {
					Patient(username);
				}
				break; // Break out of the loop on successful login
			} else {
				System.out.println("Incorrect username or password. Try again.");
				count++;
			}
			if (count == 3) {
				System.out.println("Too many failed login attempts. Please wait for 2 minute.");
				try {
					Thread.sleep(120000); // Wait for 2 minute (120,000 milliseconds)
				} catch (InterruptedException e) {
					e.printStackTrace();
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

	private static void writeToFile(String fileName, String username, String password, Long phoneNumber, String gender,
			int age, String UserType) {
		BufferedReader br = null;
		BufferedWriter writer = null;

		try {
			br = new BufferedReader(new FileReader(fileName));

			// Check if the username or phone number already exists in the file
			// Check if the username or phone number already exists in the file
			String line;
			while ((line = br.readLine()) != null) {
				String[] columns = line.split("\\|");
				if (columns.length >= 1 && columns[0].equalsIgnoreCase(username)) {
					System.out.println("Username already exists. User not added.");
					return;
				}
				if (columns.length >= 3 && columns[2].equals(String.valueOf(phoneNumber))) {
					System.out.println("Phone number already exists. User not added.");
					return;
				}
			}

			// If the username and phone number don't exist, proceed to add the new user
			writer = new BufferedWriter(new FileWriter(fileName, true));
			// Append the new user information to the file
			writer.write(username + "|" + password + "|" + phoneNumber + "|" + gender + "|" + age + "|" + UserType);
			writer.newLine(); // Move to the next line for the next entry
			System.out.println("User added successfully.");

		} catch (IOException | NumberFormatException e) {
			System.err.println("Error");
			System.err.println("Error writing to the file: " + e.getMessage());
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				System.err.println("Error while closing: " + e.getMessage());
			}
		}
	}

	// function for Regestrar
	private static void Regestrar(String username) {
		System.out.println("Login successful! Welcome: " + username);
		Scanner Reg = new Scanner(System.in);
		System.out.println("Register new user");

		String Username, Password, Gender, UserType;
		int Age;
		long PhoneNumber;

		System.out.println("Enter the information for user");

		try {
			System.out.println("Name:");
			Username = Reg.nextLine();

			System.out.println("Password:");
			Password = Reg.nextLine();
			String HashedPassword = getHash(Password);

			System.out.println("Gender:");
			Gender = Reg.nextLine();
			Reg.nextLine();

			System.out.println("Phonenumber:");
			PhoneNumber = Reg.nextLong();

			System.out.println("Age:");
			Age = Reg.nextInt();
			Reg.nextLine();

			System.out.println("Patient Or Doctor ?");
			UserType = Reg.nextLine().toLowerCase();

			switch (UserType) {
			case "patient":
				writeToFile("C:\\Users\\user\\Desktop\\SecureCoding\\SecureCoding\\src\\fianl\\Patient.txt", Username,
						HashedPassword, PhoneNumber, Gender, Age, UserType);
				System.out.println("Successful Registerd");
				break;
			case "doctor":
				writeToFile("C:\\Users\\user\\Desktop\\SecureCoding\\SecureCoding\\src\\fianl\\Doctor.txt", Username,
						HashedPassword, PhoneNumber, Gender, Age, UserType);
				System.out.println("Successful Registerd");
				break;
			default:
				System.out.println("Unknown user type. Information not saved.");

			}
		} catch (InputMismatchException e) {
			System.err.println("invalid input");
		} finally {
			Reg.close();
		}
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
			viewInformation(username, userType);
			break;
		case 2:
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
					new FileReader("C:\\Users\\user\\Desktop\\SecureCoding\\SecureCoding\\src\\fianl\\Patient.txt"));
			brDoc = new BufferedReader(
					new FileReader("C:\\Users\\user\\Desktop\\SecureCoding\\SecureCoding\\src\\fianl\\Doctor.txt"));
			String line;

			if (userType == "Patient") {
				while ((line = brPat.readLine()) != null) {
					String[] columns = line.split("\\|");
					if (columns.length >= 5 && columns[0].equals(username)) {
						String patientName = columns[0];
						int phoneNumber = Integer.parseInt(columns[2]);
						String gender = columns[3];
						int age = Integer.parseInt(columns[4]);

						System.out.println("Patient Information:");
						System.out.println("Name: " + patientName);
						System.out.println("Phone Number: " + phoneNumber);
						System.out.println("Gender: " + gender);
						System.out.println("Age: " + age);

						// You can add more information as needed
						break; // Exit the loop after finding the patient's information
					}
				}
			} else if(userType == "Doctor") {
				while ((line = brDoc.readLine()) != null) {
					String[] columns = line.split("\\|");
					if (columns.length >= 5 && columns[0].equals(username)) {
						String doctorName = columns[0];
						int phoneNumber = Integer.parseInt(columns[2]);
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
			}
			else {
				System.err.println("invalid input");
			}
		} catch (IOException e) {
			System.err.println("Error Reading from file");
		} finally {
			try {
				if (brPat != null && brDoc != null) {
					brPat.close();
					brDoc.close();
				}
			} catch (IOException e) {
				System.err.println("Error closing");
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
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				System.err.println("Error closing");
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
			viewInformation(username, userType);
			break;
		case 2:
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
		}
	}
	// End Of Doctor Methods and Features
}
