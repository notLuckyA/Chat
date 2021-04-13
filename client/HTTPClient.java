import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HTTPClient {
    public static void main(String[] args) throws Exception{
        Socket sock = new Socket("192.168.43.91",
                8080);

        OutputStream output = sock.getOutputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String message;
        String nick = null;
        while ((message = br.readLine()) != null){
            if (message.length() == 8){
                output.write("\n".getBytes());
                break;
            }
            if (nick == null){
                nick = message;
            }
            message = message + "\n";
            output.write(message.getBytes());
        }
        output.close();

    }
}
