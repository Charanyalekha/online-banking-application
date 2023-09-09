package miniproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

class CustomerDetails {
	String custName;
	long custMobileNo;
	final long custAccNo;
	String custPassword;
	static double balance;

	public CustomerDetails(String custName, long custAccNo, String custPassword, long custMobileNo, double balance) {
		this.custName = custName;
		this.custMobileNo = custMobileNo;
		this.custAccNo = custAccNo;
		this.custPassword = custPassword;
		this.balance = balance;
	}

}

class InvalidLoginAttemptsException extends Exception {
	public InvalidLoginAttemptsException(String s) {
		super(s);
	}
}

class InsufficientBalanceException extends Exception {
	public InsufficientBalanceException(String s) {
		super(s);
	}
}

class InvalidAccountException extends Exception {
	public InvalidAccountException(String s) {
		super(s);
	}
}

public class OnlineBankingApplication {
	public static boolean validateUser(HashMap<String, String> map, String username, String password)
			throws InvalidLoginAttemptsException {

		if (username == "" || password == "") {
			throw new InvalidLoginAttemptsException("Fields shoult not be empty...");

		}
		if (map.containsKey(username) && map.containsValue(password)) {
			System.out.println(" Login Successful...Welcome.." + username);
			return true;
		} else {
			throw new InvalidLoginAttemptsException(" Invalid login...(Login Failed)...");
		}
	}

	public static void depositMoney(HashMap<Long, Double> map1, long accNo, double amount)
			throws InvalidAccountException {
		Set<Long> keys = map1.keySet();
		for (Long key : keys) {
			if (map1.containsKey(accNo)) {
				map1.put(key, map1.get(key) + amount);
				CustomerDetails.balance += amount;
				System.out.println(" Amount Deposited successfully....");
			} else {
				throw new InvalidAccountException(" Couldn't Found Account Number...");
			}
		}
	}

	public static void withdrawMoney(HashMap<Long, Double> map1, long accNo, double amount)
			throws InsufficientBalanceException, InvalidAccountException {
		Set<Long> keys = map1.keySet();
		for (Long key : keys) {
			if (map1.containsKey(accNo)) {
				if (map1.get(key) < amount) {
					throw new InsufficientBalanceException(" InSufficient Balance...");
				} else {
					map1.put(key, map1.get(key) - amount);
					CustomerDetails.balance -= amount;
					System.out.println(" Amount withdraw successful....");
				}
			} else {
				throw new InvalidAccountException(" Couldn't Found Account Number...");
			}
		}
	}

	public static void displayBalance(HashMap<Long, Double> map1, long accNo2) throws InvalidAccountException {
		Set<Long> keys = map1.keySet();
		for (Long key : keys) {
			if (map1.containsKey(accNo2)) {
				System.out.println(" Your Balance :" + map1.get(key));
			} else {
				throw new InvalidAccountException(" Couldn't found Account Number...");
			}
		}

	}

	public static void main(String[] args)
			throws InvalidAccountException, InsufficientBalanceException, InvalidLoginAttemptsException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		ArrayList<CustomerDetails> list = new ArrayList<>();
		HashMap<String, String> map = new HashMap<>();
		HashMap<Long, Double> map1 = new HashMap<>();
		System.out.println(" ***** Weicome to IndianBank *****");

		int choice;
		do {
			System.out.println(" What do you want to do : \n 1.Create Account \n 2.Login \n 3.Exit");
			choice = Integer.parseInt(br.readLine());
			switch (choice) {
			case 1:
				System.out.println(" Enter your Full Name :");
				String custName = br.readLine();
				System.out.println(" Enter your Mobile Number :");
				long custMobileNo = Long.parseLong(br.readLine());
				System.out.println(" Enter your Account Number :");
				long custAccNo = Long.parseLong(br.readLine());
				System.out.println(" Enter your Password :");
				// password should 
				//   1.  contain atleast 8 characters.
				//   2.  start with a uppercase letter.
				//   3.  contain atleast one digit.
				//   4.  contain atleast one lowercase letter.
				//   5.  contain atleast one special character(@#$%^&+=).
				String custPassword;
				custPassword = br.readLine();
				while (!Pattern.matches("^(?=[A-Z])(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).*$", custPassword)) {
					System.out.println(" Password doesn't meet the requiremennts...enter again...");
					custPassword = br.readLine();
				}
				double balance = 0;
				list.add(new CustomerDetails(custName, custMobileNo, custPassword, custAccNo, balance));
				map.put(custName, custPassword);
				map1.put(custAccNo, balance);
				System.out.println(" Account created Successfully...");
				break;
			case 2:
				int attempts = 3;

				while (attempts != 0) {
					try {
						System.out.println(" Enter your Username :");
						String username = br.readLine();
						System.out.println(" Enter your password :");
						String password = br.readLine();

						if (validateUser(map, username, password)) {
							int ch;
							do {
								System.out.println(
										" What do you want to do now : \n 1. Deposit Money \n 2. Withdraw Money \n 3. View Balance \n 4.Logout ");
								ch = Integer.parseInt(br.readLine());
								switch (ch) {
								case 1:
									System.out.println(" Enter account number to which you want to deposit money :");
									long accNo = Long.parseLong(br.readLine());
									System.out.println(" Enter amount to deposit :");
									double depositAmount = Double.parseDouble(br.readLine());
									try {
										depositMoney(map1, accNo, depositAmount);
									} catch (InvalidAccountException e) {
										System.out.println(e.getMessage());
									}
									break;
								case 2:
									System.out.println(" Enter account number from which you want to withdraw money :");
									long accNo1 = Long.parseLong(br.readLine());
									System.out.println(" Enter amount to withdraw :");
									double withdrawAmount = Double.parseDouble(br.readLine());
									try {
										withdrawMoney(map1, accNo1, withdrawAmount);
									} catch (InvalidAccountException e1) {
										System.out.println(e1.getMessage());
									} catch (InsufficientBalanceException e2) {
										System.out.println(e2.getMessage());
									}
									break;
								case 3:
									System.out.println(" Enter Account Number...");
									long accNo2 = Long.parseLong(br.readLine());
									try {
										displayBalance(map1, accNo2);
									} catch (InvalidAccountException e) {
										System.out.println(e.getMessage());
									}
									break;
								case 4:
									System.out.println(" Logout Successful.....");
									break;
								default:
									System.out.println(" Invalid Entry....");
								}
							} while (ch < 4);
							break;
						} else {
							attempts--;
						}
					} catch (InvalidLoginAttemptsException e1) {
						attempts--;
						System.out.println(e1.getMessage() + "Remaining attempts :" + attempts);
					}
				}
				if (attempts == 0) {
					System.out.println(" Maximum number of attempts exceeded...");
					System.exit(0);
				}
				break;
			case 3:
				System.out.println(" ***Thanks for Visiting..Hope u had a great experience***");
				System.exit(0);
			default:
				System.out.println("Invalid Entry....");
			}

		} while (choice <= 3);

	}

}
