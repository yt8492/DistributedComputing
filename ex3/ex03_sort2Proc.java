import java.util.*;
import java.util.concurrent.CountDownLatch;

class ex03_sort2Proc {
    static final int N = 100000;
    static int A[] = new int[N];
    static CountDownLatch C = new CountDownLatch(2);

    static class SortThread implements Runnable {
        int Left, Right;

        public SortThread(int left, int right) {
            Left = left;
            Right = right;
        }

        public void run() {
            Arrays.sort(A, Left, Right);
            C.countDown();
        }
    }

    static public void main(String args[]) {
        Random rand = new Random();
        for (int i = 0; i < N; ++i)
            A[i] = rand.nextInt(1000);

        new Thread(new SortThread(0, N / 2)).start();
        new Thread(new SortThread(N / 2, N)).start();

        try {
            C.await();
        } catch (Exception e) {
        }

        // here add merging code
        int l = 0;
        int r = N / 2;
        int a = 0;
        int[] sorted = new int[N];
        while (a < N) {
            if (l >= N / 2) {
                while (r < N) {
                    sorted[a] = A[r];
                    a++;
                    r++;
                }
                break;
            } else if (r >= N) {
                while (l < N / 2) {
                    sorted[a] = A[l];
                    a++;
                    l++;
                }
                break;
            }
            int leftHead = A[l];
            int rightHead = A[r];
            if (leftHead < rightHead) {
                sorted[a] = leftHead;
                l++;
            } else {
                sorted[a] = rightHead;
                r++;
            }
            a++;
        }
        for (int value : sorted) {
            System.out.print(value + " ");
        }
    }
}

