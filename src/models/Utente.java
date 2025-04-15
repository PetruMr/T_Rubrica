package models;

/**
 * La classe utente si occupa di gestire le utenze all'interno del nostro progrmama rubrica.
 * 
 * Ogni utente ha:
 * - username
 * - password
 */
public class Utente {
    // Attributi della classe Utente
    private String username;
    private String password;
    private int ID_Utente; // ID univoco dell'utente nel database


    // Costruttore della classe Utente
    public Utente(String username, String password, int ID_Utente) {
        this.username = username;
        this.password = password;
        this.ID_Utente = ID_Utente;
    }


    // Getter e Setter per gli attributi della classe Utente
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getID_Utente() {
        return ID_Utente;
    }

    public void setID_Utente(int ID_Utente) {
        this.ID_Utente = ID_Utente;
    }
}
