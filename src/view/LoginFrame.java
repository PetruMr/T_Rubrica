package view;

import controller.LoginController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * La classe LoginFrame rappresenta la vista per le operazioni di login e registrazione.
 * Si occupa unicamente di costruire l’interfaccia grafica: la logica degli eventi e 
 * l'interazione col database sono gestite dal LoginController.
 */
public class LoginFrame extends JFrame {
    
    // Componenti grafici per la sezione di login
    private JTextField usernameFieldLogin;
    private JPasswordField passwordFieldLogin;
    
    // Componenti grafici per la sezione di registrazione
    private JTextField usernameFieldRegister;
    private JPasswordField passwordFieldRegister;
    
    // Pulsanti principali (login, register), ora pubblici per consentire al Controller di impostare i listener
    private JButton loginButton;
    private JButton registerButton;
    
    // Riferimento al Controller che gestisce la logica e gli eventi
    private LoginController controller;
    
    /**
     * Costruttore di LoginFrame (senza parametri) che configura l'interfaccia grafica.
     * Il controller verrà settato separatamente via setController().
     */
    public LoginFrame() {
        // Imposta le proprietà di base del frame
        setTitle("Rubrica - Login e Registrazione");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 250);
        
        // Crea un JTabbedPane per alternare tra login e registrazione
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Pannello per il Login
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(new EmptyBorder(20, 20, 0, 20));
        
        JPanel loginFieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        loginFieldsPanel.add(new JLabel("Username:"));
        usernameFieldLogin = new JTextField(15);
        loginFieldsPanel.add(usernameFieldLogin);
        loginFieldsPanel.add(new JLabel("Password:"));
        passwordFieldLogin = new JPasswordField(15);
        loginFieldsPanel.add(passwordFieldLogin);
        loginPanel.add(loginFieldsPanel);
        
        loginPanel.add(Box.createVerticalStrut(20));
        
        JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton = new JButton("Login");
        loginButtonPanel.add(loginButton);
        loginPanel.add(loginButtonPanel);
        
        // Pannello per la Registrazione
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.setBorder(new EmptyBorder(20, 20, 0, 20));
        
        JPanel registerFieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        registerFieldsPanel.add(new JLabel("Username:"));
        usernameFieldRegister = new JTextField(15);
        registerFieldsPanel.add(usernameFieldRegister);
        registerFieldsPanel.add(new JLabel("Password:"));
        passwordFieldRegister = new JPasswordField(15);
        registerFieldsPanel.add(passwordFieldRegister);
        registerPanel.add(registerFieldsPanel);
        
        registerPanel.add(Box.createVerticalStrut(20));
        
        JPanel registerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerButton = new JButton("Register");
        registerButtonPanel.add(registerButton);
        registerPanel.add(registerButtonPanel);
        
        // Aggiunge i pannelli al tabbed pane
        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Registrazione", registerPanel);
        
        // Aggiunge il tabbedPane al frame
        add(tabbedPane);
        
        // Centra il frame sullo schermo (senza renderlo subito visibile)
        setLocationRelativeTo(null);
    }
    
    /**
     * Metodo di "collegamento" per assegnare il controller.
     * Qui si impostano i listener dei pulsanti in modo che richiamino i metodi del controller.
     *
     * @param controller Il LoginController che gestisce la logica di login e registrazione.
     */
    public void setController(LoginController controller) {
        this.controller = controller;
        
        // Imposta i listener per i pulsanti, rimandando la logica al controller
        loginButton.addActionListener(e -> controller.handleLogin());
        registerButton.addActionListener(e -> controller.handleRegister());
    }
    
    
    
    
    // Metodi "get" per recuperare i valori di input
    
    /**
     * @return Il testo presente nel campo username del pannello di login.
     */
    public String getUsernameLogin() {
        return usernameFieldLogin.getText();
    }
    
    /**
     * @return Il testo presente nel campo password del pannello di login.
     */
    public String getPasswordLogin() {
        return new String(passwordFieldLogin.getPassword());
    }
    
    /**
     * @return Il testo presente nel campo username del pannello di registrazione.
     */
    public String getUsernameRegister() {
        return usernameFieldRegister.getText();
    }
    
    /**
     * @return Il testo presente nel campo password del pannello di registrazione.
     */
    public String getPasswordRegister() {
        return new String(passwordFieldRegister.getPassword());
    }
    


    // Metodi per la visualizzazione di messaggi e notifiche
    
    /**
     * Mostra un messaggio di errore in un JOptionPane.
     *
     * @param messaggio Il messaggio da mostrare all'utente.
     */
    public void mostraMessaggioErrore(String messaggio) {
        JOptionPane.showMessageDialog(
                this,
                messaggio,
                "Errore",
                JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Mostra un messaggio informativo in un JOptionPane.
     *
     * @param messaggio Il messaggio da mostrare all'utente.
     */
    public void mostraMessaggioInfo(String messaggio) {
        JOptionPane.showMessageDialog(
                this,
                messaggio,
                "Informazione",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
