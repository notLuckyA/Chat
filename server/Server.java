
import java.net.*;
import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


public class Server {
    static ArrayList<ClientThread> outputStreams;
    static ArrayList<String> history;
    static FileOutputStream fileOutput;
    static ArrayList<String> help;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    ArrayList<String> users = new ArrayList<String>();

    public class ClientThread extends Thread {
        private Socket clientSocket;
        OutputStream output;
        InputStream  input;
        // Конструктор класса, принимающий в качестве аргумента сокет клиента
        // для взаимодействия с ним.
        public ClientThread(Socket clientSocket) throws Exception {
            this.clientSocket = clientSocket;
            this.output = clientSocket.getOutputStream();
            this.input  = clientSocket.getInputStream();
        }

        public OutputStream getOutput() {
            return this.output;
        }
        // Этот метод вызывается автоматически при вызове у потока метода
        // "start".
        public void run() {
            // Это будет выполняться в отдельном потоке
            try {
                BufferedReader br = new BufferedReader( new InputStreamReader(input) );
                String nick = br.readLine();
                users.add(nick);
                System.out.println(users);
                historyOutput(output);
                tellAll("--> " + nick + " вошёл/вошла в чат" + "\n");
                System.out.println("--> " + nick + " вошёл/вошла в чат");

                String message;
                while ((message = br.readLine()) != null) {

                    if (message.equals("/quit")) {
                        System.out.println(message);
                        break;
                    }

                    LocalDateTime now = LocalDateTime.now();
                    System.out.println(dtf.format(now) + " " + nick + ": " + message);

                    if (message.equals("/users")) {
                        tellOne("Все пользователи в чате = " + users + "\n", output);
                    } else if (message.equals("/help")) {
                        tellOne("Возможные команды:\n", output);
                        for (String s : help) {
                            tellOne(s, output);
                        }
                        tellOne("\n", output);
                    } else {
                        tellAll(nick + ": " + message + "\n");
                    }

                }

                System.out.println("<-- " + nick + " покинул(а) чат");
                tellAll("<-- " + nick + " покинул(а) чат" + "\n");

                users.remove(nick);
                output.close();
                input.close();
                outputStreams.remove(this);
            } catch (Exception e) {
                System.out.println("ERROR: " + e);
            }
        }
    }

    public void historyOutput(OutputStream output) {
        tellOne("\n", output);
        tellOne("  --------история чата--------\n", output);
        for (String message : history) {
            tellOne("  " + message, output);
        }
        tellOne("  --------история чата--------\n", output);
        tellOne("\n", output);
    }

    public void tellOne(String message, OutputStream output) {
        try {
            output.write(message.getBytes());
            output.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void tellAll(String message) {

        Iterator iterator = outputStreams.iterator();

        while(iterator.hasNext()) {
            try {
                ClientThread t = (ClientThread) iterator.next();
                OutputStream out = t.getOutput();
                out.write(message.getBytes());
                out.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        history.add(message);
        try {
            fileOutput.write(message.getBytes());
            fileOutput.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new Server().start();
    }

    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        outputStreams = new ArrayList<>();
        history = new ArrayList<>();
        users = new ArrayList<>();

        fileOutput = new FileOutputStream("history.txt", true);
        FileInputStream fileInput = new FileInputStream("history.txt");
        BufferedReader brF = new BufferedReader(new InputStreamReader(fileInput));
        String strLine;

        while ((strLine = brF.readLine()) != null) {
            history.add(strLine + "\n");
        }

        help = new ArrayList<>();
        help.add("/help - для вывода всех доступных команд \n");
        help.add("/users - для просмотра списка пользователей в чате \n");
        help.add("/quit - для выхода из чата \n");

        while (true) {
            Socket clientSocket = serverSocket.accept();

            ClientThread t = new ClientThread(clientSocket);
            t.start();          // Стартуем поток для обработки клиента

            outputStreams.add(t);
        }
    }

}
