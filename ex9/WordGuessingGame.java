import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class WordGuessingGame {
    public static void main(String[] args) throws Exception {
        String mode = args[0];
        if (mode.equalsIgnoreCase("client")) {
            String host = args[1];
            int port = Integer.parseInt(args[2]);
            client(host, port);
        } else {
            int port = Integer.parseInt(args[1]);
            server(port);
        }
    }

    private static void client(String host, int port) throws Exception {
        Socket client = new Socket(host, port);
        Scanner sc = new Scanner(System.in);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while (true) {
            String line = reader.readLine();
            try {
                System.out.println("server> " + line);
                if (!line.contains("*")) {
                    client.close();
                    break;
                }
                System.out.print("client> ");
                String input = sc.nextLine();
                writer.write(input);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void server(int port) throws Exception {
        ServerSocket s = new ServerSocket(port);
        System.out.println("server start");
        while (true) {
            Socket client = s.accept();
            new ServerSession(client).start();
        }
    }
}

class ServerSession {

    private static final List<String> words = Arrays.asList("book", "block", "follow");

    private final Socket client;
    private final String word = words.get(new Random().nextInt(words.size()));
    private final List<Character> hitChars = new ArrayList<>();

    public ServerSession(Socket client) {
        this.client = client;
    }

    public void start() {
        new Thread(() -> {
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                String initial = String.join("", Collections.nCopies(word.length(), "*"));
                writer.write(initial);
                writer.newLine();
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while (true) {
                    char input = reader.readLine().charAt(0);
                    if (!hitChars.contains(input) && word.contains(String.valueOf(input))) {
                        hitChars.add(input);
                    }
                    StringBuilder resultBuilder = new StringBuilder();
                    for (char c : word.toCharArray()) {
                        if (hitChars.contains(c)) {
                            resultBuilder.append(c);
                        } else {
                            resultBuilder.append('*');
                        }
                    }
                    try {
                        String result = resultBuilder.toString();
                        writer.write(result);
                        writer.newLine();
                        writer.flush();
                        if (!result.contains("*")) {
                            client.close();
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
