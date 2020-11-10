import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        long i = 1;
        long j = 1;
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println(i);
            j = i + j;
            System.out.println(j);
            i = i + j;
            s.next();
        }
    }
}
