import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class CsvServer {
    private static final int porta = 8000;
    private static final String file = ".\\Mappa-dei-pub-circolari-locali-in-italia.csv";
    private static List<String[]> data = new ArrayList<>();
    private static String[] headers;

    public static void main(String[] args) {
        loadCsv();

        ExecutorService pool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Server avviato sulla porta " + porta);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadCsv() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirst = true;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(";", -1);
                if (isFirst) {
                    headers = fields;
                    isFirst = false;
                } else {
                    data.add(fields);
                }
            }
            System.out.println("Caricate " + data.size() + " righe dal CSV.");
        } catch (IOException e) {
            System.err.println("Errore nel caricamento del CSV: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            System.out.println("Nuovo client connesso: " + socket);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                String request;
                while ((request = in.readLine()) != null) {
                    request = request.trim();
                    if (request.equalsIgnoreCase("EXIT")) {
                        out.write("Bye!\n");
                        out.flush();
                        break;
                    } else if (request.startsWith("GET_ROW")) {
                        handleGetRow(request, out);
                    } else if (request.startsWith("FIND")) {
                        handleFind(request, out);
                    } else {
                        out.write("ERROR: Comando non valido\n");
                        out.flush();
                    }
                }
            } catch (IOException e) {
                System.err.println("Errore nella comunicazione con il client: " + e.getMessage());
            }
        }
        public void handleGetRow(String request, BufferedWriter out){
            //da fare------
        }
        public void handleFind(String request, BufferedWriter out){
            //da fare-------
        }
    }
}
