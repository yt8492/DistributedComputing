import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class HeadsOrTails {

    static class Process implements Runnable {

        private final Random random = new Random();
        public final int id;
        private final Semaphore s;
        boolean result; // ture is heads, false is tails

        public Process(int id, Semaphore s) {
            this.id = id;
            this.s = s;
        }

        @Override
        public void run() {
            result = random.nextBoolean();
            s.release();
        }
    }

    public static void main(String[] args) {
        Semaphore s = new Semaphore(0);
        int p = 4;
        List<Process> processes = new ArrayList<>();
        for (int i = 1; i <= p; i++) {
            processes.add(new Process(i, s));
        }
        int round = 1;
        while (true) {
            processes.forEach(t -> new Thread(t).start());
            processes.forEach((t) -> {
                try {
                    s.acquire();
                } catch (Exception e) {
                }
            });
            processes.removeIf(t -> !t.result);
            long winners = processes.stream().filter(t -> t.result).count();
            System.out.println("round " + round + " winners");
            if (winners >= 1) {
                System.out.println(
                        processes.stream().map(t -> String.valueOf(t.id)).collect(Collectors.joining(" "))
                );
            } else {
                System.out.println("none");
            }
            if (winners <= 1) {
                break;
            }
            round++;
        }
        String winner = processes.stream().findFirst().map(w -> String.valueOf(w.id)).orElse("none");
        System.out.println("winner: " + winner);
    }
}
