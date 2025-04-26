import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.println("Client connesso: " + socket);
        
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
            
        ) {
            out.write("Server connesso, digitare i comandi o HELP \r\n");
            out.flush();
            String input;
            while ((input = in.readLine()) != null) {
                input = input.trim();
                if (input.equalsIgnoreCase("EXIT")) {
                    out.write("Disconnessione avvenuta. \r\n");
                    out.flush();
                    break;
                } else if (input.startsWith("GETROW")) {
                    handleGetRow(input, out);
                } else if (input.startsWith("GETCOLUMN")) {
                    handleGetColumn(input, out);
                } else if (input.startsWith("FIND")) {
                    handleFind(input, out);
                }
                else if (input.startsWith("HELP")) {
                    out.write("Comandi: \r\n");
                    out.write("GETROW numeroRiga <<<<<<per ottenere la riga specificata \r\n");
                    out.write("GETCOLUMN numeroColonna <<<<<<per ottenere la colonna specificata \r\n");
                    out.write("FIND parola <<<<<per ottenere tutte le righe contenetiu quella parola \r\n");
                    out.flush();
                } else {
                    out.write("Comando non riconosciuto. \r\n");
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.err.println("Errore client: " + e.getMessage());
        }
    }

    private void handleGetRow(String input, BufferedWriter out) throws IOException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]);
            if (index >= 0 && index < Server.righe.size()) {
                String[] row = Server.righe.get(index);
                out.write("Riga " + index + ":\n");
                for (int i = 0; i < row.length; i++) {
                    out.write(Server.categorie[i] + ": " + row[i] + " \r\n");
                }
            } else {
                out.write("Indice riga non valido. \r\n");
            }
        } catch (Exception e) {
            out.write("Errore nel comando GET_ROW. \r\n");
        }
        out.flush();
    }

    private void handleGetColumn(String input, BufferedWriter out) throws IOException {
        try {
            String colName = input.substring("GET_COLUMN ".length()).trim();
            int colIndex = -1;
            for (int i = 0; i < Server.categorie.length; i++) {
                if (Server.categorie[i].equalsIgnoreCase(colName)) {
                    colIndex = i;
                    break;
                }
            }
            if (colIndex == -1) {
                out.write("Colonna non trovata. \r\n");
            } else {
                out.write("Colonna " + colName + ": \r\n");
                for (String[] row : Server.righe) {
                    out.write(row[colIndex] + " \r\n");
                }
            }
        } catch (Exception e) {
            out.write("Errore nel comando GET_COLUMN. \r\n");
        }
        out.flush();
    }

    private void handleFind(String input, BufferedWriter out) throws IOException {
        try {
            String keyword = input.substring("FIND ".length()).trim().toLowerCase();
            int trovati = 0;
            for (String[] row : Server.righe) {
                for (int i = 0; i < row.length; i++) {
                    if (row[i].toLowerCase().contains(keyword)) {
                        trovati++;
                        for (int j = 0; j < row.length; j++) {
                            out.write(Server.categorie[j] + ": " + row[j] + " \r\n");
                        }
                        out.write("--- \r\n");
                        break;
                    }
                }
            }
            if (trovati == 0) {
                out.write("Nessun risultato trovato. \r\n");
            }
        } catch (Exception e) {
            out.write("Errore nel comando FIND. \r\n");
        }
        out.flush();
    }
}