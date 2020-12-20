import java.math.BigInteger;
import java.util.Scanner;

public class CheckPrime {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int p = scanner.nextInt();
        for (int i = 0; i < p; i++) {
            int start = n / p * i;
            int end = Math.min(n / p * (i + 1), n);
            new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if (BigInteger.valueOf(j).isProbablePrime(100)) {
                        System.out.println(j);
                    }
                }
            }).start();
        }
    }
}
