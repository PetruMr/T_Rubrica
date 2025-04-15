package controller;

import models.Utente;
import view.LoginFrame;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;

/**
 * La classe LoginController gestisce l'intera logica di login e registrazione,
 * compresa la gestione del frame LoginFrame. Coordina le azioni dell'utente
 * e delega alla vista la sola parte di visualizzazione.
 */
public class LoginController {
    
    // Costanti per il risultato del login
    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_INVALID_CREDENTIALS = 1;
    public static final int LOGIN_SERVER_ERROR = 2;
    
    // Costanti per il risultato della registrazione
    public static final int REGISTER_SUCCESS = 0;
    public static final int REGISTER_USER_EXISTS = 1;
    public static final int REGISTER_SERVER_ERROR = 2;
    
    // Riferimento alla connessione al database
    private Connection conn;
    
    // Riferimento alla vista che mostra la GUI
    private LoginFrame loginFrame;
    
    /**
     * Costruttore del controller che riceve la connessione al database.
     * Crea la finestra di login e imposta gli ActionListener dei pulsanti.
     *
     * @param conn La connessione al database da utilizzare per le operazioni.
     */
    public LoginController(Connection conn) {
        this.conn = conn;
        
        // Creazione della vista (LoginFrame) e impostazione del Controller come "gestore"
        this.loginFrame = new LoginFrame();
        this.loginFrame.setController(this);  // Passa il riferimento di questo Controller
        
        // Visualizza la finestra al termine della configurazione
        this.loginFrame.setVisible(true);
    }
    
    /**
     * Effettua il login confrontando le credenziali inserite con quelle memorizzate.
     * La logica di recupero dei campi è delegata alla LoginFrame tramite metodi get.
     */
    public void handleLogin() {
        // Recupero di username e password dalla vista
        String username = loginFrame.getUsernameLogin();
        String password = loginFrame.getPasswordLogin();
        
        // Controlli preliminari sui campi (ad esempio lunghezza massima)
        if (username.length() > 255 || password.length() > 255) {
            loginFrame.mostraMessaggioErrore("Username e password devono essere inferiori a 255 caratteri.");
            return;
        }
        
        // Chiamata del metodo di effettivo controllo sul DB
        LoginResult result = login(username, password);
        
        // Gestione dell'esito ottenuto
        switch (result.status) {
            case LOGIN_SUCCESS:
                // Se login avvenuto con successo, si apre la MainFrame (PersonaController)
                openMainFrame(result.user);
                loginFrame.dispose();  // Chiude la finestra di login
                break;
                
            case LOGIN_INVALID_CREDENTIALS:
                loginFrame.mostraMessaggioErrore("Login fallito: credenziali non valide.");
                break;
                
            case LOGIN_SERVER_ERROR:
                loginFrame.mostraMessaggioErrore("Login fallito: errore del server.");
                break;
        }
    }
    
    /**
     * Effettua la registrazione di un nuovo utente, recuperando i dati dalla vista.
     */
    public void handleRegister() {
        // Recupero di username e password dalla vista
        String username = loginFrame.getUsernameRegister();
        String password = loginFrame.getPasswordRegister();
        
        // Controlli preliminari sui campi (ad esempio lunghezza massima)
        if (username.length() > 255 || password.length() > 255) {
            loginFrame.mostraMessaggioErrore("Username e password devono essere inferiori a 255 caratteri.");
            return;
        }
        
        // Chiamata del metodo di registrazione sul DB
        int regStatus = register(username, password);
        
        // Gestione dell'esito ottenuto
        switch (regStatus) {
            case REGISTER_SUCCESS:
                loginFrame.mostraMessaggioInfo("Registrazione avvenuta con successo, esegui il login.");
                break;
                
            case REGISTER_USER_EXISTS:
                loginFrame.mostraMessaggioErrore("Registrazione fallita: utente gia' esistente.");
                break;
                
            case REGISTER_SERVER_ERROR:
                loginFrame.mostraMessaggioErrore("Registrazione fallita: errore del server.");
                break;
        }
    }
    
