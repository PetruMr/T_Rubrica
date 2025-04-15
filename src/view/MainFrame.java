package view;

import models.Persona;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

/**
 * MainFrame rappresenta la finestra principale della rubrica.
 * Visualizza una JTable con le persone (solo Nome, Cognome, Telefono)
 * e una JToolBar contenente tre pulsanti: uno per l'aggiunta di nuove persone,
 * uno per la modifica e uno per l'eliminazione.
 */
public class MainFrame extends JFrame {

    // Tabella e relativo modello dati
    private JTable personTable;
    private DefaultTableModel tableModel;

    // Pulsanti (ora inseriti nella JToolBar)
    private JButton btnNuovo;    // Pulsante per aggiungere una nuova persona
    private JButton btnModifica; // Pulsante per modificare la persona selezionata
    private JButton btnElimina;  // Pulsante per eliminare la persona selezionata

    // Lista interna di Persona per poter ricostruire l’oggetto selezionato
    private Vector<Persona> personList = new Vector<>();

    /**
     * Costruttore di default: imposta titolo, dimensioni e layout,
     * crea la tabella con le colonne desiderate e inserisce la JToolBar
     * in cui sono presenti i pulsanti (sotto forma di icone).
     */
    public MainFrame() {
        super("Rubrica");

        // Configura la finestra principale
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        /*
         * Inizializza il modello della tabella con tre colonne:
         * Nome, Cognome, Telefono.
         * Le celle non sono modificabili dall’utente.
         */
        tableModel = new DefaultTableModel(new String[]{"Nome", "Cognome", "Telefono"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rende non modificabili le celle
            }
        };

        // Crea la JTable associandola al tableModel
        personTable = new JTable(tableModel);

        /*
         * Avvolge la JTable in uno JScrollPane per gestire
         * automaticamente le barre di scorrimento.
         */
        JScrollPane scrollPane = new JScrollPane(personTable);
        add(scrollPane, BorderLayout.CENTER);

        /*
         * Creazione di una JToolBar che conterrà i pulsanti
         * per le operazioni di inserimento, modifica ed eliminazione.
         */
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); // Evita che la toolbar possa essere spostata dall’utente

        // Creazione dei pulsanti con icone
        ImageIcon iconNuovo = new ImageIcon(getClass().getResource("/images/add.png"));
        btnNuovo = new JButton(new ImageIcon(iconNuovo.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
        btnNuovo.setToolTipText("Aggiungi una nuova persona alla rubrica");
        btnNuovo.setPreferredSize(new Dimension(64, 64));
        toolBar.add(btnNuovo);

        ImageIcon iconModifica = new ImageIcon(getClass().getResource("/images/modify.png"));
        btnModifica = new JButton(new ImageIcon(iconModifica.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
        btnModifica.setToolTipText("Modifica i dati della persona selezionata");
        btnModifica.setPreferredSize(new Dimension(64, 64));
        toolBar.add(btnModifica);

        ImageIcon iconElimina = new ImageIcon(getClass().getResource("/images/remove.png"));
        btnElimina = new JButton(new ImageIcon(iconElimina.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
        btnElimina.setToolTipText("Elimina la persona selezionata dalla rubrica");
        btnElimina.setPreferredSize(new Dimension(64, 64));
        toolBar.add(btnElimina);

        // Aggiunge la toolbar nella parte superiore della finestra con padding
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(toolBar);
        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Aggiorna la tabella con i dati presenti nel Vector di Persona.
     * Vengono mostrate solo le colonne: Nome, Cognome, Telefono.
     *
     * @param persons Il Vector contenente le persone da visualizzare.
     */
    public void updateTableData(Vector<Persona> persons) {
        // Salva localmente la lista di persone
        this.personList = persons;
        // Svuota il modello prima di reinserire i dati
        tableModel.setRowCount(0);

        // Inserisce i dati riga per riga all’interno del modello
        for (Persona p : persons) {
            Object[] rowData = { p.getNome(), p.getCognome(), p.getTelefono() };
            tableModel.addRow(rowData);
        }
    }

    /**
     * Restituisce la Persona selezionata nella JTable (in base all’indice di riga).
     * Se non è selezionata alcuna riga, o l’indice è fuori range, restituisce null.
     *
     * @return L'oggetto Persona corrispondente alla riga selezionata, altrimenti null.
     */
    public Persona getSelectedPersona() {
        int rowIndex = personTable.getSelectedRow();
        if (rowIndex < 0 || rowIndex >= personList.size()) {
            return null; // Nessuna persona valida selezionata
        }
        // Ritorna la Persona corrispondente a quell’indice nel Vector
        return personList.get(rowIndex);
    }

    /**
     * @return Il pulsante "Nuovo" (ora contenuto nella JToolBar).
     */
    public JButton getBtnNuovo() {
        return btnNuovo;
    }

    /**
     * @return Il pulsante "Modifica" (ora contenuto nella JToolBar).
     */
    public JButton getBtnModifica() {
        return btnModifica;
    }

    /**
     * @return Il pulsante "Elimina" (ora contenuto nella JToolBar).
     */
    public JButton getBtnElimina() {
        return btnElimina;
    }
}
