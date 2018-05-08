
public class Account {
	private String id;
	private String name;
	private String password;
	private int balance = 0;
	
	public Account(String id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}
	public Account(String name, String password, int balance) {
		this.name = name;
		this.password = password;
		this.balance = balance;
	}
	
	public String getID() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public int getBalance() {
		return this.balance;
	}
	public String getPassword() {
		return this.password;
	}
	public int credit(int amount) {
		if(amount<0)
			System.out.println("The amount of money must be greater than 0");
		else 
			this.balance += amount;
		return balance;
	}
	public int debit(int amount) {
		if(amount<0)
			System.out.println("The amount of money must be greater than 0");
		else if(amount<=this.balance)
			this.balance -= amount;
		else
			System.out.println("Amount exceeded balance");
		
		return this.balance;
	}
	
	public int transferTo(Account another, int amount) {
		if(amount<0)
			System.out.println("The amount of money must be greater than 0");
		else if(amount<=this.balance) {
			another.balance += amount; 
			this.balance -= amount;
		}
		else
			System.out.println("Amount exceeded balance");
		
		return this.balance;
	}
	
	public String toString() {
		return "Account [id="+this.id+", name="+this.name+", balance="+this.balance+"]";
	}
}
