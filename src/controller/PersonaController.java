package controller;

import models.Persona;
import models.Utente;
import persistence.MySQLPersonManager;
import view.EditorPersonaDialog;
import view.MainFrame;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

/**
 * La classe PersonaController coordina la logica applicativa per la gestione 
 * delle persone (inserimento, modifica, eliminazione) e la relativa interfaccia 
 * grafica (MainFrame ed EditorPersonaDialog).
 * Si appoggia a MySQLPersonManager per le operazioni sul database MySQL.
 */
public class PersonaController {

    private MainFrame mainFrame;               // Finestra principale
    private MySQLPersonManager personManager;  // Manager per la persistenza
    private Connection conn;                   // Connessione verso il DB MySQL
    private Utente currentUser;                // Utente attualmente loggato

    /**
     * Costruttore del controller: inizializza la finestra principale (MainFrame),
     * il manager di persistenza (MySQLPersonManager) e registra i listener sui pulsanti.
     *
     * @param conn        Connessione JDBC verso MySQL
     * @param currentUser L'utente loggato, proprietario delle persone da gestire
     */
    public PersonaController(Connection conn, Utente currentUser) {
        this.conn = conn;
        this.currentUser = currentUser;
        // Inizializza il MySQLPersonManager con l'utente e la connessione corrente
        this.personManager = new MySQLPersonManager(currentUser, conn);
        // Crea la finestra principale
        this.mainFrame = new MainFrame();
    }

    /**
     * Inizializza i listener dei pulsanti "Nuovo", "Modifica", "Elimina"
     * e carica la tabella con i dati attuali dal database.
     */
    public void initController() {
        // Carica dati iniziali dal DB
        refreshTable();

        // Listener per "Nuovo": apre un EditorPersonaDialog con tutti i campi vuoti.
        mainFrame.getBtnNuovo().addActionListener(e -> {
            // Persona null => creazione di una nuova persona
            EditorPersonaDialog dialog = new EditorPersonaDialog(mainFrame, null);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                // Se l'utente ha premuto "Salva"
                Persona p = dialog.getPersona();
                if (p != null) {
                    try {
                        // Crea una nuova riga nel DB con i dati della persona
                        personManager.salvaPersona(
                            p.getNome(),
                            p.getCognome(),
                            p.getEta(),
                            p.getIndirizzo(),
                            p.getTelefono()
                        );
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(
                            mainFrame, 
                            "Errore durante l'inserimento nel database:\n" + ex.getMessage(),
                            "Errore DB",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                    refreshTable();
                }
            }
        });

        // Listener per "Modifica": necessita di una persona selezionata nella tabella
        mainFrame.getBtnModifica().addActionListener(e -> {
            Persona selected = mainFrame.getSelectedPersona();
            if (selected == null) {
                JOptionPane.showMessageDialog(
                    mainFrame, 
                    "Selezionare una persona da modificare.",
                    "Nessuna selezione",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // EditorPersonaDialog aperto in modalità "modifica"
            EditorPersonaDialog dialog = new EditorPersonaDialog(mainFrame, selected);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                // Nuovi dati inseriti dall'utente
                Persona updatedData = dialog.getPersona();
                if (updatedData != null) {
                    try {
                        // Esegue l'UPDATE sul database
                        personManager.modificaPersona(
                            selected.getID(),           // ID della persona da modificare
                            updatedData.getNome(),
                            updatedData.getCognome(),
                            updatedData.getEta(),
                            updatedData.getIndirizzo(),
                            updatedData.getTelefono()
                        );
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(
                            mainFrame,
                            "Errore durante la modifica nel database:\n" + ex.getMessage(),
                            "Errore DB",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                    refreshTable();
                }
            }
        });

        // Listener per "Elimina": necessita di una persona selezionata
        mainFrame.getBtnElimina().addActionListener(e -> {
            Persona selected = mainFrame.getSelectedPersona();
            if (selected == null) {
                JOptionPane.showMessageDialog(
                    mainFrame,
                    "Selezionare una persona da eliminare.",
                    "Nessuna selezione",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String[] options = new String[2];
            options[0] = "SI";
            options[1] = "NO";

            // Richiesta di conferma
            int confirm = JOptionPane.showConfirmDialog(
                mainFrame,
                "Eliminare la persona " + selected.getNome() + " " + selected.getCognome() + "?",
                "Conferma eliminazione",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Esegue la DELETE sul database
                    personManager.eliminaPersona(selected.getID());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                        mainFrame,
                        "Errore durante l'eliminazione dal database:\n" + ex.getMessage(),
                        "Errore DB",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
                refreshTable();
            }
        });

        // Mostra la finestra principale
        mainFrame.setVisible(true);
    }

    /**
     * Recupera la lista delle persone dal database e aggiorna la tabella nella mainFrame.
     * In caso di errore di connessione o query, mostra un dialogo di errore.
     */
    private void refreshTable() {
        try {
            Vector<Persona> allPersons = personManager.leggiPersone();
            mainFrame.updateTableData(allPersons);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                mainFrame,
                "Errore durante il caricamento delle persone dal database:\n" + ex.getMessage(),
                "Errore DB",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
