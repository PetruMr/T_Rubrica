/* 
-- Scelte progettuali del DB.
-- In quanto vi sono unicamente due tabelle con relazione N-1, non mi sono impegnato nella creazione di un diagramma ER.

    Questo file contiene lo script da utilizzare per la creazione del DB MySQL che gestisce Persona ed Utente.

    In particolare:
    - Persona è formato da 
        - id (PK, INT, AUTO_INCREMENT)
        - utente.id (FK, INT, NOT NULL)
        - nome (VARCHAR(1000), NOT NULL)
        - cognome (VARCHAR(1000), NOT NULL)
        - indirizzo (VARCHAR(1000), NOT NULL)
        - telefono (VARCHAR(1000), NOT NULL)
        - eta (INT, NOT NULL)
    - Utente è formato da 
        - id (PK, INT, AUTO_INCREMENT)
        - username (VARCHAR(1000), NOT NULL)
        - password (VARCHAR(64), NOT NULL)
        - salt (VARCHAR(16), NOT NULL)

    Ogni utente avrà una o più persone associate (ovvero ogni utente può avere più contatti in rubrica). Anche se due "Persone" sono uguali, se esse non appartengono allo stesso utente sono considerabili completamente diverse, pertanto con l'uso di PK diverse si evita di avere conflitti. 

    Per nome, cognome, indirizzo e telefono sono stati utilizzati VARCHAR da 1000 caratteri. Questo è molto più di quanto è permesso avere su una SIM (20-30 caratteri per il nome ad esempio), ma è stato scelto per evitare problemi di overflow. Inoltre, non si sono utilizzati TEXT o LONGTEXT in quanto hanno performance peggiori rispetto a VARCHAR - ma questo può essere eventualmente modificato.

    Per quanto riguarda l'utente, esso ha una password salvata come hash (SHA256) e un salt di 16 caratteri. La password è lunga 64 caratteri in quanto l'hash SHA256 produce un output di 64 caratteri esadecimali.
*/


-- script: schema_database.sql
-- Descrizione: Script per la creazione del database della rubrica telefonica
-- Comprende tabelle per Utente e Persona

-- Creazione del database
CREATE DATABASE IF NOT EXISTS RubricaDB;
USE RubricaDB;

-- Tabella Utente
CREATE TABLE IF NOT EXISTS Utente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(1000) NOT NULL,
    password VARCHAR(64) NOT NULL,
    salt VARCHAR(8) NOT NULL
);

-- Tabella Persona
CREATE TABLE IF NOT EXISTS Persona (
    id INT AUTO_INCREMENT PRIMARY KEY,
    utente_id INT NOT NULL,
    nome VARCHAR(1000) NOT NULL,
    cognome VARCHAR(1000) NOT NULL,
    indirizzo VARCHAR(1000) NOT NULL,
    telefono VARCHAR(1000) NOT NULL,
    eta INT NOT NULL,
    FOREIGN KEY (utente_id) REFERENCES Utente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Indice aggiuntivo (opzionale) per migliorare le query su utente_id
CREATE INDEX idx_persona_utente_id ON Persona (utente_id);

