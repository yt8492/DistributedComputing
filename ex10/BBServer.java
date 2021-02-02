import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BBServer {
    public static void main(String[] args) {
        try {
            Naming.rebind("BBService", new BoardImpl());
            System.out.println("server start");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

interface Board extends Remote {
    void addMessage(String msg) throws RemoteException;
    String showMessages() throws RemoteException;
}

class BoardImpl extends UnicastRemoteObject implements Board {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    private final List<Message> messages = Collections.synchronizedList(new ArrayList<>());

    public BoardImpl() throws RemoteException {}

    @Override
    public void addMessage(String msg) throws RemoteException {
        Message message = new Message(msg);
        messages.add(message);
    }

    @Override
    public String showMessages() throws RemoteException {
        return messages.stream()
                .map(m -> sdf.format(m.createdAt) + '\n' + m.body + '\n')
                .collect(Collectors.joining("\n"));
    }
}

class Message {
    final String body;
    final Date createdAt;

    public Message(String body) {
        this.body = body;
        createdAt = new Date();
    }
}
