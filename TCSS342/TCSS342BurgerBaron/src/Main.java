//Coded by Elijah Cole

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
	
	private static Burger burger;
	// An int to keep track of the order number.
	private static int burgerNo;
	
    public static void main(String[] args) throws FileNotFoundException {
    	Scanner fileScanner = new Scanner(System.in);
		fileScanner = new Scanner(new File("customer.txt"));
		burgerNo = 0;
		while(fileScanner.hasNextLine()) {
			parseLine(fileScanner.nextLine());
		}
		fileScanner.close();
		//testMyStack();
		//testBurger();
		//final TestGenerator gen = new TestGenerator();
		//gen.genTestCase("not-for-mortal-eyes.txt");
    }
    
    public static void parseLine(final String line) throws FileNotFoundException {
    	if(line.isEmpty()) return;
    	final Scanner lineScanner = new Scanner(line);
    	final boolean iAmABaron = assertBurgerBasics(lineScanner);
    	assertAdditionsOmissions(lineScanner, iAmABaron);
    	
    	System.out.println("Processing order number " + burgerNo + ": " + line);
		System.out.println(burger.toString());
		burgerNo++;
    }
    
    // This helper method determines whether the burger is a baron, the number of patties,
    // and the patty type. It also returns a boolean representing whether the burger is a baron.
    private static boolean assertBurgerBasics(final Scanner lineScanner) {
    	boolean initializedAsBaron = false;
    	String nextWordHolder;
    	int pattiesToAdd = 0;
    	String requestedPatty = "Beef";
    	// The method only observes the first three words of the line, as those are the only relevant ones
    	// for this task.
    	for(int i = 0; i < 3; i++) {
    		nextWordHolder = lineScanner.next();
    		if(!lineScanner.hasNext()) i = 4;
    		switch(nextWordHolder) {
    		case "Baron": burger = new Burger(true);
    			initializedAsBaron = true;
    			break;
    		case "Double": pattiesToAdd = 1;
    			break;
    		case "Triple": pattiesToAdd = 2;
    			break;
    		// I took some pains to proof my code against the American appetite, even though it
    		// wasn't in the project's scope.
    		case "Quadruple": pattiesToAdd = 3;
    			break;
    		case "Quintuple": pattiesToAdd = 4;
    			break;
    		case "Chicken": case "Veggie":
    			requestedPatty = nextWordHolder;
    			break;
    		// Escape condition, this switch statement's scope has ended once Burger is reached.
    		case "Burger":
    			i = 4;
    			break;
    		default:
    			break;
    		}
    	}
    	if(!initializedAsBaron) burger = new Burger(false);
    	for(int i = pattiesToAdd; i > 0; i--) burger.addPatty();
    	burger.changePatties(requestedPatty);
    	return initializedAsBaron;
    }
    
    // Private helper method to handle everything assertBurgerBasics() didn't, namely
    // the categories to add/remove and the exceptions. Has a boolean as a parameter representing
    // whether the burger is a baron, so it knows whether to add or remove ingredients.
    private static void assertAdditionsOmissions(final Scanner lineScanner, 
    											 final boolean burgIsABaron) {
    	// Boolean representing whether the "but" in the line has been reached yet.
    	boolean beforeButt = true;
    	while(lineScanner.hasNext()) {
    		String nextWordHolder = lineScanner.next();
    		switch(nextWordHolder) {
    		case "Cheese": case "Sauce": case "Veggies":
    			// Did you know Java has a xor operator? I didn't before this project.
    			// This information isn't very useful though, because != will function
    			// identically to it. Worth note: it actually won't function identically
    			// for Booleans, but it will work for booleans. Frickin Java.
    			if(burgIsABaron ^ beforeButt) burger.addCategory(nextWordHolder);
    			else burger.removeCategory(nextWordHolder);
    			break;
    		case "Pickle": case "Top-Bun": case "Mayonnaise": case "Baron-Sauce":
    		case "Lettuce": case "Tomato": case "Onions": case "Pepperjack":
    		case "Mozzerella": case "Cheddar": case "Mushrooms": case "Mustard":
    		case "Ketchup": case "Bottom-Bun":
    			if(!burgIsABaron ^ beforeButt) burger.removeIngredient(nextWordHolder);
    			else burger.addIngredient(nextWordHolder);
    			break;
    		case "but":
    			beforeButt = false;
    			break;
    		default:
    			break;
    		}
    	}
    }
    public static void testMyStack() {
        final MyStack<String> stack = new MyStack<String>();
        System.out.println("Is empty stack empty? " + stack.isEmpty());
        stack.push("butts");
        System.out.println("Is 1 item stack empty? " + stack.isEmpty());
        stack.push("john lenin");
        System.out.println("Size of two item stack: " + stack.size());
        System.out.println("toString " + stack.toString());
        System.out.println("peek(): " + stack.peek());
        System.out.println("size (no additons since first size()): " + stack.size());
        System.out.println("pop(): " + stack.pop());
        System.out.println("size(): " + stack.size());
        System.out.println("pop(): " + stack.pop());
        System.out.println("size(): " + stack.size());
        System.out.println("pop(): " + stack.pop());
    }
    public static void testBurger(){
    	Burger berg = new Burger(true);
        System.out.println("A Baron " + berg.toString());
        berg.changePatties("Chicken");
        System.out.println("Now chicken baron " + berg.toString());
        berg = new Burger(false);
        System.out.println("A default " + berg.toString());
        berg.addCategory("Sauce");
        System.out.println("With sauce " + berg.toString());
        berg.removeCategory("Sauce");
        System.out.println("Removed sauce " + berg.toString());
        berg.addCategory("Cheese");
        System.out.println("With cheese " + berg.toString());
        berg.removeCategory("Cheese");
        System.out.println("Removed cheese " + berg.toString());
        berg.addCategory("Veggies");
        System.out.println("With veggies " + berg.toString());
        berg.removeCategory("Veggies");
        System.out.println("Removed veggies " + berg.toString());
        berg.addPatty();
        System.out.println("Two patty " + berg.toString());
        berg.addIngredient("Tomato");
        System.out.println("W/ tomato " + berg.toString());
        berg.addIngredient("Pepperjack");
        System.out.println("w/ pepperjack " + berg.toString());
        berg.removeIngredient("pepperjack");
        System.out.println("No pepperjack " + berg.toString());
        berg.removeIngredient("Tomato");
        System.out.println("No tomat " + berg.toString());
        berg = new Burger(false);
        berg.removeIngredient("Top-Bun");
        berg.removeIngredient("Beef");
        berg.removeIngredient("Bottom-Bun");
        System.out.println("Empty " + berg.toString());
        berg.addCategory("Cheese");
        System.out.println("I smeel cheese " + berg.toString());
        berg.removeCategory("cheese");
        berg.addIngredient("Mozzerella");
        System.out.println("I just want a slice of mozzerella " + berg.toString());
        berg.removeIngredient("mozzerella");
        berg.addPatty();
        System.out.println("I just want a fricking burger. Literally only a " + berg.toString());
        berg.addIngredient("PepperJack");
        berg.addPatty();
        System.out.println("instead of buns, " + berg.toString());
        berg.addIngredient("Cheddar");
        berg.addIngredient("Mozzerella");
        berg.addIngredient("Onions");
        System.out.println("barely a " + berg.toString());
        berg.addCategory("Cheese");
        System.out.println("More chhez please " + berg.toString());
        berg.removeCategory("Cheese");
        berg.removeIngredient("Onions");
        System.out.println("no more chhez please " + berg.toString());
    }
    
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // This test case generator generates more than 26 million test cases. It generates these
    // iteratively, producing every possible combination of a burger with 2 items in the first field
    // and 3 in the second field. It takes approximately half a minute to generate the test cases, but
    // my code takes just under an hour to process the test cases into burgers (while that runtime would
    // leave few developers thrilled, it's worth mentioning that that means my code processes 
    // about 8500 orders per second). 
    // NOTE: Due to the iterative nature of the generator, most cases are realistically nonsense.
    // The first order generated is "Baron Burger with no Pickle Pickle but Pickle Pickle Pickle". My code
    // can process this idiocy by way of accepting burgers with multiple of the same item, and disregarding
    // requests to remove ingredients not present on the burger.
    static class TestGenerator{
    	final HashMap<Integer, String> ings;
    	
    	TestGenerator(){
    		ings = new HashMap<Integer, String>();
    		ings.put(0, "Pickle"); ings.put(1, "Top-Bun"); ings.put(2, "Mayonnaise");
    		ings.put(3, "Baron-Sauce"); ings.put(4, "Lettuce"); ings.put(5, "Tomato");
    		ings.put(6, "Onions"); ings.put(7, "Pepperjack"); ings.put(8, "Mozzerella");
    		ings.put(9, "Cheddar"); ings.put(10, "Mushrooms"); ings.put(11, "Mustard");
    		ings.put(12, "Ketchup"); ings.put(13, "Bottom-Bun"); ings.put(14, "");
    		ings.put(15, "Cheese"); ings.put(16, "Veggies"); ings.put(17, "Sauce");
    	}
    	public void genTestCase(final String name) throws FileNotFoundException {
    		final PrintWriter writer = new PrintWriter(new File(name));
    		final StringBuilder s = new StringBuilder();
    		for(int baron = 0; baron < 2; baron++) { //Baron or not
    			for(int patNum = 0; patNum < 4; patNum++) { //# of patties
    				for(int patType = 0; patType < 3; patType++) {//Type of patties
    					for(int firstFieldOne = 0; firstFieldOne < 18; firstFieldOne++) { //Omissions and
    						for(int firstFieldTwo = 0; firstFieldTwo < 18; firstFieldTwo++) { //additions
        						for(int secondFieldOne = 0; secondFieldOne < 15; secondFieldOne++) {//Exceptions
        						for(int secondFieldTwo = 0; secondFieldTwo < 15; secondFieldTwo++) {
        						for(int secondFieldThree = 0; secondFieldThree < 15; secondFieldThree++) {
        							s.setLength(0);
            						if(patNum == 1) s.append("Double");
            						else if(patNum == 2) s.append("Triple");
            						else if(patNum == 3) s.append("Quadruple");
            						
            						if(patType == 1) s.append(" Chicken");
            						else if(patType == 2) s.append(" Veggie");
            						
            						if(baron == 0) s.append(" Baron Burger with no ");
            						else s.append(" Burger with ");
            						
            						s.append(ings.get(firstFieldOne) + " ");
            						s.append(ings.get(firstFieldTwo) + " ");
            						
            						if(baron == 0) s.append("but ");
            						else s.append("but no ");
            						
            						s.append(ings.get(secondFieldOne) + " ");
            						s.append(ings.get(secondFieldTwo)  + " ");
            						s.append(ings.get(secondFieldThree) + " ");
            						writer.println(s.toString());
            					}
            					}
        						}
        					}
    					}
    				}
    			}
    		}
    	}
    }
}
