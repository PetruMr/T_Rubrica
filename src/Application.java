import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Questa applicazione stabilisce una connessione a un database MySQL
 * utilizzando i parametri di connessione letti da un file di proprietà
 * chiamato "credenziali_database.properties". I parametri includono:
 * - username
 * - password
 * - ip-server-mysql
 * - porta
 * L'URL di connessione viene costruito in base agli ultimi due parametri,
 * assumendo che il nome del database sia "rubricadb".
 */

public class Application {
    public static void main(String[] args) {
        // Caricamento esplicito del driver JDBC di MySQL (opzionale)
        // Le versioni moderne registrano automaticamente il driver se il jar è nel classpath.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC MySQL non trovato!");
            e.printStackTrace();
            return;
        }

        // Caricamento dei parametri di connessione dal file "credenziali_database.properties"
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("credenziali_database.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file delle credenziali.");
            e.printStackTrace();
            return;
        }

        // Lettura dei parametri dal file properties
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        String ipServer = props.getProperty("ip-server-mysql");
        String porta = props.getProperty("porta");

        // Costruzione dell'URL di connessione (assumendo "rubricadb" come nome del database)
        String url = "jdbc:mysql://" + ipServer + ":" + porta + "/rubricadb";

        // Tentativo di connessione al database
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            if (conn != null) {
                System.out.println("Connessione stabilita con successo!");
            }
        } catch (SQLException e) {
            System.err.println("Connessione fallita. Controllare l'output per dettagli.");
            e.printStackTrace();
        }
    }
}
