# ProgettoPub
Struttura Progetto:
Classi:
Server: gestione caricamento File CSV, avvio server sulla porta impostata e comunicazione con ogni singolo client.
ClienHandler: gestione client divisa (ogni client connesso viene gestito con un thread di client hendler).

RelazioneProgettoPub.docx: documentazione progetto

Avvio server:
Avviare il main nella classe Server, se nel terminale viene scritto "errore nel caricamento del file CSV" 
potrebbe essere necessario modificare la String fileCSV nella classe server.
COMANDI:
Per ottenere una riga usare il comando GETROW numeroRiga
Per ottenere una colonna usare il comando GETCOLUMN numeroColonna
Per ricercare usare il comando FIND parolaChiave
Per sapere i comandi usare il comando HELP