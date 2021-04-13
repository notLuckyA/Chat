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

            System.out.print("Введите имя\n");

            String nick = null;
            String message;
            while ((message = br.readLine()) != null){
                if (nick == null){
                    nick = message;
                    System.out.print("--> " + nick + " вошёл в чат\n");
                } else {
                    System.out.println(nick + ": " +message);
                }
                if (message.equals("\n")){
                    break;
                }
            }
            System.out.print("<-- " + nick + " вышел из чата");
            output.close();
            input.close();
        }
    }
}
