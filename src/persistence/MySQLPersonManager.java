package persistence;

import models.Persona;
import models.Utente;

import java.sql.*;
import java.util.Vector;

/**
 * La classe MySQLPersonManager si occupa di interagire con il database MySQL
 * per gestire (creare, leggere, aggiornare, eliminare) oggetti Persona.
 * 
 * Questa implementazione presuppone che esista una tabella "Persona" 
 * con le seguenti colonne:
 * 
 * CREATE TABLE Persona (
 *     ID         INT AUTO_INCREMENT PRIMARY KEY,
 *     Utente_ID  INT NOT NULL,
 *     Nome       VARCHAR(256) NOT NULL,
 *     Cognome    VARCHAR(256),
 *     Indirizzo  VARCHAR(256),
 *     Telefono   VARCHAR(256) NOT NULL,
 *     Eta        INT DEFAULT 0
 * );
 * 
 * Si assume inoltre che l'ID_Utente corrisponda all'ID di un utente 
 * (rappresentato da un oggetto Utente nel nostro software).
 * 
 * NOTA: L'ID e l'ID_Utente sono di tipo INT in MySQL. L'ID è auto-increment, 
 *       mentre l'ID_Utente deve essere impostato dal codice.
 */
public class MySQLPersonManager {

    private Connection conn;  // Connessione verso MySQL
    private Utente user;      // Utente proprietario delle persone che andiamo a gestire

    /**
     * Costruttore di MySQLPersonManager.
     * 
     * @param user L'Utente "proprietario" di queste persone (chi è loggato).
     * @param conn La connessione JDBC verso il database MySQL.
     */
    public MySQLPersonManager(Utente user, Connection conn) {
        this.conn = conn;
        this.user = user;
    }

    /**
     * Crea una nuova Persona nel database, associandola all'utente corrente (user).
     * Poiché la tabella Persona ha la colonna ID auto-increment, non è necessario 
     * specificare manualmente l'ID. Il database lo genererà automaticamente.
     *
     * @param nome      Nome della persona (non nullo).
     * @param cognome   Cognome della persona (può essere vuoto).
     * @param eta       Età della persona (>= 0).
     * @param indirizzo Indirizzo della persona (può essere vuoto).
     * @param numero    Telefono della persona (non nullo).
     * 
     * @throws SQLException se ci sono problemi nella comunicazione col DB.
     */
    public void salvaPersona(String nome, String cognome, int eta, String indirizzo, String numero) throws SQLException {
        String sql = "INSERT INTO Persona (utente_id, Nome, Cognome, Indirizzo, Telefono, Eta) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getID_Utente());    // ID dell'utente proprietario
            ps.setString(2, nome);
            ps.setString(3, cognome);
            ps.setString(4, indirizzo);
            ps.setString(5, numero);
            ps.setInt(6, eta);
            ps.executeUpdate();
        }
    }

    /**
     * Legge tutte le persone dal database che appartengono all'utente corrente (user).
     * Restituisce un Vector di oggetti Persona.
     *
     * @return Un Vector di Persona (può essere vuoto se non ci sono persone).
     * @throws SQLException se si verifica un problema di comunicazione col DB.
     */
    public Vector<Persona> leggiPersone() throws SQLException {
        Vector<Persona> result = new Vector<>();
        String sql = "SELECT ID, utente_id, Nome, Cognome, Indirizzo, Telefono, Eta " +
                     "FROM Persona WHERE utente_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getID_Utente());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Ricostruisce l'oggetto Persona dai campi del database
                    int ID = rs.getInt("ID");
                    int ID_Utente = rs.getInt("utente_id");
                    String Nome = rs.getString("Nome");
                    String Cognome = rs.getString("Cognome");
                    String Indirizzo = rs.getString("Indirizzo");
                    String Telefono = rs.getString("Telefono");
                    int Eta = rs.getInt("Eta");

                    Persona p = new Persona(ID, ID_Utente, Nome, Cognome, Indirizzo, Telefono, Eta);
                    result.add(p);
                }
            }
        }
        return result;
    }

    /**
     * Elimina una persona (identificata da 'idPersona') dal database, 
     * assicurandosi che appartenga all'utente corrente.
     *
     * @param idPersona ID della persona da eliminare.
     * @throws SQLException se si verifica un problema di comunicazione col DB
     *                      o se la query fallisce.
     */
    public void eliminaPersona(int idPersona) throws SQLException {
        String sql = "DELETE FROM Persona WHERE ID = ? AND utente_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            ps.setInt(2, user.getID_Utente());
            ps.executeUpdate();
        }
    }

    /**
     * Aggiorna i campi di una persona esistente nel database.
     * L'ID_Utente deve coincidere con l'utente corrente, per evitare 
     * aggiornamenti non autorizzati.
     *
     * @param idPersona ID della persona da modificare.
     * @param nome      Nuovo nome (non nullo).
     * @param cognome   Nuovo cognome (può essere vuoto).
     * @param eta       Nuova età (>= 0).
     * @param indirizzo Nuovo indirizzo (può essere vuoto).
     * @param numero    Nuovo telefono (non nullo).
     * 
     * @throws SQLException se qualcosa va storto nella query o nella connessione.
     */
    public void modificaPersona(int idPersona, String nome, String cognome, int eta,
                                String indirizzo, String numero) throws SQLException {
        String sql = "UPDATE Persona " +
                     "SET Nome = ?, Cognome = ?, Indirizzo = ?, Telefono = ?, Eta = ? " +
                     "WHERE ID = ? AND utente_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setString(3, indirizzo);
            ps.setString(4, numero);
            ps.setInt(5, eta);
            ps.setInt(6, idPersona);
            ps.setInt(7, user.getID_Utente());
            ps.executeUpdate();
        }
    }
}
