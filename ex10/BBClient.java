import java.rmi.Naming;
import java.util.Scanner;

public class BBClient {
    public static void main(String[] args) {
        String mode = args[0];
        try {
            Board board = (Board) Naming.lookup("//127.0.0.1/BBService");
            if (mode.equals("add")) {
                Scanner sc = new Scanner(System.in);
                String message = sc.nextLine();
                board.addMessage(message);
            } else if (mode.equals("read")) {
                System.out.println(board.showMessages());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
