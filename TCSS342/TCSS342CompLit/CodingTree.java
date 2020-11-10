// Made by Eli Cole.

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class CodingTree {
	/*======================================================================================*/
	/*==*/// Set to true for fancy console output, false if you just want to compress a freaking file.
	/*==*/private final boolean fancy = true;
	/*==*/// The file name given to the passed file once encoded.
	/*==*/public final String encodedFileName = "compressedFile.txt";
	/*==*/// Name of the file containing the code dictionary
	/*==*/public final String codeDictFileName = "compressedFileCodes.txt";
	/*==*/// The file name given to the passed file once decoded.
	/*==*/public final String decodedFileName = "decompressedFile.txt";
	/*======================================================================================*/
	
	private final LinkedHashMap<Character, String> codes;
	
	public String bits;
	
	CodingTree(String message) throws IOException{
	    long startTime = System.currentTimeMillis();
		codes = new LinkedHashMap<Character, String>();
		buildHuffmanCodeDict(message);
		encodeHuffSansInfileDict(message);
		System.out.print(" with a run time of " + (System.currentTimeMillis() - startTime) + "ms");
	}
	
	//-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
	//-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
	// Generates a code dictionary for a given message by the Huffman method.
	private void buildHuffmanCodeDict(String message) {
		LinkedHashMap<Character, Integer> charFrequencies = new LinkedHashMap<Character, Integer>();
		MyPriorityQueue<CTreeNode<Character>> charTree = new MyPriorityQueue<CTreeNode<Character>>();
		
		/*======================================================================================*/
		/*==*/if(fancy)System.out.print("\nHuffman compression: Counting message chars...");
		/*======================================================================================*/
		
		// Counting the occurrences of chars in the message in charFrequencies.
		for (int i = 0; i < message.length(); i++) {
			if (charFrequencies.containsKey(message.charAt(i))) 
				charFrequencies.put(message.charAt(i), charFrequencies.get(message.charAt(i)) + 1);
			else charFrequencies.put(message.charAt(i), 1);
		}
		
		/*======================================================================================*/
		/*==*/if(fancy)System.out.println("\nTotal # of distinct characters: [" + charFrequencies.size() + "]");
		/*==*/if(fancy)System.out.print("\nHuffman compression: Building char tree...");
		/*======================================================================================*/
		
		// Assembling charTree from charFrequencies, and iteratively building the actual tree.
		charFrequencies.forEach((c,i) -> charTree.add(new CTreeNode<Character>(c, i)));
		while (charTree.size() > 1) charTree.add(new CTreeNode<Character>(charTree.poll(), charTree.poll(), null));
		
		/*======================================================================================*/
		/*==*/if(fancy)System.out.println("\nTotal # of characters: [" + charTree.peek().freq() + "]");
		/*======================================================================================*/
		
		// Adding all elements of the tree to codes.
		while(!charTree.isEmpty()) {
			CTreeNode<Character> root = charTree.poll();
			if(root.left() != null) charTree.add(root.left());
			if(root.right() != null) charTree.add(root.right());
			if(root.value() != null) codes.put(root.value(), root.loc());
		}
		
		/*======================================================================================*/
		/*==*/if(fancy)System.out.println("\nCompression dictionary:");
		/*==*/if(fancy)codes.forEach((c,s) -> System.out.println(c + " " + s));
		/*======================================================================================*/
	}
	
	//-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
	//-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
	// Encodes the message using the current code dictionary. Note: codes are kept in a seperate txt file. I may submit a version of this later
	// that stores the dictionary in the compressed file.
	private void encodeHuffSansInfileDict(String message) throws IOException {
		
		/*======================================================================================*/
		/*==*/if(fancy)System.out.println("\nHuffman compression: Encoding data...");
		/*======================================================================================*/
		
		DataOutputStream compStream = new DataOutputStream(new FileOutputStream(new File(encodedFileName)));
		PrintStream codeStream = new PrintStream(new File(codeDictFileName));
		// Building codes txt file.
		codes.forEach((c,s) -> {
			codeStream.print(c);
			codeStream.println(" " + s);
		});
		// Building compressed message txt file.
		StringBuilder messageBin = new StringBuilder();
		for(int i = 0; i < message.length(); i++) {
		    messageBin.append(codes.get(message.charAt(i)));
		}
		bits = messageBin.toString();
		compStream.write(convertBinStringToBytes(messageBin.toString()));
		compStream.close();
		float msgSize = (message.length() + 1023) / 1024;
		float cmpSize = (compStream.size() + 1023) / 1024;
		// Final console output.
		if(fancy)System.out.println("\nDone\n");
		System.out.println("Input file compressed as: " + encodedFileName);
		System.out.println("Code dictionary stored as: " + codeDictFileName);
		System.out.println("Original file size: " + Float.valueOf(msgSize).toString().replaceAll("\\.?0*$", "") + "kb");
		System.out.println("Compressed file size: " + Float.valueOf(cmpSize).toString().replaceAll("\\.?0*$", "") + "kb");
		System.out.println("Compression ratio: " + (cmpSize / msgSize * 100) + "%");
		System.out.print("\nHuffman encoding complete");
	}
	
	//-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-\
	//-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
	// Takes raw binary in string form and parses it into binary byte[] form.
	private byte[] convertBinStringToBytes(String message) {
	    // bs has a size of message.length / 8 (rounded up). 8 bits to every byte.
	    byte[] bs = new byte[(message.length() + 8 - 1) / 8 + 1];
	    byte byteVal = 0;
		for (int i = 0; i < message.length(); i++) {
		    // Alters byteVal to include a 1 or 0 at the position required. byteVal + ((1 or 0) * 2^x), x denotes where the 1 or 0 should go in the byte. 
			byteVal = (byte) (byteVal + ((message.charAt(i) - 48) * Math.pow(2, Math.abs((i % 8) - 7))));
			if(i % 8 == 7) {
			    bs[i / 8] = byteVal;
			    byteVal = 0;
			}
		}
		bs[bs.length - 2] = byteVal;
		byteVal = 0;
		// The final byte in the byte[] is simply a number denoting how many of the previous byte's bits were blank spaces, not zeroes.
		// e.g. a String of "10110" will be encoded as a byte[] {10110000, 00000011}
		bs[bs.length - 1] = (byte) (byteVal + (bs.length % 8));
		return bs;
	}
	
    //-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
	//-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
	// This is the optional decode method. It works, but has garbage run time.
    public void decode(String bits, LinkedHashMap<Character, String> codes) throws FileNotFoundException {
        
        /*======================================================================================*/
        /*==*/if(fancy)System.out.println("\nHuffman decompression: Decoding bitstring using passed dictionary...");
        /*======================================================================================*/
        
        long startTime = System.currentTimeMillis();
        LinkedHashMap<String, Character> codesButBackwards =  new LinkedHashMap<String, Character>();
        codes.forEach((c, s) -> codesButBackwards.put(s, c));
        PrintStream output =  new PrintStream(new File(decodedFileName));
        StringBuilder subBits = new StringBuilder();
        
        for(int i = 0; i < bits.length(); i++) {
        	subBits.append(bits.charAt(i));
        	if(codesButBackwards.containsKey(subBits.toString())) {
        		output.write(codesButBackwards.get(subBits.toString()));
        		subBits.setLength(0);
        	}
        }
        output.close();
        
        System.out.println("\nDone\n");
        System.out.print("Huffman decompression complete");
        System.out.print(" with a run time of " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("\nInput file decompressed as: " + decodedFileName);
    }
	
    //-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
    //-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
    // This method takes a file path containing codes of chars and their respective strings of ones and zeroes 
    // and turns it into a LinkedHashMap<Character, String>.
    public LinkedHashMap<Character, String> parseCodeFile(Path path) throws IOException {
        
        /*======================================================================================*/
        /*==*/if(fancy)System.out.println("\nHuffman decompression: Building internal decompression dictionary from external file...");
        /*======================================================================================*/
        
    	LinkedHashMap<Character, String> parsedCodes = new LinkedHashMap<Character, String>();
    	ByteArrayInputStream inStr = new ByteArrayInputStream(Files.readAllBytes(path));
    	int charHolder1;
    	int charHolder2;
    	StringBuilder codeHolder = new StringBuilder();
    	
    	while(inStr.available() > 0) {
    	    charHolder1 = inStr.read();
    	    inStr.skip(1);
    	    while((charHolder2 = inStr.read()) != 13) codeHolder.append((char) charHolder2);
    	    parsedCodes.put((char) charHolder1, codeHolder.toString());
    	    codeHolder.setLength(0);
    	    inStr.skip(1);
    	}
    	inStr.close();
    	
    	return parsedCodes;
    }
    
    //-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
    //-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
    // This method takes a file path containing compressed/encoded bits 
    // and turns them into a string of ones and zeroes representative of the bits.
    public String parseCompressedFile(Path path) throws IOException {
        
        /*======================================================================================*/
        /*==*/if(fancy)System.out.println("\n\nHuffman decompression: Parsing compressed file into readable bitstring...");
        /*======================================================================================*/
        
        ByteArrayInputStream inStr = new ByteArrayInputStream(Files.readAllBytes(path));
        StringBuilder str = new StringBuilder();
        
        while(inStr.available() > 1) {
            int no = inStr.read();
            str.append("00000000".substring(Integer.toBinaryString(no).length()) + Integer.toBinaryString(no));
        }
        inStr.close();
        
        return str.substring(0, str.length() - (int) inStr.read());
    }
    
	//-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
	//-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
	// Internal tree-node class, used for building a Huffman character tree. Stores a char value (null value indicates this is a branch, not a leaf),
    // as well as the frequency (weight) of this node and a String of ones and zeroes denoting its location in the tree.
    class CTreeNode<T> implements Comparable<CTreeNode<T>>{
    	final private T value;
    	private String loc;
    	private int freq;
    	final private CTreeNode<T> left;
    	final private CTreeNode<T> right;
    	
    	CTreeNode(T value, int freq){
    		this.left = null;
    		this.right = null;
    		this.value = value;
    		this.loc = "";
    		this.freq = freq;
    	}
    	CTreeNode(CTreeNode<T> left, CTreeNode<T> right, T value){
    		this.left = left;
    		this.right = right;
    		this.value = value;
    		this.setLoc("");
    		freq = 0;
    		if(left != null) freq = freq + left.freq();
    		if(right != null) freq = freq + right.freq();
    	}
    	
    	public void setLoc(String loc) {
    		this.loc = loc;
    		if (left != null) left.setLoc(loc + "0");
    		if (right != null) right.setLoc(loc + "1");
    	}
    	public CTreeNode<T> left() {return left;}
    	public CTreeNode<T> right() {return right;}
    	public T value() {return value;}
    	public String loc() {return loc;}
    	public int freq() {return freq;}
        @Override
        public int compareTo(CTreeNode<T> other) {return this.freq() - other.freq();}
        @Override
        public boolean equals(Object other) {return other instanceof CTreeNode<?> && this.value() == ((CTreeNode<?>) other).value();}
    }
    
    //-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
    //-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
    // A version of a Priority Queue, made for the bonus thingy.
    class MyPriorityQueue<T extends Comparable<T>> {
        ArrayList<T> values;
        
        MyPriorityQueue() {values = new ArrayList<T>();}
        
        public boolean isEmpty() {return values.isEmpty();}
        public int size() {return values.size();}
        public T poll() {return values.remove(0);}
        public T peek() {return values.get(0);}
        public void add(T element) {
            values.add(element);
            Collections.sort(values);
        }
    }
}
