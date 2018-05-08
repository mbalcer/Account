import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.IOException;

public class main {
	public static boolean checkNameAccount(String name) throws IOException {
		File fileTask = new File("account.txt");
		Scanner fileIn = new Scanner(fileTask);
		int numberLine = 0;
		
		while(fileIn.hasNextLine()) {
			String in = fileIn.nextLine();
			if(in.equalsIgnoreCase(name) && numberLine % 4 == 1)
				return true;
			numberLine++;
		}
		fileIn.close();
		
		return false;
	}
	
	public static int howManyLines() throws FileNotFoundException {
		File fileTask = new File("account.txt");
		Scanner fileIn = new Scanner(fileTask);
		int numberLines = 0;
		while(fileIn.hasNextLine()) {
			numberLines++;
			fileIn.nextLine();
		}
		return numberLines;
	}
	
	public static void changeBalance(String name, String password, int balance) throws IOException{
		int numberLines = howManyLines(), counterLines = 0;
		String allFileInString[] = new String[numberLines]; 
		
		File file = new File("account.txt");
		Scanner fileIn = new Scanner(file);
		while(fileIn.hasNextLine()) {
			allFileInString[counterLines] = fileIn.nextLine();
			counterLines++;
		}
		fileIn.close();
		
		Writer fileWrite = new BufferedWriter(new FileWriter("account.txt",false));
		for(int i=0; i<numberLines; i++) {
			if(allFileInString[i].equals(name) && i % 4 == 1) {
				fileWrite.append(name+"\r\n");
				fileWrite.append(password+"\r\n");
				fileWrite.append(String.valueOf(balance)+"\r\n");		
				i+=2;
			}
			else {
				fileWrite.append(allFileInString[i]+"\r\n");
			}
		}
		fileWrite.close();
		
	}
	
	public static void accountService(String name, String password, int balance) throws IOException {
		System.out.println("--------------------");
		System.out.println("Nazwa: "+name);
		System.out.println("Saldo: "+balance);
		System.out.println("1. Wyp³aæ pieni¹dze z konta");
		System.out.println("2. Wp³aæ pieni¹dze na konto");
		System.out.println("3. Przeœlij pieni¹dze");
		System.out.println("0. Wyloguj siê");
		System.out.println("--------------------");
		System.out.print("Wybór: ");
		
		Scanner in = new Scanner(System.in);
		int choose = in.nextInt();
		
		Account acc = new Account(name,password,balance);
		switch(choose) {
			case 1: {
				System.out.print("Ile wyp³aciæ? ");
				int amount = in.nextInt();
				acc.debit(amount);
				changeBalance(acc.getName(), acc.getPassword(), acc.getBalance());
			} break;
			case 2: {
				System.out.print("Ile wp³aciæ? ");
				int amount = in.nextInt();
				acc.credit(amount);
				changeBalance(acc.getName(), acc.getPassword(), acc.getBalance());
			} break;
			case 3: {
				System.out.print("Komu przes³aæ pieniadze? (podaj nazwe) ");
				String nameAnother = in.next();
				if(checkNameAccount(nameAnother)) {
					File fileTask = new File("account.txt");
					Scanner fileIn = new Scanner(fileTask);
					int numberLine = 0;
					boolean pass = false;
					String passwordAnother = "";
					Account a2 = null;
					
					while(fileIn.hasNextLine()) {
						String fileInString = fileIn.nextLine();
						if(pass && numberLine % 4 == 2) {
							passwordAnother = fileInString;
						}
						else if(pass & numberLine % 4 == 3) {
							int balanceAnother = Integer.parseInt(fileInString);
							a2 = new Account(nameAnother, passwordAnother, balanceAnother);
							break;
						}
						if(fileInString.equalsIgnoreCase(nameAnother) && numberLine % 4 == 1) {
							pass = true;
						}
						numberLine++;
					}
					fileIn.close();
					
					System.out.print("Ile pieniêdzy przes³aæ do "+nameAnother+"? ");
					int amount = in.nextInt();
					acc.transferTo(a2, amount);
					
					changeBalance(acc.getName(), acc.getPassword(), acc.getBalance());
					changeBalance(a2.getName(), a2.getPassword(), a2.getBalance());
				} else {
					System.out.println("\nNie mamy takiego konta w bazie");
				}
			} break;
			case 0: break;
			default: {
				System.out.println("Z³y wybór");
			} break;
		}
	}
	
