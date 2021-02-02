import java.util.concurrent.atomic.AtomicInteger;

class ex03_money {
    static AtomicInteger Account = new AtomicInteger(0);

    static class Spouse implements Runnable {
        private int Sum;

        public Spouse(int sum) {
            Sum = sum;
        }

        public void run() {
            for (int i = 0; i < Sum; i++) {

                Account.incrementAndGet();

            }

        }
    }

    static public void main(String args[]) {
        Spouse husband = new Spouse(500000);
        Spouse wife = new Spouse(500000);

        Thread t1 = new Thread(husband);
        Thread t2 = new Thread(wife);
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(Account);
    }
}
