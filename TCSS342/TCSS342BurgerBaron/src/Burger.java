//Coded by Elijah Cole

public class Burger {
    
	// I slapped together an enumeration for this for reasons, mainly because they're more efficient than
	// strings and they have useful methods like valueOf() and ordinal(). Ing is short for ingredient.
	public enum Ing{
		PICKLE, TOP_BUN, MAYONNAISE, BARON_SAUCE, LETTUCE, TOMATO, 
		ONIONS, PEPPERJACK, MOZZERELLA, CHEDDAR, PATTY_BEEF, PATTY_CHICKEN, 
		PATTY_VEGGIE, MUSHROOMS, MUSTARD, KETCHUP, BOTTOM_BUN;
	}
	
    private final MyStack<Ing> myBurger;
    // My implementation uses this field to remember what type of patties the burger contains.
    private Ing myPattyType;
    
    public Burger(final boolean theWorks) {
    	myBurger = new MyStack<Ing>();
    	if(theWorks) {
    		// burgerFlipper because the burger is initialized upside down.
    		final MyStack<Ing> burgerFlipper = new MyStack<Ing>();
    		for(Ing addition : Ing.values()) burgerFlipper.push(addition);
    		while(!burgerFlipper.isEmpty()) myBurger.push(burgerFlipper.pop());
    		// The constructor works by simply adding all Ings if theWorks. The following block is to
    		// remove the chicken and veggie patties added to the burger by this method.
    		changePatties("Beef");
    		removeIngredient("Beef");
    		removeIngredient("Beef");
    	} else {
    		myBurger.push(Ing.BOTTOM_BUN);
    		myBurger.push(Ing.PATTY_BEEF);
    		myBurger.push(Ing.TOP_BUN);
    	}
    	myPattyType = Ing.PATTY_BEEF;
    }
    
    // Note: pattyType doesn't have to be a type of patty. You can use this method to turn all patties
    // to tomatoes if you want.
    public void changePatties(final String pattyType) {
    	final MyStack<Ing> myBurgerHolder = new MyStack<Ing>();
    	myPattyType = parseStringToIng(pattyType);
    	while(!myBurger.isEmpty()) {
    		switch(myBurger.peek()) {
    		case PATTY_BEEF: case PATTY_CHICKEN: case PATTY_VEGGIE:
    			myBurgerHolder.push(myPattyType);
    			myBurger.pop();
    			break;
    		default:
    			myBurgerHolder.push(myBurger.pop());
    			break;
    		}
    	}
    	while(!myBurgerHolder.isEmpty()) myBurger.push(myBurgerHolder.pop());
    }
    
    // Note: addPatty will function incorrectly if there is cheese but no patties in the burger.
    // It will put the patty above the cheese in the stack. I created a solution for this,
    // but left it out because it measurably increased the big O of addPatty to solve a 
    // problem that will never realistically occur in the scope of this project.
    public void addPatty() {
    	final MyStack<Ing> myBurgerHolder = new MyStack<Ing>();
    	while(!myBurger.isEmpty()) {
    		if(myBurger.peek().name().substring(0, 5).equals("PATTY")
    				|| myBurger.peek() == Ing.PEPPERJACK
    				|| myBurger.peek() == Ing.MOZZERELLA
    				|| myBurger.peek() == Ing.CHEDDAR) {
    			myBurger.push(myPattyType);
    			while(!myBurgerHolder.isEmpty()) myBurger.push(myBurgerHolder.pop());
    			return;
    		} else myBurgerHolder.push(myBurger.pop());
    	}
    	// If these lines are reached, it means the return statement never triggered. This means the
    	// burger has neither patties nor cheese, so the cheese-on-bottom-patty rule doesn't matter
    	// and we can just use normal addIngsBeforeThese().
    	while(!myBurgerHolder.isEmpty()) myBurger.push(myBurgerHolder.pop());
    	final MyStack<Ing> onePattyStack = new MyStack<Ing>();
    	onePattyStack.push(myPattyType);
    	addTheseIngs(onePattyStack);
    }
    
