import java.util.Arrays;
import java.util.Random;

public class QuickSort {

    public static void main(String[] args) {
        Random random = new Random();
        int[] array = new int[1000000];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100000);
        }
        long start = System.currentTimeMillis();
        quickSort(array, 0, array.length - 1);
        long end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");
    }

    static void quickSort(int[] array, int left, int right) {
        if (left < right) {
            int p = partition(array, left, right);
            if (p - left >= array.length / 30 && right - p >= array.length) {
                // omp sections
                {
                    // omp section
                    {
                        quickSort(array, left, p - 1);
                    }
                    // omp section
                    {
                        quickSort(array, p + 1, right);
                    }
                }
            } else {
                quickSort(array, left, p - 1);
                quickSort(array, p + 1, right);
            }
        }
    }

    static int partition(int[] array, int left, int right) {
        int pivot = array[right];
        int i = left;
        for (int j = left; j < right; j++) {
            if (array[j] < pivot) {
                int tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
                i++;
            }
        }
        int tmp = array[i];
        array[i] = array[right];
        array[right] = tmp;
        return i;
    }
}
