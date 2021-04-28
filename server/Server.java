import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Server {
    static ArrayList<ClientThread> outputStreams;

    public static final String YELLOW = "\u001B[33m";
    public static final String RESET = "\u001B[0m";

    public class ClientThread extends Thread {
        private Socket clientSocket;
        OutputStream output;
        InputStream input;

        public ClientThread(Socket clientSocket) throws Exception {
            this.clientSocket = clientSocket;
            this.output = clientSocket.getOutputStream();
            this.input = clientSocket.getInputStream();
        }

        public OutputStream getOutput() {
            return this.output;
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader((new InputStreamReader(input)));
                Date dn = new Date();
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm:ss");

                String nick = br.readLine();
                tellAll(formatForDateNow.format(dn) + " " + nick + " вошёл в чат");
                System.out.println(formatForDateNow.format(dn) + " " + nick + " вошёл в чат");
                String message;
                while ((message = br.readLine()) != null) {

                    dn = new Date();

                    if (message.equals("\n")) {
                        break;
                    }
                    System.out.println(formatForDateNow.format(dn) + " " + "<" + YELLOW + nick + RESET + ">: " + message);
                    tellAll(formatForDateNow.format(dn) + " " + "<" + YELLOW + nick + RESET + ">: " + message);
                }
                System.out.print(formatForDateNow.format(dn) + " " + nick + " вышел из чата");
                tellAll(formatForDateNow.format(dn) + " " + " " + nick + " вышел из чата");
                output.close();
                input.close();
                outputStreams.remove(this);
            } catch (IOException e) {
                System.out.println("Error" + e);
            }
        }
    }

    public void tellAll(String message) {
        Iterator iterator = outputStreams.iterator();

        while (iterator.hasNext()) {
            try {
                ClientThread t = (ClientThread) iterator.next();
                OutputStream out = t.getOutput();
                out.write(message.getBytes());
                out.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Server().start();
    }

    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        outputStreams = new ArrayList<>();
        while (true) {
            Socket clientSocket = serverSocket.accept();

            ClientThread t = new ClientThread(clientSocket);
            t.start();
        }
    }
}