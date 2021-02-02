import java.util.ArrayList;
import java.util.Vector;
import java.util.stream.IntStream;

public class CompareCollection {
    public static void main(String[] args) {
        Vector<Integer> vector = new Vector<>();
        ArrayList<Integer> list = new ArrayList<>();
        IntStream.range(0, 100000).forEach(i -> {
            vector.add(0);
            list.add(0);
        });
        long time1 = System.nanoTime();
        for (int i = 0; i < list.size(); i++) {
            vector.set(i, i);
        }
        long time2 = System.nanoTime();
        for (int i = 0; i < vector.size(); i++) {
            list.set(i, i);
        }
        long time3 = System.nanoTime();
        System.out.println("Vector: " + (time2 - time1) + "ns");
        System.out.println("ArrayList: " + (time3 - time2) + "ns");
    }
}
