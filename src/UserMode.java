import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UserMode extends JFrame {

    private UserMode userMode = this;
    private static JTable tblDb = new JTable(){
        public boolean isCellEditable(int row, int col){
            return false;
        }
    };
    private JComboBox<String> cmbRicerca;
    private ArrayList<String> listaCategorie = new ArrayList<>();
    private static String selectedTable = "GEN";

    public UserMode() {

        //imposto l' impossibilità di selezionare più di una riga per volta nella tabella
        tblDb.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.setTitle("Utente");
        this.setSize(700, 350);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        /*try catch utile a prendere lo stile grafico del sistema su cui gira il programma
         * per adattarlo esempio da Windows10 o da Windows7 etc...*/
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        /*istanza dei pannelli dove verranno inseriti i bottoni
         * per la scelta degli indirizzi*/
        JPanel pnlWest = new JPanel(new GridLayout(2, 1));
        JPanel pnlIndirizzi = new JPanel(new GridLayout(6, 1));

        /*istanza e inizializzazione dei pulsanti situati a sinistra per
         * la scelta degli indirizzi Chimica, Informatica, Meccanica, Elettrotecnica, Robotica
         * oppure un pulsante per selezionare tutti gli indirizzi quindi la ricerca */

        /*bottone per tutti gli indirizzi*/
        BottoniIndirizzi btnTUTTO = new BottoniIndirizzi("GENERALE");
        btnTUTTO.setForeground(Color.BLACK);
        BottoniIndirizzi.setBtn(btnTUTTO);
        btnTUTTO.setFont(new Font("Arial", Font.PLAIN, 12));
        btnTUTTO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTable="GEN";
                reloadTable(tblDb);
            }
        });
        /*bottone per l'indirizzo di informatica*/
        BottoniIndirizzi btnINF = new BottoniIndirizzi("INF");
        btnINF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTable="INF";
                reloadTable(tblDb);
            }
        });

        /*bottone per l'indirizzo di chimica*/
        BottoniIndirizzi btnCHI = new BottoniIndirizzi("CHI");
        btnCHI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTable="CHI";
                reloadTable(tblDb);
            }
        });

        /*bottone per l'indirizzo di meccanica*/
        BottoniIndirizzi btnMEC = new BottoniIndirizzi("MEC");
        btnMEC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTable="MEC";
                reloadTable(tblDb);
            }
        });


        /*bottone per l'indirizzo di elettrotecnica*/
        BottoniIndirizzi btnELE = new BottoniIndirizzi("ELE");
        btnELE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTable="ELE";
                reloadTable(tblDb);
            }
        });

        /*bottone per l'indirizzo di robotica*/
        BottoniIndirizzi btnROB = new BottoniIndirizzi("ROB");
        btnROB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTable="ROB";
                reloadTable(tblDb);
            }
        });



        /*aggiunta dei bottoni al pannello indirizzi diviso in 6 righe e 1 colonna.*/
        pnlIndirizzi.add(btnTUTTO);
        pnlIndirizzi.add(btnINF);
        pnlIndirizzi.add(btnCHI);
        pnlIndirizzi.add(btnMEC);
        pnlIndirizzi.add(btnELE);
        pnlIndirizzi.add(btnROB);

        /*aggiunta del pannello contenente i bottoni degli indirizzi al pannello west diviso in 2 righe e 1 colonna*/
        pnlWest.add(pnlIndirizzi);
        /*aggiunta del pannello west al JFrame UserMode posizionato nel borderLayout a OVEST */
        this.add(pnlWest, BorderLayout.WEST);

        /*pannello centrale diviso in due parti, la prima superiore contiene la barra di ricerca, la combobox contenente
         * le categorie e il bottone di ricerca*/
        JPanel pnlCenter = new JPanel(new GridLayout(2, 1));

        /*pannello inserito nella prima cella del pnlCenter divisa in due parti contenenti la prima la barra di ricerca e
         * seconda la combobox e il bottone cerca*/
        JPanel pnlRicerca = new JPanel(new GridLayout(2, 1));

        /*pannello contenente la barra di ricerca impostato su flowlayout*/
        JPanel pnlTxtCerca = new JPanel(new FlowLayout(FlowLayout.CENTER));
        /*barra di ricerca*/
        JTextField txtCerca = new JTextField(25);
        pnlTxtCerca.add(txtCerca);

        /*panel contenente la combobox per le categorie di ricerca divisa in flowlayout*/
        JPanel pnlCmbRicerca = new JPanel(new FlowLayout(FlowLayout.CENTER));
        /*combobox per le catgorie*/
        cmbRicerca=new JComboBox<>();
        //richiamo il metodo che ricarica la combo
        reloadComboCategorie(cmbRicerca);
        pnlCmbRicerca.add(cmbRicerca);

        /*panel contenente il bottone per la ricerca impostato su flowlayouy*/
        JPanel pnlBtnCerca = new JPanel(new FlowLayout(FlowLayout.CENTER));
        /*Button di ricerca*/
        BottoniOperazioni btnCerca = new BottoniOperazioni("Cerca");
        //disabilito il bottone inizialmente, e lo abilito/disabilito quando nel txtCerca viene inserito/rimosso il testo
        btnCerca.setEnabled(false);
        txtCerca.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { btnCerca.setEnabled(true); }

            @Override
            public void removeUpdate(DocumentEvent e) { btnCerca.setEnabled(false); }

            @Override
            public void changedUpdate(DocumentEvent e) { }
        });

        //alla pressione sul bottone cerca controllo che il testo non sia nullo(impossibile) e richiamo l'apposito metodo
        btnCerca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtCerca.getText()!=null) {
                    ricerca(txtCerca.getText());
                }
            }
        });

        pnlBtnCerca.add(btnCerca);

        /*pannello impostato su gridlayout con 2 colonne e 1 riga contenente i pannelli
         * della combobox per le categoire e il bottone per la ricerca dei prodotti*/
        JPanel pnlComboButtonRicerca = new JPanel(new GridLayout(1, 2));

        /*aggiunta dei pannelli istanziati precedentemente*/
        pnlComboButtonRicerca.add(pnlCmbRicerca);
        pnlComboButtonRicerca.add(pnlBtnCerca);

        /*aggiunta dei pannelli contenenti la barra di ricerca e la combo con il bottone al pannello più grande*/
        pnlRicerca.add(pnlTxtCerca);
        pnlRicerca.add(pnlComboButtonRicerca);

        /*aggiunta di tutti i pannelli al pannello centrale*/
        pnlCenter.add(pnlRicerca);

        /*tabella con i risultati della ricerca*/


        JScrollPane scrollPaneTabella = new JScrollPane(tblDb);
        //richiamo il metodo di ricaricamento della tabella
        reloadTable(tblDb);

        pnlCenter.add(scrollPaneTabella);

        this.add(pnlCenter, BorderLayout.CENTER);

        BottoniOperazioni btnRichiedi = new BottoniOperazioni("Richiedi oggetto");
        //disabilito inizialmente il bottone per le richieste
        btnRichiedi.setEnabled(false);
        JPanel pnlBtnRichiedi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtnRichiedi.add(btnRichiedi);
        tblDb.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //ad ogni selezione/deselezione imposto i bottoni come attivati/disattivati basandomi sul risultato dell' operazione
                //di casting della tabella (chiamata e nel metodo in quanto fonte dell' evento) in un ListSelectionModel, dal quale poi controllo se ci siano righe selezionate
                btnRichiedi.setEnabled(!((ListSelectionModel)e.getSource()).isSelectionEmpty());
            }
        });

        //al click sul bottone richiedi richiamo il metodo apposito
        btnRichiedi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RequestFrame requestFrame = new RequestFrame(tblDb, userMode);
            }
        });

        this.add(pnlBtnRichiedi, BorderLayout.SOUTH);

        /*impostazione sfondi dell'interfaccia grafica utente*/
        pnlTxtCerca.setBackground(Color.WHITE);
        pnlBtnCerca.setBackground(Color.WHITE);
        pnlCmbRicerca.setBackground(Color.WHITE);
        pnlBtnRichiedi.setBackground(Color.WHITE);
        pnlWest.setBackground(Color.WHITE);


        this.setVisible(true);
    }


    /*
        METODO DI RICERCA
        Il metodo, ottenuta una stringa, richiama i rispettivi metodi a seconda del valore scelto nella combo delle categorie
     */
    private void ricerca(String indexNome) {
        //se l' utente sceglie "Tutte le categorie" richiamo il metodo che non tiene conto della categoria passando solo la stringa del nome
        if(cmbRicerca.getSelectedItem().toString().equals("Tutte le categorie")){
            if(!selectTableRow(indexNome)){
                JOptionPane.showMessageDialog(null, "Nessun prodotto corrispondente trovato.");
            }
        }else {
            //se l' utente seleziona una categoria, richiamo il metodo apposito di ricerca e controllo in una if che questo non dia risultato false(nessun prodotto trovato)
            if (!selectTabelRow(cmbRicerca.getSelectedItem().toString(), indexNome)) {
                JOptionPane.showMessageDialog(null, "Nessun prodotto corrispondente trovato.");
            }
        }
    }

    //!!!COPIA-INCOLLA DA ADMINMODE!!!
    private void reloadComboCategorie(JComboBox cmbCategoria) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(MainMagazzino.getDbCategories(listaCategorie).toArray());
        model.addElement("Tutte le categorie");
        cmbCategoria.setModel(model);
        cmbCategoria.setSelectedIndex(0);
    }

    //!!!COPIA-INCOLLA DA ADMINMODE!!!
    public static void reloadTable(JTable tblDb) {
        tblDb.setModel(MainMagazzino.tableBuilder(selectedTable));
    }




    //!!!COPIA-INCOLLA DA ADMINMODE!!!
    //mai usato, sostituito con gli altri due
    public static void selectTableRow(int index){
        for(int i=0; i<tblDb.getModel().getRowCount(); i++) {
            boolean found=false;
            if((tblDb.getModel().getValueAt(i, 0).equals(String.valueOf(index)))&&!found){
                tblDb.setRowSelectionInterval(i, i );
                found=true;
            }
        }
    }

    public static boolean selectTableRow(String indexName){
        //ricevuto un nome, scorro tutta la tabella finchè non trovo un prodotto con lo stesso nome
        boolean found = false;
        for (int i=0; i<tblDb.getModel().getRowCount(); i++){
            if(tblDb.getModel().getValueAt(i,2).equals(indexName)&&!found){
                //quando trovo il prodotto lo seleziono(i come intervallo minimo e massimo di selezione per ottenere una sola riga selezionata)
                tblDb.setRowSelectionInterval(i, i );
                found=true;
            }
        }
        return found;
    }

    public static boolean selectTabelRow(String indexCategoria, String indexName){
        boolean found = false;
        for (int i=0; i<tblDb.getModel().getRowCount(); i++){
            if(tblDb.getModel().getValueAt(i,2).equals(indexName)&&tblDb.getModel().getValueAt(i,1).equals(indexCategoria)&&!found){
                tblDb.setRowSelectionInterval(i, i );
                found=true;
            }
        }
        return found;
    }

    public static String getSelectedTable() {
        return selectedTable;
    }
}
