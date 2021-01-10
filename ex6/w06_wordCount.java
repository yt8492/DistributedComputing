import mpi.Intracomm;
import mpi.MPI;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class w06_wordCount {
    public static void main(String[] args) throws Exception {
        MPI.Init(args);
        Intracomm comm = MPI.COMM_WORLD;
        int[] sum = new int[1];
        int[] max = new int[1];
        int rank = comm.Rank();
        File file = new File("file_" + rank + ".txt");
        String[] content = new BufferedReader(new InputStreamReader(new FileInputStream(file)))
                .lines().collect(Collectors.joining(" ")).split(" ");
        int wordCount = content.length;
        int longestWordLength = Arrays.stream(content).mapToInt(String::length).max().orElse(0);
        comm.Reduce(new int[] { wordCount }, 0, sum, 0, 1, MPI.INT, MPI.SUM, 0);
        comm.Reduce(new int[] { longestWordLength }, 0, max, 0, 1, MPI.INT, MPI.MAX, 0);
        if (rank == 0) {
            System.out.println(sum[0] + " " + max[0]);
        }
        MPI.Finalize();
    }
}
