import java.util.InputMismatchException;
import java.util.Scanner;

public class DisplayStarApplication {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter an integer to generate string of stars : ");
		
		try {
			int userInput= scanner.nextInt();
			String stars = starString(userInput);
			System.out.println("The value for 2^"+ userInput +" = "+ stars.length());
			System.out.println("Representation in stars :"+stars);
			
		} catch (InputMismatchException ime) {
			System.out.print("Please provide a valid integer input ");
			
		} catch (IllegalArgumentException iae) {
			System.out.print("Please provide a positive integer "+iae.getMessage());
			
		} catch (OutOfMemoryError ome) {
			System.out.print("Out Of Memory error due to Heap space");
			
		} catch (Exception e){						
			System.out.print("Generic Exception "+e.getStackTrace());			
		}
		
	}
	
	private static String starString(int n) {

		if(n < 0) {			
			throw new IllegalArgumentException("The Value given is less than 0");
		}else if(n == 0) {
			return "*";
		}else {
			return starString(n-1) + starString(n-1);			 
		}	
	}

	
}
