package signature;

public class MyBike implements bicycle{

	MyBike bike;
	int modelNumber;
	String color;
	double price;
	String brand;
	static int noChange;
	
	public void MyBike(){
		modelNumber = 0; color = ""; price = 0; brand = "";
		noChange = 5;
		//String noChange = "hey";
	}
	
	
	@Override
	public int modelNumber() {
		//this.modelNumber = 500; 
		modelNumber = 500;
		noChange = 8;
		this.noChange = 47;
		return 0; //remove this.noChange
	}
	
	public void setNoChange(){
		//this.noChange = 10;
		noChange = 11;
	}
	
	private void setModelNumber(){
		
	}

	@Override
	public int wheelSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setColor() {
		// TODO Auto-generated method stub
		this.color = "red";
		
	}

	@Override
	public double bikePrice() {
		// TODO Auto-generated method stub
		this.price = 199.95;
		return this.price;
	}

	@Override
	public String bikeBrand() {
		// TODO Auto-generated method stub
		this.brand = "huffy";
		return null;
	}
	
	public static void main(String args[]) {
		MyBike bike = new MyBike();
		System.out.println("my bikes model number is " + bike.modelNumber());
		bike.setColor();
		bike.bikePrice();
		System.out.println("my " + bike.color + " bike cost me " + bike.price);
		System.out.println("without the use of class reference to no change = " + noChange);
		System.out.println("using this.class reference to no change = " + bike.noChange );
		System.out.println("bike model number to get static value of noChange , it = " + bike.modelNumber());
		bike.setNoChange();
		System.out.println("setNoChange(), wihthout this =  " + noChange + " with this = " + bike.noChange);
		MyBike newBike = new MyBike();
		System.out.println("newBike noChange = " + noChange + " with this = " + newBike.noChange  + 
				" static refrecence to nochange = " + MyBike.noChange);
	}

}
