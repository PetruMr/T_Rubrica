# Turing - Rubrica

Questo progetto è la realizzazione pratica di un compito assegnatomi dall'azienda [Turing](www.webturing.net). Si tratta di un progetto che fa parte del *secondo step* della selezione per il ruolo di *Developer*.

Il progetto consiste nella realizzazione di una rubrica telefonica, in cui è possibile aggiungere, modificare e cancellare contatti. La rubrica deve essere realizzata in Java e deve utilizzare un database MySQL per la memorizzazione dei dati. Da notare che esistono diverse versioni, però questo progetto è stato realizzato prendendo in cosiderazione le richieste più *complete* del progetto (avere un DB, utenze, caricare il progetto su GitHub, ecc.).

## Requisiti

Il documento che contiene i requisiti completi del progetto rimangono privati, seguendo le indicazioni dell'azienda, ma il codice rimarrà documentato per motivare quanto viene fatto.

## Esecuzione

### Versioni utilizzate nello sviluppo

|  | Versione |
| -------- | ----------- |
| Java | 17 |
| MySQL | 8.0 |

### Step per la compilazione

Per compilare il progetto è stato creato il file `build.bat` il quale fa uso di *Java* per compilare il progetto nel file eseguibile `Rubrica.jar` ed inserirlo nella cartella `out`.

### Step per provare il progetto

Una volta scaricato il contenuto di questa repository, il contenuto utile per l'esecuzione del progetto, e per fare eventuali prove, si trova nella cartella `out`.

1. Modificare il file `credenziali_database.properties` utilizzando le impostazioni del proprio DBMS. Le credenziali di default sono:
   ```properties
   username=root	
   password=
   ip-server-mysql=localhost
   porta=3306
   ```
2. Lanciare sul proprio sistema MySQL lo script `schema_database.sql` per costruire il database.
3. Eseguire `Rubrica.jar`
