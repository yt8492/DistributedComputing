import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class WordGuessingGameRMI {
    public static void main(String[] args) {
        try {
            if (args[0].equals("server")) {
                Naming.rebind("GameService", new RMIGameServerImpl());
                System.out.println("server start");
            } else {
                Scanner sc = new Scanner(System.in);
                RMIGameServer server = (RMIGameServer) Naming.lookup("//127.0.0.1/GameService");
                int id = server.initGame();
                while (true) {
                    System.out.print("client> ");
                    char input = sc.nextLine().charAt(0);
                    String result = server.showWord(input, id);
                    System.out.println("server> " + result);
                    if (!result.contains("*")) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

interface RMIGameServer extends Remote {
    int initGame() throws RemoteException;
    String showWord(char nextChar, int gameId) throws RemoteException;
}

class RMIGameServerImpl extends UnicastRemoteObject implements RMIGameServer {

    private static final List<String> words = Arrays.asList("book", "block", "follow");
    private final Map<Integer, GameSession> sessions = new TreeMap<>();
    private int id = 0;

    public RMIGameServerImpl() throws RemoteException {}

    @Override
    public synchronized int initGame() throws RemoteException {
        String word = words.get(new Random().nextInt(words.size()));
        sessions.put(id, new GameSession(word));
        return id++;
    }

    @Override
    public String showWord(char nextChar, int gameId) throws RemoteException {
        GameSession session = sessions.get(gameId);
        String result = session.checkChar(nextChar);
        if (!result.contains("*")) {
            sessions.remove(gameId);
        }
        return result;
    }
}

class GameSession {
    private final String word;
    private final List<Character> hitChars = new ArrayList<>();

    public GameSession(String word) {
        this.word = word;
    }

    public String checkChar(char input) {
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
        String result = resultBuilder.toString();
        return result;
    }
}
