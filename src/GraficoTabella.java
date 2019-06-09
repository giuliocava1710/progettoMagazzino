import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GraficoTabella extends JPanel {
    //creo il grafico come proprietà, in modo che sia costantemente modificabile nel programma
    private JCartesian pnlCartesian = new JCartesian( 800,700,-5,62,-10,700);
    public GraficoTabella() {
        /*
         * setto il layout della finestra a border
         *
         * SUL GRAFICO
         * uso il metodo per disegnare gli assi
         * setto il layout a null in modo da poter piazzare componenti in base alle coordinate nella finestra
         * aggiungo il pannello nella zona centrale del frame
         * */
        setLayout(new BorderLayout());

        pnlCartesian.drawAxis();

        pnlCartesian.setLayout(null);
        //refresh in grafica
        pnlCartesian.validate();
        pnlCartesian.repaint();

        add(pnlCartesian, BorderLayout.CENTER);






        /*
         * Creo un nuovo pannello nel quale inserisco la tabella al fine di ottenere un margine piu ampio nei bordi
         * Inizializzo una nuova tabella alla quale setto il modello di default, definisco i nomi delle colonne
         * un ciclo for di colonne le aggiunge alla tabella
         * Al JScrollPane passo la tabella con il modello settato.
         * aggiungo la tbella quindi al pannello e il panello della parte destra del frame
         * */
        JPanel pnlTab = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTable tblModifiche=new JTable();


        //richiamo il metodo loadTableModifiche, che restituisce un DefaultTableModel
        tblModifiche.setModel(loadTableModifiche());
        //imposto la tabella in modo che la colonna delle modifiche sia leggibile
        tblModifiche.getColumnModel().getColumn(4).setMinWidth(200);
        JScrollPane scrollRichieste=new JScrollPane(tblModifiche);
        pnlTab.add(scrollRichieste);
        add(pnlTab, BorderLayout.EAST);


         /*
             Creo due combobox, necessarie al selezionamento di anno e mese da visualizzare nel grafico
         */
        JPanel pnlComboAnnoMese = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox cmbAnno = new JComboBox();
        //carico nella combo il model di ritorno da un apposito metodo che inserisce tutti gli anni dal più remoto a oggi
        cmbAnno.setModel(new DefaultComboBoxModel(getAnni().toArray()));
        //seleziono nella combo l' ultimo valore, ovvero l' anno odierno
        cmbAnno.setSelectedIndex(cmbAnno.getItemCount() -1);
        //carico una seconda combo con tutti i mesi
        JComboBox cmbMese = new JComboBox();
        String mesi[]={
                "01 - Gennaio",
                "02 - Febbraio",
                "03 - Marzo",
                "04 - Aprile",
                "05 - Maggio",
                "06 - Giugno",
                "07 - Luglio",
                "08 - Agosto",
                "09 - Settembre",
                "10 - Ottobre",
                "11 - Novembre",
                "12 - Dicembre"
        };
        DefaultComboBoxModel mdlCmbMese = new DefaultComboBoxModel(mesi);
        cmbMese.setModel(mdlCmbMese);
        //aggiungo entrambe le combo ad un apposito panel
        pnlComboAnnoMese.add(cmbAnno);
        pnlComboAnnoMese.add(cmbMese);

        add(pnlComboAnnoMese, BorderLayout.SOUTH);

        //richiamo il metodo loadGrafico, che disegna il grafico basandosi su anno e mesi selezionati
        loadGrafico(cmbAnno.getSelectedItem().toString(), cmbMese.getSelectedItem().toString());

        /*
            aggiungo gli itemiListener alle combo in modo che quando l' elemento selezionato cambia venga ricaricato il grafico
         */
        cmbAnno.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                loadGrafico(cmbAnno.getSelectedItem().toString(), cmbMese.getSelectedItem().toString());
            }
        });

        cmbMese.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                loadGrafico(cmbAnno.getSelectedItem().toString(), cmbMese.getSelectedItem().toString());
            }
        });



    }

    private ArrayList<String> getAnni(){
        //mi connetto al db
        Connection dbConnection = MainMagazzino.dbConnect();
        ResultSet dbReturn = null;
        try {
            //ottengo la data minore nel db
            dbReturn = dbConnection.createStatement().executeQuery("SELECT Data FROM Modifiche ORDER BY Data ASC LIMIT 1");
            //calcolo il tempo attuale dal sistema
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dataOraAttuale = sdf.format(new Timestamp(System.currentTimeMillis()));
            //separo la stringa in modo da ottenere solo l'anno
            String dataAttuale = dataOraAttuale.substring(0, dataOraAttuale.indexOf("-"));
            ArrayList<String> anni = new ArrayList<>();
            //aggiungo all' arraylist degli anni tutti gli anni basandomi sul meno recente nel database e arrivando fino a quello ottenuto dal sistema
            for(int anno=Integer.parseInt(dbReturn.getString("Data").substring(0, dbReturn.getString("Data").indexOf("-"))); anno<= Integer.parseInt(dataAttuale); anno++){
                anni.add(String.valueOf(anno));
            }
            dbConnection.close();
            return anni;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadGrafico(String anno, String mese) {
        //rimuovo tutti i componenti e il disegno dal grafico
        pnlCartesian.removeAll();
        pnlCartesian.clean();
        //disegno nuovamente gli assi e i riferimenti sull' asse x
        pnlCartesian.drawAxis();
        pnlCartesian.drawXReferences();
        pnlCartesian.drawYReferences(50);
        //ottengo il numero del mese dalla stringa presa dalla combobox e passata come parametro
        mese=mese.substring(0,mese.indexOf(" "));
        //creo una stringa pattern che indica una data in un formato yyyy-mm-__ dove gli ultimi due caratteri sono i caratteri generici singoli di SQL
        String pattern = anno + "-" + mese + "-__";
        try {
            //mi connetto al db
            Connection dbConnection = MainMagazzino.dbConnect();
            /*
            DA IMPLEMENTARE LA MASSIMA Y DINAMICA IN BASE AL VALORE PIU ALTO DEL MAGAZZINO
            PreparedStatement statementValore = dbConnection.prepareStatement("SELECT MAX(Valore) AS Max FROM Modifiche WHERE Data LIKE ?");
            statementValore.setString(1, pattern);
            ResultSet dbReturnValore = statementValore.executeQuery();
            pnlCartesian = new JCartesian(800,700, -1,62,-10,dbReturnValore.getFloat("Max"));
            */
            //eseguo la query sul database per ottenere Data, Ora(?) e Valore di tutti i record che rispettano il pattern della data
            PreparedStatement statement = dbConnection.prepareStatement("SELECT Data, Ora, Valore FROM Modifiche WHERE Data LIKE ?");
            statement.setString(1, pattern);
            ResultSet dbReturn  = statement.executeQuery();
            //ottengo dal risultato il primo record, gestendolo fuori dal ciclo successivo
            //prendo i dati di Data e Valore del primo record, usando la substring per ottenere solo il giorno dalla data
            String giorno = dbReturn.getString("Data");
            giorno = giorno.substring(8, 10);
            float valore = dbReturn.getFloat("Valore");
            /*
            MANCANO I JBUTTON SUL GRAFICO
            //creo un bottone che sarà posizionato all' inizio della prima riga disegnata grazie al metodo setBounds a cui passo le coordinate convertite dalla classe JCartesian
            JButton btnFirstToAdd = new JButton();
            btnFirstToAdd.setBounds(JCartesian.convX(Integer.parseInt(giorno)), JCartesian.convY(valore), 10, 10);
            //aggiungo il bottone al grafico grazie al suo layout null
            pnlCartesian.add(btnFirstToAdd);
             */

            //ciclo finchè esistono record nella tabella risultato della query
            while (dbReturn.next()) {
                //ottengo con lo stesso metodo precedente giorno e valore nel secondo record
                String giorno2 = dbReturn.getString("Data");
                giorno2 = giorno2.substring(8, 10);
                float valore2 = dbReturn.getFloat("Valore");
                //controllo che il giorno precedentemente usato come coordinata X non sia uguale al nuovo giorno ottenuto. Se lo è, non prendo il record in considerazione
                if(!giorno.equals(giorno2)) {
                    //disegno una linea che usa come coordinate giorno e valore dei due record
                    pnlCartesian.drawLineCart(Integer.parseInt(giorno), valore, Integer.parseInt(giorno2), valore2);
                    /*
                    MANCANO I JBUTTON SUL GRAFICO
                    //creo un bottone e lo posiziono al termine della riga appena creata con il metodo setBounds
                    JButton btnToAdd = new JButton();
                    btnToAdd.setBounds(JCartesian.convX(Integer.parseInt(giorno2)), JCartesian.convY(valore2), 10, 10);
                    pnlCartesian.add(btnToAdd);
                    */
                    //refresho la grafica
                    pnlCartesian.revalidate();
                    pnlCartesian.repaint();
                    //imposto le nuove coordinate sulle prime due, in modo che siano usate come partenza per la prossima linea
                    giorno = giorno2;
                    valore = valore2;
                }



            }
            //chiudo la connessione
            dbConnection.close();
        } catch (SQLException e) {
            //l' eccezzione che può avvenire è la SQLException ResultSet Closed, che indica che la connessione è stata chiusa o che il resultest non conteneva più record ed è stato chiuso automaticamente
            //quando viene scatenata, azzero il grafico, disegnando assi e riferimenti
            pnlCartesian.clean();
            pnlCartesian.drawAxis();
            pnlCartesian.drawXReferences();
            pnlCartesian.drawYReferences(50);
        }

    }

    private TableModel loadTableModifiche() {
        //creo il modello della tabella caricando i campi che conterrà in un array
        String[]colonne={"ID", "Data","Ora","Valore","Modifica"};
        DefaultTableModel model = new DefaultTableModel();
        for(String colonna:colonne){
            model.addColumn(colonna);
        }
        try {
            //mi connetto al db e ottengo l' intera tabella delle modifiche con l'apposita query
            Connection dbConnection = MainMagazzino.dbConnect();
            ResultSet dbReturn = dbConnection.createStatement().executeQuery("SELECT * FROM Modifiche");
            //finchè il risultato contiene record, aggiungo campo per campo al modello
            while(dbReturn.next()){
                //per aggiungere i campi ottengo i vari dati grazie agli appositi metodi dal resultset e li aggiungo ad un array, che poi aggiungo al model
                Object[] singleDbRow = {
                        String.valueOf(dbReturn.getInt("NumeroModifica")),
                        dbReturn.getString("Data"),
                        dbReturn.getString("Ora"),
                        String.valueOf(dbReturn.getFloat("Valore")),
                        dbReturn.getString("Modifica"),
                };

                model.addRow(singleDbRow);
            }
            //chiudo la connessione e restituisco il model
            dbConnection.close();
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
