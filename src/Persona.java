/**
 * Classe che rappresenta una persona, ovvero un oggetto che fa parte della rubrica.
 * Essa è composta dai seguenti dati (che vengono scritti e letti da un databse MySQL, pertanto hanno un tipo specifico):
 * - ID         (int) - identificativo univoco della persona
 * - ID_Utente  (int) - identificativo univoco dell'utente a cui appartiene la persona
 * - Nome       (Varchar(1000))
 * - Cognome    (Varchar(1000))
 * - Indirizzo  (Varchar(1000))
 * - Telefono   (Varchar(1000))
 * - Eta        (int)
 * 
 * Con questa classe si devono poter eseguire le seguenti operazioni:
 * - Creazione di un oggetto Persona
 * - Modifica dei dati di una persona
 * - Eliminazione di una persona
 * - Visualizzazione dei dati di una persona
 */
public class Persona {   
    
    // ATTRIBUTI DELLA CLASSE PERSONA

    // Attributi che devono necessariamente avere un valore
    private int ID;
    private int ID_Utente;
    private String Nome;
    private String Telefono;

    // Questi dati non sono necessari, posso essere anche nulli
    private String Cognome = "";
    private String Indirizzo = "";
    private int Eta = 0;


    // COSTRUTTORI PER LA CLASSE PERSONA

    /**
     * Costruttore della classe Persona
     * @param ID         ID della persona
     * @param ID_Utente  ID dell'utente a cui appartiene la persona
     * @param Nome       Nome della persona
     * @param Cognome    Cognome della persona
     * @param Indirizzo  Indirizzo della persona
     * @param Telefono   Telefono della persona
     * @param Eta        Età della persona
     */
    public Persona(int ID, int ID_Utente, String Nome, String Cognome, String Indirizzo, String Telefono, int Eta) throws IllegalArgumentException {
        // Vengono utilizzati i setter in modo da implementare un'unica volta i controlli sulla validità dei dati
        this.setID(ID);
        this.setID_Utente(ID_Utente);
        this.setNome(Nome);
        this.setTelefono(Telefono);

        // Questi dati non sono necessari, posso essere anche nulli
        this.setCognome(Cognome);
        this.setIndirizzo(Indirizzo);
        this.setEta(Eta);    
    }


    // SETTER E GETTER PER PERSONA

    public int getID() {
        return ID;
    }
    public void setID(int ID) throws IllegalArgumentException {
        // Controllo che l'ID sia un numero positivo
        if (ID < 0) {
            throw new IllegalArgumentException("L'ID deve essere un numero positivo.");
        }
        this.ID = ID;
    }

    public int getID_Utente() {
        return ID_Utente;
    }
    public void setID_Utente(int ID_Utente) throws IllegalArgumentException {
        // Controllo che l'ID_Utente sia un numero positivo
        if (ID_Utente < 0) {
            throw new IllegalArgumentException("L'ID_Utente deve essere un numero positivo.");
        }
        this.ID_Utente = ID_Utente;
    }

    public String getNome() {
        return Nome;
    }
    public void setNome(String Nome) throws IllegalArgumentException {
        // Controllo che il nome non sia vuoto
        if (Nome == null || Nome.isEmpty()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto.");
        }
        this.Nome = Nome;
    }

    public String getCognome() {
        return Cognome;
    }
    public void setCognome(String Cognome) {
        // Controllo che il cognome non sia vuoto
        if (Cognome == null || Cognome.isEmpty()) {
            this.Cognome = "";
        } else {
            this.Cognome = Cognome;
        }
    }

    public String getIndirizzo() {
        return Indirizzo;
    }
    public void setIndirizzo(String Indirizzo) {
        // Controllo che l'indirizzo non sia vuoto
        if (Indirizzo == null || Indirizzo.isEmpty()) {
            this.Indirizzo = "";
        } else {
            this.Indirizzo = Indirizzo;
        }
    }

    public String getTelefono() {
        return Telefono;
    }
    public void setTelefono(String Telefono) throws IllegalArgumentException {
        // Controllo che il telefono non sia vuoto
        if (Telefono == null || Telefono.isEmpty()) {
            throw new IllegalArgumentException("Il telefono non può essere vuoto.");
        }
        this.Telefono = Telefono;
    }

    public int getEta() {
        return Eta;
    }
    public void setEta(int Eta) throws IllegalArgumentException {
        // Controllo che l'età sia un numero positivo
        if (Eta < 0) {
            throw new IllegalArgumentException("L'età deve essere un numero positivo.");
        }
        this.Eta = Eta;
    }

    // METODI PER LA CLASSE PERSONA
    /**
     * Metodo che restituisce una stringa con i dati della persona
     * @return Stringa con i dati della persona
     */
    public String toString() {
        return "ID: " + ID + "\n" +
               "ID_Utente: " + ID_Utente + "\n" +
               "Nome: " + Nome + "\n" +
               "Cognome: " + Cognome + "\n" +
               "Indirizzo: " + Indirizzo + "\n" +
               "Telefono: " + Telefono + "\n" +
               "Età: " + Eta;
    }
}