    public void addCategory(final String type) {
    	final MyStack<Ing> additions = new MyStack<Ing>();
        switch(type.toUpperCase()) {
        case "CHEESE":
        	additions.push(Ing.CHEDDAR);
        	additions.push(Ing.MOZZERELLA);
        	additions.push(Ing.PEPPERJACK);
            this.addTheseCheese(additions);
            break;
        case "SAUCE":
        	additions.push(Ing.KETCHUP);
        	additions.push(Ing.MUSTARD);
        	additions.push(Ing.BARON_SAUCE);
        	additions.push(Ing.MAYONNAISE);
        	this.addTheseIngs(additions);
        	break;
        case "VEGGIES":
        	myBurger.push(Ing.PICKLE);
        	additions.push(Ing.MUSHROOMS);
        	additions.push(Ing.ONIONS);
        	additions.push(Ing.TOMATO); 
        	additions.push(Ing.LETTUCE); 
        	this.addTheseIngs(additions);
        	break;
        default:
        	break;
        }
    }
    
    public void removeCategory(final String type) {
    	final MyStack<Ing> myBurgerHolder = new MyStack<Ing>();
    	switch(type.toUpperCase()) {
    	case "CHEESE":
    		while(!myBurger.isEmpty()) {
    			if(myBurger.peek() == Ing.PEPPERJACK
    					|| myBurger.peek() == Ing.MOZZERELLA
    					|| myBurger.peek() == Ing.CHEDDAR) 
    				myBurger.pop();
    			else myBurgerHolder.push(myBurger.pop());
    		}
    		break;
    	case "SAUCE":
    		while(!myBurger.isEmpty()) {
    			if(myBurger.peek() == Ing.MAYONNAISE
    					|| myBurger.peek() == Ing.BARON_SAUCE
    					|| myBurger.peek() == Ing.MUSTARD
    					|| myBurger.peek() == Ing.KETCHUP) 
    				myBurger.pop();
    			else myBurgerHolder.push(myBurger.pop());
    		}
    		break;
    	case "VEGGIES":
    		while(!myBurger.isEmpty()) {
    			if(myBurger.peek() == Ing.PICKLE
    					|| myBurger.peek() == Ing.LETTUCE
    					|| myBurger.peek() == Ing.TOMATO
    					|| myBurger.peek() == Ing.ONIONS
    					|| myBurger.peek() == Ing.MUSHROOMS) 
    				myBurger.pop();
    			else myBurgerHolder.push(myBurger.pop());
    		}
    		break;
    	default:
    		break;
    	}
    	while(!myBurgerHolder.isEmpty()) myBurger.push(myBurgerHolder.pop());
    }
    
    public void addIngredient(final String type) {
    	final Ing typeAsIng = parseStringToIng(type);
        final MyStack<Ing> typeAsStack = new MyStack<Ing>();
        typeAsStack.push(typeAsIng);
        // I like switch statements.
        switch(typeAsIng) {
        case PEPPERJACK: case MOZZERELLA: case CHEDDAR:
        	addTheseCheese(typeAsStack);
        	break;
        // The following patty case makes it so you can use addIngredient() to put two
        // different types of patty on your burger.
        case PATTY_CHICKEN: case PATTY_BEEF: case PATTY_VEGGIE:
        	Ing pattyTypeHolder = myPattyType;
        	myPattyType = typeAsIng;
        	addPatty();
        	myPattyType = pattyTypeHolder;
        	break;
        default:
        	addTheseIngs(typeAsStack);
        	break;
        }
    }
    
    public void removeIngredient(final String type) {
    	final MyStack<Ing> myBurgerHolder = new MyStack<Ing>();
    	Ing typeAsIng = parseStringToIng(type);
    	
    	while(!myBurger.isEmpty()) {
    		if(myBurger.peek() == typeAsIng) {
    			myBurger.pop();
    			// This line ends the method as soon as the type is found.
    			// Incidentally, it means this method only removes one ingredient,
    			// so if there's multiple, some will remain. This is largely irrelevant as
    			// doubled ingredients don't exist in the domain of the problem.
    			while(!myBurgerHolder.isEmpty()) myBurger.push(myBurgerHolder.pop());
    			return;
    		}
    		else myBurgerHolder.push(myBurger.pop());
    	}
    	while(!myBurgerHolder.isEmpty()) myBurger.push(myBurgerHolder.pop());
    }
    
    @Override
    public String toString() {
    	return "Burger: [" + myBurger.toString() + "]";
    }
    
