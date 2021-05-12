import javax.xml.namespace.QName;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class ChatClient {

   static String name;
    String color;
    Socket socket;
    BufferedReader bufferedReader;
    OutputStream outputStream;
    boolean alive = true;

    String ANSI_PINK = "\u001b[";

    public ChatClient(String name) {
        this.color = generateColor();
        this.name = generateColor() + name + "\u001B[0m";
    }

    public String generateColor() {
        int color;
        if (Math.random() * 10 > 5) {
            color = (int) (Math.random() * 8 + 90);
        } else {
            color = (int) (Math.random() * 8 + 30);
        }

        return ANSI_PINK + color + "m";
    }

    public static void main(String[] args) {

        ChatClient client = new ChatClient(name);
        client.start();
    }

    public void start() {
        setUpNetworking();

        Thread t = new Thread(new InReader());
        t.start();

        try {
            Scanner scanner = new Scanner(System.in);
            name = scanner.nextLine();
            String message = name + "\n";
            outputStream.write(message.getBytes());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while ((message = br.readLine()) != null) {
                printMessage("> ");
                if (message.length() == 0) {
                    continue;
                } else if (message.equals("/quit")) {
                    message = message + "\n";
                    outputStream.write(message.getBytes());
                    break;
                } else {
                    message = message + "\n";
                    outputStream.write(message.getBytes());
                }
            }
            alive = false;

            outputStream.close();
            printMessage("Вы вышли из чата\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // private static Semaphore sem = new Semaphore(1);
    private static synchronized void printMessage(String message) {
        // try {
        //     sem.acquire();
        System.out.print(message);
        //     sem.release();
        // } catch(Exception e) {

        // }
    }

    private void setUpNetworking() {
        try {
            socket = new Socket("192.168.43.91", 8080);
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            outputStream = socket.getOutputStream();
            printMessage("подключение установлено\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public class InReader implements Runnable {

        public void run() {
            String message;
            try {
                while ((message = bufferedReader.readLine()) != null && alive == true) {
                    printMessage(message + "\n> ");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
