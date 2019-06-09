import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginFrame extends JFrame {


    //variabile che contiene il frame stesso, in modo da permettere al main di accedere alle altre proprietà nonostante il metodo di dichiarazione del frame e quello di utilizzo siano diversi
    private LoginFrame loginFrame = this;

    public LoginFrame() throws HeadlessException {
        /*
            IMPOSTAZIONI GRAFICA FINESTRA
         */

        //impostazioni di base JFrame
        setSize(500,150);
        setTitle("Magazzino - Login");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("C:/Users/Lorenzo/Desktop/ProgettoMagazzinoScolastico/icons/login.png").getImage());



        //creazione elementi da inserire
        JLabel lblID = new JLabel("Utente:", JLabel.CENTER);
        JLabel lblPassword = new JLabel("Password:", JLabel.CENTER);
        JLabel lblWrongInput = new JLabel("Impossibile accedere al server!", JLabel.CENTER);
        BottoniOperazioni btnLogin = new BottoniOperazioni("Login");
        JTextField txtID = new JTextField(20);
        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setEchoChar('*');

        /*pannelli contenenti i textfield per password e ID e Bottone per il login*/
        JPanel pnlTxtId = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel pnlTxtPassword = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel pnlBtnLogin = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        pnlTxtId.add(txtID);
        pnlTxtPassword.add(txtPassword);
        pnlBtnLogin.add(btnLogin);


        //gestione dei layout e inserimento elementi
        setLayout(new GridLayout(3,2));

        add(lblID);
        add(pnlTxtId);
        add(lblPassword);
        add(pnlTxtPassword);
        add(lblWrongInput);
        add(pnlBtnLogin);
        lblWrongInput.setVisible(false);

        setResizable(false);



        /*
            CLICK BOTTONE LOGIN
            gestisce gli errori di login(username e password errati), controlla il tipo di accoount con cui si sta accedendo e
            passa in base a questo un valore numerico ad un metodo apposito nella classe MainMagazzino per proseguire l'esecuzione
         */

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //imposto la scritta di errore a non visibile nel caso in cui sia già stato fatto un tentativo di login e sia rimasta visibile
                lblWrongInput.setVisible(false);
                try {
                    //mi connetto al DB usando il metodo public static dbConnect nel main e salvandone il return in un oggetto apposito
                    Connection dbConnection = MainMagazzino.dbConnect();
                    //creo una query sul db nella tabella utenti in cui prendo tutti i record il cui campo ID sia uguale al testo inserito nell' apposita textbox dall' utente
                    //salvo il risultato della query in un ResultSet
                    ResultSet dbReturn = dbConnection.createStatement().executeQuery("SELECT * FROM Utenti WHERE id='"+txtID.getText()+"'");
                    //se il ResultSet è vuoto (non ci sono risultati corrispondenti a quell' Id) segnalo l'errore
                    if(!dbReturn.isBeforeFirst()){
                        lblWrongInput.setText("Utente non esistente");
                        lblWrongInput.setForeground(Color.red);
                        lblWrongInput.setVisible(true);
                        //se sono stati trovati risultati procedo con il controllo della password
                    }else{
                        //se la password digitata dall' utente corrisponde a quella ottenuta nel campo "password" del ResultSet procedo al login
                        if(dbReturn.getString("password").equals(txtPassword.getText())){
                            //in base al tipo di profilo risultante nel DB passo un valore intero ad un metodo statico nel main che si occupa di far proseguire l' esecuzione del programma
                            if(dbReturn.getInt("profileType")==0){
                                MainMagazzino.loginCall(0);
                                dbConnection.close();
                                dispose();
                            }else{
                                MainMagazzino.loginCall(1);
                                dbConnection.close();
                                dispose();
                            }
                            //se la password non coincide segnalo l'errore
                        }else{
                            lblWrongInput.setText("Password errata");
                            lblWrongInput.setForeground(Color.red);
                            lblWrongInput.setVisible(true);
                            dbConnection.close();
                        }
                    }

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        setVisible(true);
    }
}





/*CREA UNA TABELLA NEL DB
    Connection dbConnection= DriverManager.getConnection("jdbc:sqlite:C:/Users/Lorenzo/Desktop/ProgettoMagazzinoScolastico/MagazzinoDB.db");
                    Statement stm;
                    stm = dbConnection.createStatement();
                    stm.executeQuery("CREATE TABLE Utenti(\n"
                            + "id text PRIMARY KEY,\n"
                            + "password text NOT NULL,\n"
                            + "profileType integer NOT NULL\n"
                            + ");");
                    dbConnection.close();
 */



/*AGGIUNGE UN RECORD AL DB
    Connection dbConnection= DriverManager.getConnection("jdbc:sqlite:C:/Users/Lorenzo/Desktop/ProgettoMagazzinoScolastico/MagazzinoDB.db");
    PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO Utenti(id,password,profileType) VALUES(?,?,?)");
                    statement.setString(1, "UtenteMagazzino");
                    statement.setString(2, "UtentePassword");
                    statement.setInt(3, 1);

                    statement.executeUpdate();
 */


/*AGGIORNARE UN CAMPO ESISTENTE IN UNA TABELLA DEL DB
    Connection dbConnection= DriverManager.getConnection("jdbc:sqlite:C:/Users/Lorenzo/Desktop/ProgettoMagazzinoScolastico/MagazzinoDB.db");

                    PreparedStatement statement = dbConnection.prepareStatement("UPDATE Utenti SET profileType = 0 WHERE id = ?");
                    statement.setString(1, "UtenteMagazzino");
                    statement.executeUpdate();
 */
