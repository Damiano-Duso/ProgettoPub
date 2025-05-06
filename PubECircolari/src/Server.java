import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int porta = 8069; 
    private static final String fileCSV = "Mappa-dei-pub-circoli-locali-in-Italia.csv"; 
    public static List<String[]> righe = new ArrayList<>(); 
    public static String[] categorie;

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


    public static void loadCsv() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileCSV))) {
            String line;
            boolean isFirst = true;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(";", -1);
                if (isFirst) {
                    categorie = fields;
                    isFirst = false;
                } else {
                    righe.add(fields);
                }
            }
            System.out.println("Caricate " + righe.size() + " righe del file CSV.");
        } catch (IOException e) {
            System.err.println("Errore nel caricamento del file CSV: " + e.getMessage());
        }
    }
}