import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainMagazzino {
    final static private String CONNECTION_PATH =  "jdbc:sqlite:MagazzinoDB.db";
    public static void main(String[] args) throws InterruptedException {
        /*
            ISTANZIAZIONE FINESTRA DI LOGIN
         */
        try {
            Class.forName("org.sqlite.JDBC");
            LoginFrame loginFrame = new LoginFrame();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Impossibile trovare i driver JDBC adatti.");
            e.printStackTrace();
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null,"è stata generata un'eccezione");
            System.exit(0);
        }





    }

    /*
        GESTIONE DEL LOGIN
        il metodo viene chiamato dalla classe LoginFrame dopo aver eseguito i controlli sul login. Il valore passato dipende dal tipo di account con cui si accede. I due tipi di account vengono determinati nel database
     */

    public static void loginCall(int profileType){
        //in base al valore assunto dalla variabile viene lanciata una finestra o l' altra
        if(profileType==0){
            userMode();
        }else if(profileType==1){
            adminMode();
        }
    }

    /*
        CONNESSIONE AL DATABASE
        esegue una connessione al database e la restituisce al metodo chiamante. È importante chiudere la connessione dopo aver usato questo metodo
     */
    public static Connection dbConnect(){
        try {
            return DriverManager.getConnection(CONNECTION_PATH);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
        LETTURA DELLE CATEGORIE PRESENTI NEL DATABASE
        scorre tutta la colonna "Categoria" del database e aggiunge ogni categoria non ancora presente all' interno dell' arraylist apposito.
        Usato per caricare le combobox delle categorie
     */

    public static ArrayList<String> getDbCategories(ArrayList<String> lista){
        Connection dbConnection = dbConnect();
        try {
            ResultSet dbReturn = dbConnection.createStatement().executeQuery("SELECT Categoria FROM INF UNION SELECT Categoria FROM CHI UNION SELECT Categoria FROM ELE UNION SELECT Categoria FROM ROB UNION SELECT Categoria FROM MEC");
            while(dbReturn.next()){
                if(!(lista.contains(dbReturn.getString("Categoria")))) {
                    lista.add(dbReturn.getString("Categoria"));
                }
            }
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /*
        COSTRUTTORE DELLE TABELLE DEI PRODOTTI
        Costruisce un TableModel da settare sulle tabelle dei prodotti. Imposta le colonne della tabella,
        si connette al database e esegue una lettura dell' intera tabella necessaria, imposta i corretti valori ricavati
        e ritorna il TableModel alla funzione chiamante
     */
    public static TableModel tableBuilder(String table) {
        Object[] headers = {"Codice Prodotto", "Categoria", "Nome/Descrizione", "Quantità", "Numero Scaffale", "Costo", "Costo Totale", "Aliquota", "CostoAmmortato", "DataAcquisto"};
        DefaultTableModel tblModel = new DefaultTableModel(headers, 0);

        Connection dbConnection = MainMagazzino.dbConnect();

        try {
            if(!table.equals("GEN")) {
                ResultSet dbReturn = dbConnection.createStatement().executeQuery("SELECT * FROM " + table);
                while (dbReturn.next()) {

                    Object[] singleDbRow = {String.valueOf(dbReturn.getInt("CodiceProdotto")),
                            dbReturn.getString("Categoria"),
                            dbReturn.getString("Nome"),
                            String.valueOf(dbReturn.getInt("Quantita")),
                            dbReturn.getString("NumScaffale"),
                            String.valueOf(dbReturn.getInt("Costo")),
                            String.valueOf(dbReturn.getInt("CostoTotale")),
                            String.valueOf(dbReturn.getInt("Aliquota")),
                            dbReturn.getFloat("CostoAmmortato"),
                            dbReturn.getString("DataAcquisto")
                    };

                    tblModel.addRow(singleDbRow);
                }
            }else{
                ResultSet dbReturn = dbConnection.createStatement().executeQuery("SELECT * FROM INF UNION SELECT * FROM CHI UNION SELECT * FROM ELE UNION SELECT * FROM ROB UNION SELECT * FROM MEC");
                while(dbReturn.next()){
                    Object[] singleDbRow = {String.valueOf(dbReturn.getInt("CodiceProdotto")),
                            dbReturn.getString("Categoria"),
                            dbReturn.getString("Nome"),
                            String.valueOf(dbReturn.getInt("Quantita")),
                            dbReturn.getString("NumScaffale"),
                            String.valueOf(dbReturn.getInt("Costo")),
                            String.valueOf(dbReturn.getInt("CostoTotale")),
                            String.valueOf(dbReturn.getInt("Aliquota")),
                            dbReturn.getFloat("CostoAmmortato"),
                            dbReturn.getString("DataAcquisto")
                    };
                    tblModel.addRow(singleDbRow);


                }
            }
            //AggiornaAmmortamento.RefreshAmmortamento(tblModel);
            return tblModel;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;


    }

    /*
        GESTIONE ESECUZIONE
        2 metodi appositi quando evocati istanziano l' interfaccia utente e l' interfaccia amministratore
     */
    private static void userMode() { UserMode userMode = new UserMode(); }

    private static void adminMode() { AdminMode adminMode = new AdminMode();
    }

    /*
        TABELLA DELLE MODIFICHE
        Il metodo viene richiamato ad ogni aggiunta, modifica e rimozione al db di un indirizzo e aggiorna la tabella
        delle modifiche con il valore totale del database dopo la modifica e i campi necessari
     */

    public static void updateModifiche(String txtModifica) throws SQLException {
        Connection dbConnection = dbConnect();
        //aggiornamento della tabella delle modifiche
        //calcolo il totale del valore del magazzino dopo la somma
        ResultSet dbReturnInf = dbConnection.createStatement().executeQuery("SELECT SUM(CostoTotale) AS 'ValoreTotale' FROM INF");
        ResultSet dbReturnChi = dbConnection.createStatement().executeQuery("SELECT SUM(CostoTotale) AS 'ValoreTotale' FROM CHI");
        ResultSet dbReturnMec = dbConnection.createStatement().executeQuery("SELECT SUM(CostoTotale) AS 'ValoreTotale' FROM MEC");
        ResultSet dbReturnEle = dbConnection.createStatement().executeQuery("SELECT SUM(CostoTotale) AS 'ValoreTotale' FROM ELE");
        ResultSet dbReturnRob = dbConnection.createStatement().executeQuery("SELECT SUM(CostoTotale) AS 'ValoreTotale' FROM ROB");


        float totVal = dbReturnInf.getFloat("ValoreTotale")+dbReturnChi.getFloat("ValoreTotale")+dbReturnEle.getFloat("ValoreTotale")+dbReturnMec.getFloat("ValoreTotale")+dbReturnRob.getFloat("ValoreTotale");
        //ottengo dal sistema data e ora e salvo entrambi in una stringa
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataOra = sdf.format(new Timestamp(System.currentTimeMillis()));
        //separo la stringa in due sottostringhe
        String data = dataOra.substring(0, dataOra.indexOf(" "));
        String ora = dataOra.substring(dataOra.indexOf(" "));

        //creo la queri inserendo i dati ottenuti
        PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO Modifiche(Data,Ora,Valore,Modifica) VALUES(?,?,?,?)");
        statement.setString(1, data);
        statement.setString(2, ora);
        statement.setFloat(3, totVal);
        statement.setString(4, txtModifica);
        //eseguo la query
        statement.executeUpdate();
        dbConnection.close();
    }
}
