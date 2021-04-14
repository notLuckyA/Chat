import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class HTTPClient {
    public static void main(String[] args) throws Exception {
        Socket sock = new Socket("192.168.43.91",
                8080);

        OutputStream output = sock.getOutputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("NAME: ");
        Scanner scanner = new Scanner(System.in);
        String nick = scanner.nextLine();


        String message;
        message = nick + "\n";
        //System.out.print(nick + "\n");
        output.write(message.getBytes());

        while ((message = br.readLine()) != null) {
            if (message.length() == 8) {
                output.write("\n".getBytes());
                break;
            }

            message = message + "\n";
            output.write(message.getBytes());
        }
        output.close();

    }
}
