//Coded by Elijah Cole

public class MyStack<T> {
    
    private Node<T> myTop;
    
    public MyStack() {myTop = new Node<T>();}
    
    public boolean isEmpty() {return myTop.isBottom();}
    
    public void push(final T item) {myTop = new Node<T>(item, myTop);}
    public T pop() {
        if(myTop.isBottom()) return null;
        final T topValueHolder = myTop.getValue();
        myTop = myTop.getNode();
        return topValueHolder;
    }
    public T peek() {return myTop.getValue();}
    
    public int size() {
    	int count = 0;
    	final MyStack<T> holderStack = new MyStack<T>();
    	while(!this.isEmpty()) {
    		holderStack.push(this.pop());
    		count++;
    	}
    	while(!holderStack.isEmpty()) this.push(holderStack.pop());
    	return count;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        MyStack<T> holderStack = new MyStack<T>();
        // Moves all nodes from this to a temporary holder stack 
        // while appending values to the string builder,
        while(!this.isEmpty()) {
            if(this.peek() == null) sb.append("NULL_VALUE");
            else sb.append(this.peek().toString());
            sb.append(", ");
            holderStack.push(this.pop());
        }
        // Then moves all values back.
        while(!holderStack.isEmpty()) this.push(holderStack.pop());
        return sb.toString();
    }
    // clone(), made by my own volition. Returns a deep copy of this MyStack object.
    /*@Override
    public MyStack<T> clone(){
        MyStack<T> holderStack = new MyStack<T>();
        MyStack<T> cloneStack = new MyStack<T>();
        while(!this.isEmpty()) {holderStack.push(this.pop());}
        while(!holderStack.isEmpty()) {
            this.push(holderStack.peek());
            cloneStack.push(holderStack.pop());
        }
        return cloneStack;
    }*/
    
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // The internal node class. The type is called V and not T for clarity,
    // because MyStack uses T and is in the same file.
    class Node<V> {
        private final V myValue;
        // My implementation of Node works by each Node holding the subsequent Node
        // as myPointer, like babushka dolls. The smallest babushka is the bottom
        // of the MyStack.
        private final Node<V> myPointer;
        private final boolean ifImBottom;
        
        private Node(final V o, final Node<V> n) {
            myValue = o;
            myPointer = n;
            ifImBottom = false;
        }
        // All stacks have a bottom denoted by a Node with null values
        // and a true ifImBottom. This is done to prevent jank when
        // popping an empty stack: the bottom Node cannot be popped.
        private Node() {
            myValue = null;
            myPointer = null;
            ifImBottom = true;
        }
        
        private V getValue() {return myValue;}
        private Node<V> getNode() {return myPointer;}
        private boolean isBottom() {return ifImBottom;}
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}


