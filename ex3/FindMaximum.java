import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class FindMaximum {

    static class FindThread implements Runnable {
        public int result;
        private final int[] array;
        private final int startInclusive;
        private final int endExclusive;
        private final CountDownLatch latch;

        public FindThread(int[] array, int startInclusive, int endExclusive, CountDownLatch latch) {
            this.array = array;
            this.startInclusive = startInclusive;
            this.endExclusive = endExclusive;
            this.latch = latch;
        }

        @Override
        public void run() {
            int max = array[startInclusive];
            for (int i = startInclusive; i < endExclusive; i++) {
                if (array[i] > max) {
                    max = array[i];
                }
            }
            result = max;
            latch.countDown();
        }
    }

    public static void main(String[] args) throws Exception {
        int n = 100;
        int[] array = new int[n];
        CountDownLatch latch = new CountDownLatch(4);
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(400);
        }
        List<FindThread> threads = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            threads.add(new FindThread(array, i * n / 4, i * n / 4 + 1, latch));
        }
        threads.forEach(t -> new Thread(t).start());
        latch.await();
        int max = max(
                threads.get(0).result,
                threads.get(1).result,
                threads.get(2).result,
                threads.get(3).result
        );
        System.out.println(max);
    }

    private static int max(int... args) {
        int max = args[0];
        for (int a : args) {
            if (a > max) {
                max = a;
            }
        }
        return max;
    }
}
