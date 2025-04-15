package models;

/**
 * La classe Persona rappresenta un contatto all'interno della rubrica telefonica.
 * È legata a un particolare utente (rappresentato da un ID_Utente) e contiene 
 * le informazioni principali di una Persona:
 * <ul>
 *   <li>ID (int) — identificativo univoco di questa persona</li>
 *   <li>ID_Utente (int) — identificativo univoco dell'utente proprietario di questa persona</li>
 *   <li>Nome (String) — campo obbligatorio, lunghezza max. 256</li>
 *   <li>Cognome (String) — facoltativo, lunghezza max. 256</li>
 *   <li>Indirizzo (String) — facoltativo, lunghezza max. 256</li>
 *   <li>Telefono (String) — campo obbligatorio, lunghezza max. 256</li>
 *   <li>Eta (int) — facoltativo, dev'essere >= 0</li>
 * </ul>
 *
 * Questa classe fornisce i metodi setter/getter con controllo di validità, 
 * e un metodo toString() per ottenere una descrizione testuale dell'oggetto.
 */
public class Persona {

    // ----------------------------
    //  ATTRIBUTI OBBLIGATORI
    // ----------------------------
    private int ID;           // Identificativo univoco della persona
    private int ID_Utente;    // Identificativo univoco dell'utente proprietario
    private String Nome;      // Nome (non può essere null o vuoto)
    private String Telefono;  // Telefono (non può essere null o vuoto)

    // ----------------------------
    //  ATTRIBUTI OPZIONALI
    // ----------------------------
    private String Cognome;   // Può essere vuoto
    private String Indirizzo; // Può essere vuoto
    private int Eta;          // Se negativo, lancia un'eccezione

    /**
     * Costruttore completo della classe Persona.
     * Vengono utilizzati i metodi setter interni per effettuare i controlli 
     * e per garantire la validità dei dati.
     *
     * @param ID         L'ID univoco nel database di questa persona (>= 0).
     * @param ID_Utente  L'ID dell'utente proprietario (>= 0).
     * @param Nome       Il nome della persona (non nullo/vuoto).
     * @param Cognome    Il cognome della persona (può essere vuoto).
     * @param Indirizzo  L'indirizzo (può essere vuoto).
     * @param Telefono   Il numero di telefono (non nullo/vuoto).
     * @param Eta        L'età (>= 0).
     *
     * @throws IllegalArgumentException Se uno dei campi obbligatori è invalido
     *                                  o se ID/ID_Utente/Eta sono negativi.
     */
    public Persona(int ID, int ID_Utente, String Nome, String Cognome,
                   String Indirizzo, String Telefono, int Eta) throws IllegalArgumentException {
        this.setID(ID);
        this.setID_Utente(ID_Utente);
        this.setNome(Nome);
        this.setTelefono(Telefono);

        // Campi opzionali
        this.setCognome(Cognome);
        this.setIndirizzo(Indirizzo);
        this.setEta(Eta);
    }

    // ----------------------------
    //         GETTER/SETTER
    // ----------------------------

    public int getID() {
        return ID;
    }

    /**
     * Imposta l'ID della persona.
     * @param ID valore intero >= 0
     * @throws IllegalArgumentException se ID < 0
     */
    public void setID(int ID) throws IllegalArgumentException {
        if (ID < 0) {
            throw new IllegalArgumentException("L'ID deve essere un numero positivo (o zero per 'non assegnato').");
        }
        this.ID = ID;
    }

    public int getID_Utente() {
        return ID_Utente;
    }

    /**
     * Imposta l'ID dell'utente proprietario.
     * @param ID_Utente valore intero >= 0
     * @throws IllegalArgumentException se ID_Utente < 0
     */
    public void setID_Utente(int ID_Utente) throws IllegalArgumentException {
        if (ID_Utente < 0) {
            throw new IllegalArgumentException("L'ID_Utente deve essere un numero positivo (o zero se non assegnato).");
        }
        this.ID_Utente = ID_Utente;
    }

    public String getNome() {
        return Nome;
    }

    /**
     * Imposta il nome della persona.
     * @param Nome stringa non nulla né vuota
     * @throws IllegalArgumentException se Nome è null o vuoto
     */
    public void setNome(String Nome) throws IllegalArgumentException {
        if (Nome == null || Nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto o nullo.");
        }
        this.Nome = Nome;
    }

    public String getCognome() {
        return Cognome;
    }

    /**
     * Imposta il cognome della persona.
     * Se la stringa è nulla o vuota, memorizza una stringa vuota.
     * @param Cognome stringa o null
     */
    public void setCognome(String Cognome) {
        if (Cognome == null || Cognome.trim().isEmpty()) {
            this.Cognome = "";
        } else {
            this.Cognome = Cognome;
        }
    }

    public String getIndirizzo() {
        return Indirizzo;
    }

    /**
     * Imposta l'indirizzo della persona.
     * Se la stringa è nulla o vuota, memorizza una stringa vuota.
     * @param Indirizzo stringa o null
     */
    public void setIndirizzo(String Indirizzo) {
        if (Indirizzo == null || Indirizzo.trim().isEmpty()) {
            this.Indirizzo = "";
        } else {
            this.Indirizzo = Indirizzo;
        }
    }

    public String getTelefono() {
        return Telefono;
    }

    /**
     * Imposta il telefono della persona.
     * @param Telefono stringa non nulla né vuota
     * @throws IllegalArgumentException se Telefono è null o vuoto
     */
    public void setTelefono(String Telefono) throws IllegalArgumentException {
        if (Telefono == null || Telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("Il telefono non può essere vuoto o nullo.");
        }
        this.Telefono = Telefono;
    }

    public int getEta() {
        return Eta;
    }

    /**
     * Imposta l'età della persona.
     * @param Eta valore intero >= 0
     * @throws IllegalArgumentException se Eta < 0
     */
    public void setEta(int Eta) throws IllegalArgumentException {
        if (Eta < 0) {
            throw new IllegalArgumentException("L'eta' deve essere un numero positivo o zero.");
        }
        this.Eta = Eta;
    }

    /**
     * Rappresentazione testuale della persona, utile per il debug.
     * @return una stringa con i dati salienti della persona
     */
    @Override
    public String toString() {
        return "ID: " + ID + "\n" +
               "ID_Utente: " + ID_Utente + "\n" +
               "Nome: " + Nome + "\n" +
               "Cognome: " + Cognome + "\n" +
               "Indirizzo: " + Indirizzo + "\n" +
               "Telefono: " + Telefono + "\n" +
               "Eta': " + Eta;
    }
}
