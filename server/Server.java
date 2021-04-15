import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

    public static class ClientThread extends Thread {
        private Socket clientSocket;

        public ClientThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                OutputStream output = clientSocket.getOutputStream();
                InputStream input = clientSocket.getInputStream();
                BufferedReader br = new BufferedReader((new InputStreamReader(input)));

                Date dt = new Date();
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm:ss");

                String nick = br.readLine();
                System.out.println("--> " + formatForDateNow.format(dt) + " " + nick + " вошёл в чат");
                String message;
                while ((message = br.readLine()) != null) {

                   dt = new Date();

                    if (message.equals("\n")) {
                        break;
                    }
                    System.out.println(formatForDateNow.format(dt) + "<" + nick + ">: " + message);
                }
                System.out.print("<-- " + formatForDateNow.format(dt) + " " + nick + " вышел из чата");
                output.close();
                input.close();
            } catch (IOException e) {
                System.out.println("Error" + e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            Socket clientSocket = serverSocket.accept();

            ClientThread t = new ClientThread(clientSocket);
            t.start();
        }
    }
}