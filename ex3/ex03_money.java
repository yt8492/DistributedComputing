import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

class ex03_money {
    static AccountType Account = new AccountType();
    static Semaphore M = new Semaphore(1);
    static CountDownLatch C = new CountDownLatch(2);

    static class AccountType {

        int account = 0;

        public int getValue() {
            return account;
        }

        synchronized void addOneUnit() {
            account++;
        }
    }

    static class Spouse implements Runnable {
        private int Sum;

        public Spouse(int sum) {
            Sum = sum;
        }

        public void run() {
            for (int i = 0; i < Sum; i++) {
                try {
                    M.acquire();
                } catch (Exception e) {
                }

                Account.addOneUnit();

                M.release();
            }

            C.countDown();
        }
    }

    static public void main(String args[]) {
        Spouse husband = new Spouse(500000);
        Spouse wife = new Spouse(500000);

        new Thread(husband).start();
        new Thread(wife).start();

        try {
            C.await();
        } catch (Exception e) {
        }

        System.out.println(Account.account);
    }
}
