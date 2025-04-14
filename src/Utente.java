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


    // Costruttore della classe Utente
    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
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
}