	public static void createAccount() throws IOException {
		Random rand = new Random();
		Scanner in = new Scanner(System.in);
		
		String name;
		do {
			System.out.println("Podaj nazwe: ");
			name = in.nextLine();
		} while(checkNameAccount(name));
		
		System.out.println("Podaj haslo: ");
		String password = in.nextLine();
		
		String id = String.valueOf(rand.nextInt(1000000));
		
		Writer record = new BufferedWriter(new FileWriter("account.txt", true));  
		record.append(id+"\r\n");
		record.append(name+"\r\n");
		record.append(password+"\r\n");
		record.append("0\r\n");
		record.close();
	}
	
	public static void loginToAccount() throws IOException {
		Scanner in = new Scanner(System.in);
		
		System.out.println("Podaj nazwe: ");
		String name = in.nextLine();
		if(checkNameAccount(name)==false) {
			System.out.println("Nie ma konta o takiej nazwie w banku b¹dz poda³eœ z³¹ nazwê konta");
			return;
		}
		
		System.out.println("Podaj has³o: ");
		String password = in.nextLine();
		
		File fileTask = new File("account.txt");
		Scanner fileIn = new Scanner(fileTask);
		int numberLine = 0;
		boolean pass = false;
		
		while(fileIn.hasNextLine()) {
			String fileInString = fileIn.nextLine();
			if(pass==true && numberLine % 4 == 2) {
				if(fileInString.equals(password)==false) {
					System.out.println("Poda³eœ z³e has³o do konta");
					return;
				}
				else {
					System.out.println("Zalogowa³eœ siê do swojego konta");
				}
			}
			else if(pass==true && numberLine % 4 == 3) {
				int balance = Integer.parseInt(fileInString);
				accountService(name,password,balance);
				break;
			}
			if(fileInString.equalsIgnoreCase(name) && numberLine % 4 == 1) {
				pass = true;
			}
			numberLine++;
		}
		fileIn.close();
	}
	
	public static void deleteAccount() throws IOException {
		Scanner in = new Scanner(System.in);
		
		System.out.println("Podaj nazwe: ");
		String name = in.nextLine();
		if(checkNameAccount(name)==false) {
			System.out.println("Nie ma konta o takiej nazwie w banku b¹dz poda³eœ z³¹ nazwê konta");
			return;
		}
		System.out.println("Podaj has³o: ");
		String password = in.nextLine();
		
		File fileTask = new File("account.txt");
		Scanner fileIn = new Scanner(fileTask);
		int numberLine = 0;
		boolean pass = false;
		String allInFile[] = new String[howManyLines()];
		
		while(fileIn.hasNextLine()) {
			allInFile[numberLine] = fileIn.nextLine();
			numberLine++;
		}
		
		
		fileIn.close();
		int del=-1;
		
		for(int i=0; i<numberLine; i++) {
			if(pass==true && i % 4 == 2) {
				if(allInFile[i].equals(password)==false) {
					System.out.println("Poda³eœ z³e has³o do konta");
					return;
				}
				else {
					System.out.println("Na pewno chcesz usun¹æ konto "+name+" (T/N)?");
					String confirmDelete = in.next();
					if(confirmDelete.equalsIgnoreCase("t")){
						Writer fileAccountWrite = new BufferedWriter(new FileWriter("account.txt",false)); 
						for(i=0; i<allInFile.length; i++) {
							if(i==del)
								i+=3;
							else
								fileAccountWrite.append(allInFile[i]+"\r\n");
						}
						fileAccountWrite.close();
						break;
					}
					else return;
				}
			}
			if(allInFile[i].equalsIgnoreCase(name) && i % 4 == 1)	{
				del = i-1;
				pass = true;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		for(;;) {
			System.out.println("-------------------");
			System.out.println("1. Utworz konto w banku");
			System.out.println("2. Zaloguj sie do banku");
			System.out.println("3. Usuñ konto");
			System.out.println("0. Wyjdz");
			System.out.println("-------------------");
			System.out.print("Wybór: ");
			
			Scanner in = new Scanner(System.in);
			int choice = in.nextInt();
			
			switch(choice) {
				case 1: {
					createAccount();
				} break;
				case 2: {
					loginToAccount();
				} break;
				case 3: {
					deleteAccount();
				} break;
				case 0: {
					System.exit(0);
				} break;
				default: {
					System.out.println("Z³y wybór");
				} break;
			}
			
		}
	}

}
