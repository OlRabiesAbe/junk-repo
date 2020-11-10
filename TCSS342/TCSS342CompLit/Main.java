// Made by Eli Cole.

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args)throws IOException {
	    String fileName = "OldManAndTheSea.txt";
	    System.out.println("Input file: " + fileName);
	    String fileString = new String(Files.readAllBytes(Paths.get(fileName)));
	    CodingTree tree = new CodingTree(fileString);
        tree.decode(tree.parseCompressedFile(Paths.get(tree.encodedFileName)), tree.parseCodeFile(Paths.get(tree.codeDictFileName)));
	}
	
	@SuppressWarnings("unused")
	private static void testCodingTree() throws IOException {
		String isshin = "Smart thinking my boy! Let me see...That's some good sake! This is how we drank when we won our battles. "
				+ "The people of Ashina, together as one. Oh, the rebellion...we just took back what was stolen from us. "
				+ "Before, this land was...it was a place where we, the Ashina people lived. "
				+ "Where the waters flowed, straight from the source. "
				+ "We were a people who loved our country dearly. And we made good sake to boot! But we were heretics, and we were weak. "
				+ "Naturally, we were overrun. Trampled into submission. "
				+ "For many long, excruciating years...we couldn't even pray at the water from the springs. "
				+ "The way we were then...even good sake couldn't get us truly drunk. "
				+ "Yes...amidst the chaos that was Japan...the endless casualties. "
				+ "The flames of war...we found the perfect opportunity to take back our land. "
				+ "But now...it's a place of death...it's a bitter thing indeed.";
		CodingTree tree = new CodingTree(isshin);
        tree.decode(tree.parseCompressedFile(Paths.get("compressedFile.txt")), tree.parseCodeFile(Paths.get("compressedFileCodes.txt")));
	}
}


//This is nothing, ignore.
/*int i = 77;
System.out.println(4 > 2 ? "yeh" : "neh"); //Conditional: "condition" ? "in true case" : "in false case"
System.out.println(4 < 2 ? "yeh" : "neh");
System.out.println(i <<= 3); //i * 2 a certain number of times. Also assigns i new val, like i++. i = i * 2 * 2 * 2;
System.out.println(i & 615); // compares two ints bit by bit with and operator. basically magic number changing machine.
System.out.println(11 / 2);
System.out.println((11 + 2 - 1) / 2);*/