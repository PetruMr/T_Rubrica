package view;

import models.Persona;
import javax.swing.*;
import java.awt.*;

/**
 * EditorPersonaDialog è una finestra di dialogo modale che permette la creazione
 * o la modifica di un oggetto Persona. Presenta campi di testo per nome, cognome,
 * indirizzo, telefono e età, e bottoni "Salva" e "Annulla".
 *
 * Se il parametro 'persona' è null, la finestra si comporterà da "inserimento nuova persona".
 * Se 'persona' non è null, i campi verranno precompilati, consentendo di modificare
 * una persona già esistente.
 */
public class EditorPersonaDialog extends JDialog {

    // Campi di testo per i vari attributi di Persona
    private JTextField txtNome;
    private JTextField txtCognome;
    private JTextField txtIndirizzo;
    private JTextField txtTelefono;
    private JTextField txtEta;

    // Bottoni di azione
    private JButton btnSalva;
    private JButton btnAnnulla;

    // Flag che indica se l'utente ha premuto "Salva"
    private boolean confirmed = false;

    // Riferimento all'oggetto Persona esistente (se in modalità modifica).
    // Potremmo usarlo per preservare ID e ID_Utente.
    private Persona existingPersona;

    /**
     * Costruttore del dialog.
     *
     * @param owner   Finestra proprietaria (MainFrame o altra finestra).
     * @param persona L'oggetto Persona da modificare; se null, siamo in inserimento.
     */
    public EditorPersonaDialog(Frame owner, Persona persona) {
        super(owner, true); // Crea un dialog modale
        this.existingPersona = persona;

        // Imposta il titolo in base alla modalità (inserimento o modifica)
        setTitle((persona == null) ? "Nuova Persona" : "Modifica Persona");
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Pannello centrale con griglia 5x2 per i campi
        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Creazione dei campi testuali
        fieldsPanel.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        fieldsPanel.add(txtNome);

        fieldsPanel.add(new JLabel("Cognome:"));
        txtCognome = new JTextField();
        fieldsPanel.add(txtCognome);

        fieldsPanel.add(new JLabel("Indirizzo:"));
        txtIndirizzo = new JTextField();
        fieldsPanel.add(txtIndirizzo);

        fieldsPanel.add(new JLabel("Telefono:"));
        txtTelefono = new JTextField();
        fieldsPanel.add(txtTelefono);

        fieldsPanel.add(new JLabel("Eta':"));
        txtEta = new JTextField();
        fieldsPanel.add(txtEta);

        add(fieldsPanel, BorderLayout.CENTER);

        // Pannello per i pulsanti (Salva e Annulla)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalva = new JButton("Salva");
        btnAnnulla = new JButton("Annulla");
        buttonsPanel.add(btnSalva);
        buttonsPanel.add(btnAnnulla);
        add(buttonsPanel, BorderLayout.SOUTH);

        // Se stiamo in modalità modifica, riempi i campi con i dati esistenti
        if (persona != null) {
            txtNome.setText(persona.getNome());
            txtCognome.setText(persona.getCognome());
            txtIndirizzo.setText(persona.getIndirizzo());
            txtTelefono.setText(persona.getTelefono());
            txtEta.setText(String.valueOf(persona.getEta()));
        }

        // Listener per "Annulla": chiude il dialog e imposta confirmed a false
        btnAnnulla.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        // Listener per "Salva": chiude il dialog e imposta confirmed a true
        btnSalva.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        // Centra la finestra sopra il padre
        setLocationRelativeTo(owner);
    }

    /**
     * Restituisce true se l'utente ha premuto "Salva".
     * @return true se confermato, false se annullato.
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Crea e restituisce un nuovo oggetto Persona utilizzando i dati
     * presenti nei campi di testo del form. Se i campi non sono validi,
     * restituisce null e mostra un messaggio di errore appropriato.
     *
     * @return L'oggetto Persona costruito, oppure null in caso di input non valido.
     */
    public Persona getPersona() {
        try {
            String nome = txtNome.getText().trim();
            String cognome = txtCognome.getText().trim();
            String indirizzo = txtIndirizzo.getText().trim();
            String telefono = txtTelefono.getText().trim();
            int eta = Integer.parseInt(txtEta.getText().trim());

            // Se la Persona esisteva già, ne recuperiamo l'ID e l'ID_Utente
            // per non perdere queste informazioni.
            int personaID = 0;
            int personaID_Utente = 0;
            if (existingPersona != null) {
                personaID = existingPersona.getID();
                personaID_Utente = existingPersona.getID_Utente();
            }

            // Costruiamo il nuovo oggetto Persona
            // ID e ID_Utente saranno 0 per i nuovi inserimenti,
            // oppure i valori di existingPersona se stiamo modificando una Persona esistente.
            return new Persona(
                personaID,
                personaID_Utente,
                nome,
                cognome,
                indirizzo,
                telefono,
                eta
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Il campo 'Eta'' deve contenere un numero intero valido.",
                "Errore di input",
                JOptionPane.ERROR_MESSAGE
            );
            return null;
        } catch (IllegalArgumentException ex) {
            // Gestisce eventuali altri errori di validazione (nome vuoto, telefono vuoto, ecc.)
            JOptionPane.showMessageDialog(
                this,
                "Errore nei dati inseriti: " + ex.getMessage(),
                "Errore di input",
                JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
    }
}
