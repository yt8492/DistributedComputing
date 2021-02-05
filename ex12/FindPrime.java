import java.math.BigInteger;

public class FindPrime {
    public static void main(String[] args) {
        int start = Integer.parseInt(args[0]);
        int end = Integer.parseInt(args[1]);
        // omp parallel for
        for (int i = start; i < end; i++) {
            if (BigInteger.valueOf(i).isProbablePrime(10)) {
                System.out.println(i);
            }
        }
    }
}