    /**
     * Classe interna che rappresenta il risultato di un'operazione di login.
     * Contiene lo stato del login ed, in caso di successo, l'oggetto Utente.
     */
    public static class LoginResult {
        public int status;
        public Utente user;
        
        public LoginResult(int status, Utente user) {
            this.status = status;
            this.user = user;
        }
    }
    
    /**
     * Metodo per effettuare il login sul database, comparando i valori con quelli memorizzati.
     *
     * @param username Il nome utente inserito.
     * @param password La password inserita.
     * @return Un oggetto LoginResult contenente lo stato del login ed eventualmente l'Utente autenticato.
     */
    public LoginResult login(String username, String password) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT password, salt, id FROM Utente WHERE username = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Recupera l'hash e il salt memorizzati
                    String storedHash = rs.getString("password");
                    String salt = rs.getString("salt");
                    String computedHash = computeHash(password, salt);
                    int id_utente = rs.getInt("id");
                    
                    if (computedHash.equals(storedHash)) {
                        // Login eseguito con successo: crea l'oggetto Utente
                        Utente user = new Utente(username, storedHash, id_utente);
                        return new LoginResult(LOGIN_SUCCESS, user);
                    } else {
                        // Credenziali non valide (password errata)
                        return new LoginResult(LOGIN_INVALID_CREDENTIALS, null);
                    }
                } else {
                    // Utente non trovato
                    return new LoginResult(LOGIN_INVALID_CREDENTIALS, null);
                }
            }
        } catch (SQLException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return new LoginResult(LOGIN_SERVER_ERROR, null);
        }
    }
    
    /**
     * Metodo per registrare un nuovo utente.
     * Verifica che l'utente non esista già e, in caso contrario, inserisce l'utente nel database.
     *
     * @param username Il nome utente da registrare.
     * @param password La password da registrare.
     * @return Un intero che rappresenta il risultato della registrazione.
     */
    public int register(String username, String password) {
        try {
            // Verifica se l'utente esiste già nel database
            try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM Utente WHERE username = ?")) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return REGISTER_USER_EXISTS;  // Utente già esistente
                    }
                }
            }
            
            // Genera un salt casuale e calcola l'hash della password
            String salt = generateSalt();
            String hashedPassword = computeHash(password, salt);
            
            // Inserisce il nuovo utente nel database
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Utente (username, password, salt) VALUES (?, ?, ?)")) {
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword);
                stmt.setString(3, salt);
                int rows = stmt.executeUpdate();
                return (rows > 0) ? REGISTER_SUCCESS : REGISTER_SERVER_ERROR;
            }
        } catch (SQLException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return REGISTER_SERVER_ERROR;
        }
    }
    
    /**
     * Apre il frame principale della rubrica, passando l'utente autenticato e la connessione
     * a un nuovo controller che si occuperà della gestione.
     *
     * @param user L'oggetto Utente autenticato.
     */
    private void openMainFrame(Utente user) {
        PersonaController personaController = new PersonaController(this.conn, user);
        personaController.initController();
    }
    
    /**
     * Metodo helper per calcolare l'hash di una password concatenata col salt usando SHA-256.
     *
     * @param password La password in chiaro.
     * @param salt Il salt da concatenare.
     * @return Una stringa esadecimale che rappresenta l'hash della combinazione password+salt.
     * @throws NoSuchAlgorithmException Se l'algoritmo SHA-256 non è disponibile.
     */
    private String computeHash(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text = password + salt;
        byte[] hashBytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    /**
     * Metodo helper per generare un salt casuale.
     * Utilizza 4 byte casuali per ottenere una stringa esadecimale di 8 caratteri.
     *
     * @return Il salt generato.
     */
    private String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] saltBytes = new byte[4]; // 4 byte = 8 caratteri esadecimali
        sr.nextBytes(saltBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : saltBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