    // Special utility method to add a stack of Ings to myBurger in their proper places.
    // The existence of ordinal() for enumerations means I can add Ings to myBurger in a zipper-like fashion,
    // between ingsToAdd and myBurger, the one with the smaller ordinal on top gets pushed to myBurgerHolder.
    // Incidentally, ingsToAdd must be properly ordered (top to bottom) for the method to work. Also, ingsToAdd is a
    // MyStack and not a single Ing so addCategory only has to call this method once.
    private void addTheseIngs(final MyStack<Ing> ingsToAdd) {
    	final MyStack<Ing> myBurgerHolder = new MyStack<Ing>();
    	while(!myBurger.isEmpty() && !ingsToAdd.isEmpty()) {
    		if(myBurger.peek().ordinal() < ingsToAdd.peek().ordinal()) myBurgerHolder.push(myBurger.pop());
    		else myBurgerHolder.push(ingsToAdd.pop());
    	}
    	while(!ingsToAdd.isEmpty()) myBurgerHolder.push(ingsToAdd.pop());
    	while(!myBurgerHolder.isEmpty()) myBurger.push(myBurgerHolder.pop());
    }
    
    // A special version of addTheseIngs() to negotiate the admittedly rather complicated
    // cheese-on-the-bottom-patty rule. Takes a stack of exclusively cheeses to be added to myBurger.
    // It first moves all cheese from myBurger to cheeseToAdd and counts the number of patties,
    // then sorts cheeseToAdd by ordinal, then finally finds the proper place to put the cheeses based on
    // how many patties the burger has (if the burger has 3 patties, it knows to add the cheese after two patties
    // are popped).
    private void addTheseCheese(final MyStack<Ing> cheeseToAdd) {
    	final MyStack<Ing> myBurgerHolder = new MyStack<Ing>();
    	// The no of patties on myBurger
    	int pattyCount = 0;
    	// This block removes all cheeses from myBurger and adds it into cheeseToAdd,
    	// so it can all be returned to myBurger at once. It also counts how many patties
    	// the burger has.
    	while(!myBurger.isEmpty()) {
    		if(myBurger.peek().ordinal() >= 7 && myBurger.peek().ordinal() <= 9) 
    			cheeseToAdd.push(myBurger.pop());
    		else {
    			if(myBurger.peek().name().substring(0, 5).equals("PATTY")) pattyCount++;
    			myBurgerHolder.push(myBurger.pop());
    		}
    	}
    	while(!myBurgerHolder.isEmpty()) myBurger.push(myBurgerHolder.pop());
    	
    	// Sorts cheeseToAdd, using myBurgerHolder so I don't have to initialize another MyStack.
    	while (!cheeseToAdd.isEmpty()) { 
    	        Ing cheeseHolder = cheeseToAdd.pop();
    	        while (!myBurgerHolder.isEmpty() && (myBurgerHolder.peek().ordinal() > cheeseHolder.ordinal())) 
    	            cheeseToAdd.push(myBurgerHolder.pop());
    	        myBurgerHolder.push(cheeseHolder);
    	}
    	while(!myBurgerHolder.isEmpty()) cheeseToAdd.push(myBurgerHolder.pop());
    	
    	// Finds the bottom patty and adds all cheese on top of it. 
    	// Just calls addTheseIngs() if the burger has 1 or 0 patties.
    	if(pattyCount == 0 || pattyCount == 1) {
    		addTheseIngs(cheeseToAdd);
    		return;
    	} else {
    		while(!myBurger.isEmpty()) {
    			if(myBurger.peek().name().substring(0, 5).equals("PATTY")) pattyCount--;
    			if(pattyCount == 0) {
    				while(!cheeseToAdd.isEmpty()) myBurgerHolder.push(cheeseToAdd.pop());
    				while(!myBurgerHolder.isEmpty()) myBurger.push(myBurgerHolder.pop());
    				return;
    			}
    			myBurgerHolder.push(myBurger.pop());
    		}
   		}
    }
    
    // Utility method. Takes a string and turns it into an Ing. 
    private Ing parseStringToIng(final String type) {
    	switch(type.toUpperCase()) {
    	case "CHICKEN": 
    		return Ing.PATTY_CHICKEN;
    	case "BEEF": 
    		return Ing.PATTY_BEEF;
    	case "VEGGIE": 
    		return Ing.PATTY_VEGGIE;
    	case "BARON-SAUCE": 
    		return Ing.BARON_SAUCE;
    	case "TOP-BUN":
    		return Ing.TOP_BUN;
    	case"BOTTOM-BUN":
    		return Ing.BOTTOM_BUN;
    	default: 
    		return Ing.valueOf(type.toUpperCase());
    	}
    }
}
