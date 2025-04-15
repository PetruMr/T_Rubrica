import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import javax.swing.JOptionPane;
import java.sql.Statement;

import controller.LoginController;

/**
 * L'applicazione stabilisce una connessione a un database MySQL.
 * I parametri di connessione (username, password, ip-server-mysql, porta) vengono letti
 * da un file di proprietà denominato "credenziali_database.properties".
 * Se il file non è presente o non è leggibile, viene creato automaticamente con valori di default.
 */
public class Application {
    public static void main(String[] args) {
        // Caricamento del driver JDBC di MySQL.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Visualizza un messaggio d'errore fatale se il driver non è stato trovato.
            String errorMessage = "Driver JDBC MySQL non trovato! " + e.getMessage();
            JOptionPane.showMessageDialog(null, errorMessage, "Errore fatale", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        // Creazione dell'oggetto Properties che conterrà i parametri di connessione.
        Properties props = new Properties();
        File fileCredenziali = new File("credenziali_database.properties");

        // Verifica l'esistenza e l'accessibilità del file delle credenziali.
        if (!fileCredenziali.exists() || !fileCredenziali.canRead()) {
            // Informazione: Il file non esiste oppure non è leggibile, quindi viene creato con i valori di default.
            System.out.println("File delle credenziali non trovato o non leggibile. Generazione con valori di default...");

            // Impostazione dei valori di default.
            props.setProperty("username", "root");
            props.setProperty("password", "");
            props.setProperty("ip-server-mysql", "localhost");
            props.setProperty("porta", "3306");

            // Creazione e scrittura del file delle credenziali con i valori di default.
            try (FileOutputStream fos = new FileOutputStream(fileCredenziali)) {
                props.store(fos, "File delle credenziali generato automaticamente con valori di default");
            } catch (IOException e) {
                // Se la scrittura fallisce, visualizza un messaggio d'errore fatale.
                String errorMessage = "Errore durante la scrittura del file delle credenziali: " + e.getMessage();
                JOptionPane.showMessageDialog(null, errorMessage, "Errore fatale", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(1);
            }
        }

        // Caricamento dei parametri di connessione dal file delle credenziali.
        try (FileInputStream fis = new FileInputStream(fileCredenziali)) {
            props.load(fis);
        } catch (IOException e) {
            // Se la lettura del file fallisce, visualizza un messaggio d'errore fatale.
            String errorMessage = "Errore durante la lettura del file delle credenziali: " + e.getMessage();
            JOptionPane.showMessageDialog(null, errorMessage, "Errore fatale", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        // Estrazione dei parametri di connessione dal file properties.
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        String ipServer = props.getProperty("ip-server-mysql");
        String porta = props.getProperty("porta");

        // Costruzione dell'URL per la connessione al database "rubricadb".
        String url = "jdbc:mysql://" + ipServer + ":" + porta + "/rubricadb";

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            if (conn != null) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    new LoginController(conn);
                });
            }
            
            // Monitoraggio della connessione al database.
            new Thread(() -> monitorConnection(conn)).start();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connessione fallita.\n\nOutput dell'errore per dettagli:\n" + e.getMessage(),
                    "Errore fatale", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private static void monitorConnection(Connection conn) {
        while (true) {
            try {
                Thread.sleep(5000); // Check every 5 seconds
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeQuery("SELECT 1");
                }
            } catch (SQLException | InterruptedException e) {
                JOptionPane.showMessageDialog(null,
                        "Il server si e' disconnesso.\nRiprova ad accedere alla tua rubrica piu' tardi.",
                        "Errore Fatale: Connessione Persa",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }
}
