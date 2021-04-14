import java.io.*;
import java.net.*;
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true){
            Socket clientSocket = serverSocket.accept();

            OutputStream output = clientSocket.getOutputStream();
            InputStream input = clientSocket.getInputStream();
            BufferedReader br = new BufferedReader((new InputStreamReader(input)));

            String nick = br.readLine();
            System.out.println("--> " + nick + " вошёл в чат");


            String message;
            while ((message = br.readLine()) != null){
                if (message.equals("\n")){
                    break;
                }
                System.out.println(nick + ": " + message);
            }
            System.out.print("<-- " + nick + " вышел из чата");
            output.close();
            input.close();
        }
    }
}
