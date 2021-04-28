import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class HTTPClient {
    static String name;
    Socket socket;
    BufferedReader bufferedReader;
    OutputStream outputStream;
    boolean alive = true;

    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";

    public HTTPClient(String name) {
        this.name = name;
    }

    public static void main(String[] args) {

        HTTPClient client = new HTTPClient(name);
        client.start();
    }

    public void start() {
        setUpNetworking();

        Thread t = new Thread(new InReader());
        t.start();

        try {
            System.out.print("NAME: ");
            Scanner scanner = new Scanner(System.in);
            name = scanner.nextLine();
            // outputStream.write(message.getBytes());

            String message = ANSI_PURPLE + name + ANSI_RESET + "\n";
            outputStream.write(message.getBytes());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while ((message = br.readLine()) != null) {
                if (message.length() == 0) {
                    outputStream.write("\n".getBytes());
                    break;
                }
                message = message + "\n";
                outputStream.write(message.getBytes());
            }
            alive = false;

            outputStream.close();
            System.out.println("Вы вышли из чата");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setUpNetworking() {
        try {
            socket = new Socket("192.168.43.91", 8080);
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            outputStream = socket.getOutputStream();
            System.out.println("Connect");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public class InReader implements Runnable {
        public void run() {
            String message;

            try {
                while ((message = bufferedReader.readLine()) != null && alive == true) {
                    System.out.println(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
