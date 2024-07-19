package lk.ijse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private transient String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' + // Password will not be serialized, so this will be null after deserialization
                '}';
    }

    public static void main(String[] args) {
        // Serialization (Client Side)
        new Thread(() -> {
            try (Socket socket = new Socket("localhost", 12345);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                User user = new User("john_doe", "password123");
                out.writeObject(user);
                System.out.println("User object sent to server");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Deserialization (Server Side)
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(12345);
                 Socket clientSocket = serverSocket.accept();
                 ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

                User receivedUser = (User) in.readObject();
                System.out.println("Received User object: " + receivedUser);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
