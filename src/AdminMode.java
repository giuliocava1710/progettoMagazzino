import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminMode extends JFrame {

    /*
        PROPRIETÀ
        Inserisco come proprietà della classe la comboBox delle categorie, la finestra stessa e le due tabelle (prodotti e richieste)
        Ciò permette di gestire le proprietà tra diversi metodi di questa classe e altre classi
     */

    private JComboBox cmbCategoria = new JComboBox();
    private AdminMode adminMode= this;
    private ArrayList<String> listaCategorie = new ArrayList<>();
    private static String selectedTable = null;

    //disabilito la possibilità di selezionare una singola cella tramite un override del metodo apposito in entrambe le tabelle
    private static JTable tblDb = new JTable(){
        public boolean isCellEditable(int row, int col){
            return false;
        }
    };


    private static JTable tblRichieste = new JTable(){
        public boolean isCellEditable(int row, int col){
            return false;
        }
    };

    public AdminMode() {


        /*
            IMPOSTAZIONE GRAFICA FINESTRA
         */

        selectedTable="GEN";
        //imposto l' impossibilità di selezionare più di una riga per volta nella tabella
        tblDb.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblRichieste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);



        setSize(1000,750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Magazzino - Amministratore");
        setIconImage(new ImageIcon("C:/Users/Lorenzo/Desktop/ProgettoMagazzinoScolastico/icons/icon.png").getImage());
        //imposto la grafica in modo che segua lo stile del sistema operativo su cui il programma viene eseguito
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null,"è stata generata un'eccezione");
            System.exit(0);
        }
        setResizable(false);

        //richiamo il metodo di inizializzazione dell' interfaccia passando le due tabelle, in modo da poterle usare nel metodo senza
        //renderle proprietà pubbliche
        initUI(tblDb, tblRichieste);


        setVisible(true);





    }



    /*
     * metodo initUI ideato per frammentare il codice, viene lanciato dal costruttore della classe interfacciaGrafica
     * con la funzione di disegnare i vari componenti tra cui bottoni e tabelle
     * */
    private void initUI(JTable tblDb, JTable tblRichieste) {

        /*
            GESTIONE DELLA GRAFICA E DELLE FUNZIONI DELL' INTERFACCIA
            Il metodo imposta tutti i settaggi grafici e aggiunge gli elementi necessari
            Gestisce inoltre i click sui vari bottoni e le operazioni sulle tabelle
         */



        /*
            PANNELLO OVEST DELL' INTERFACCIA
            Il pannello Ovest del Borderlayout contiene il necessario a modificare il database
         */
        JPanel pnlWest = new JPanel(new GridLayout(2, 1));
        JPanel pnlOperazioni=new JPanel(new GridLayout(3,1));
        JPanel pnlAdd =new JPanel();
        BottoniOperazioni btnAggiungi=new BottoniOperazioni("AGGIUNGI NUOVO");
        btnAggiungi.setEnabled(false);

        /*
            GESTIONE EVENTI BOTTONI
            I metodi listener dei bottoni si limitano a richiamare le classi apposite di gestione degli eventi (aggiunta, rimozione e modifica) passando i parametri necessari
            Al termine, viene ricaricata con un metodo apposito la comboBox che contiene le categorie ideata per la funzione di ricerca, in modo da compensare eventuali modifiche alle categorie presenti
            I bottoni relativi alla rimozione e alla modifica sono disabilitati fino a quando non viene selezionato un elemento della tabella dei prodotti
         */
        btnAggiungi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNewFrame addNewFrame = new AddNewFrame(adminMode, listaCategorie);
                reloadComboCategorie(cmbCategoria);
            }
        });
        pnlAdd.add(btnAggiungi);
        pnlAdd.setBackground(Color.WHITE);
        /*---------------------------------------*/
        JPanel pnlMod=new JPanel();
        BottoniOperazioni btnModifica=new BottoniOperazioni("MODIFICA");
        btnModifica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditFrame editFrame = new EditFrame(adminMode, tblDb, listaCategorie);
                reloadComboCategorie(cmbCategoria);
            }
        });
        btnModifica.setEnabled(false);

        pnlMod.add(btnModifica);
        pnlMod.setBackground(Color.WHITE);
        /*------------------------------------*/
        JPanel pnlRim=new JPanel();
        BottoniOperazioni btnRimuovi=new BottoniOperazioni("RIMUOVI PRODOTTO");
        btnRimuovi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RemoveFrame removeFrame = new RemoveFrame(adminMode, tblDb);
                reloadComboCategorie(cmbCategoria);
            }
        });
        btnRimuovi.setEnabled(false);
        pnlRim.add(btnRimuovi);
        pnlRim.setBackground(Color.WHITE);


        pnlOperazioni.add(pnlAdd);
        pnlOperazioni.add(pnlMod);
        pnlOperazioni.add(pnlRim);
        pnlWest.add(pnlOperazioni);

        /*
            PANNELLO GESTIONE RICHIESTE
            Grafica che permette di confermare e eliminare le richieste dalla tabella richieste
            tramite due bottoni
            La struttura dei pannelli comprende due label vuote per creare spazio in alto
         */

        BottoniOperazioni btnEliminaRichiesta = new BottoniOperazioni("ELIMINA RICHIESTA");

        JPanel pnlAzioniRichieste = new JPanel(new GridLayout(4,1));
        pnlAzioniRichieste.setBackground(Color.WHITE);


        JPanel pnlEliminaRichiesta = new JPanel();
        pnlEliminaRichiesta.add(btnEliminaRichiesta);
        pnlEliminaRichiesta.setBackground(Color.WHITE);

        pnlAzioniRichieste.add(new JLabel(""));
        pnlAzioniRichieste.add(new JLabel(""));
        pnlAzioniRichieste.add(pnlEliminaRichiesta);
        pnlWest.add(pnlAzioniRichieste);

        btnEliminaRichiesta.setEnabled(false);



        btnEliminaRichiesta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminaRichiesta();
            }
        });

        //imposto un listener sulla tabella grazie al quale alla selezione di una riga vengono abilitati i bottoni relativi alla rimozione e alla conferma di una richiesta, disabilitati di default
        tblRichieste.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //ad ogni selezione/deselezione imposto i bottoni come attivati/disattivati basandomi sul risultato dell' operazione
                //di casting della tabella (chiamata e nel metodo in quanto fonte dell' evento) in un ListSelectionModel, dal quale poi controllo se ci siano righe selezionate
                btnEliminaRichiesta.setEnabled(!((ListSelectionModel)e.getSource()).isSelectionEmpty());
            }
        });


        this.add(pnlWest,BorderLayout.EAST);


        /*
            PANNELLO OVEST DELL' INTERFACCIA
            Il pannello est è occupato dai bottoni relativi agli indirizzi
         */

        /*
         * creo l' istanza dei bottoni degli indirizzi che verranno aggiungi a un pannello
         * divisore che avrà la funzione di distanziare i vari componenti dal fondo della pagina.
         * Successivamente verranno aggiungi al pannello principale indirizzi che verrà posizionato
         * a est nell'interfaccia.
         * */

        //!!!MANCA LA GESTIONE DEGLI INIDIRIZZI!!!
        JPanel pnlIndirizzi = new JPanel(new GridLayout(2,1));
        JPanel pnlDivisore = new JPanel(new GridLayout(6,1));

        /*Bottoni degli indirizzi, con il bottone generale già selezionato*/
        BottoniIndirizzi btnTutto = new BottoniIndirizzi("GENERALE");
        BottoniIndirizzi.setBtn(btnTutto);
        btnTutto.setForeground(Color.BLACK);
        btnTutto.setFont(new Font("Arial", Font.PLAIN, 12));

        BottoniIndirizzi btnInf = new BottoniIndirizzi("INF");
        BottoniIndirizzi btnChi = new BottoniIndirizzi("CHI");
        BottoniIndirizzi btnMec = new BottoniIndirizzi("MEC");
        BottoniIndirizzi btnEle = new BottoniIndirizzi("ELE");
        BottoniIndirizzi btnRob = new BottoniIndirizzi("ROB");

        pnlDivisore.add(btnTutto);
        pnlDivisore.add(btnInf);
        pnlDivisore.add(btnChi);
        pnlDivisore.add(btnMec);
        pnlDivisore.add(btnEle);
        pnlDivisore.add(btnRob);
        pnlIndirizzi.add(pnlDivisore);


        JPanel pnlGrafico = new JPanel(new GridLayout(6,1));
        BottoniIndirizzi btnGrafico = new BottoniIndirizzi("GRAFICO");

        pnlGrafico.add(btnGrafico);
        pnlGrafico.setBackground(Color.WHITE);
        pnlIndirizzi.add(pnlGrafico);

        this.add(pnlIndirizzi,BorderLayout.WEST);

        /*
            PANNELLO CENTRALE DELL' INTERFACCIA
            Il pannello centrale contiene la tabella dei prodotti e la tabella delle richieste con relativo JScrollPane,
            oltre che il necessario alla funzione di ricerca
         */
        /*
         * Inizializzo una nuova tabella alla quale setto il modello di default e definisco quelle che
         * sono le sue colonne, tramite un ciclo for aggiungo le colonne al modello.
         * Al JScrollPane passo la tabella con il modello settato.
         * Dichiaro un pannello centrale che conterrà la tabella magazzino insieme alla tabella richieste
         * con i bottoni delle operazioni e la funzione di ricerca del prodotto
         * */

        //richiamo il metodo reloadTable, che imposta il modello della tabella basandosi sul valore di ritorno di un apposito metodo nella classe MainMagazzino
        reloadTable();

        JScrollPane scrollTabellaMagazzino=new JScrollPane(tblDb);
        //imposto un listener sulla tabella grazie al quale alla selezione di una riga vengono abilitati i bottoni relativi alla rimozione e alla modifica di un prodotto, disabilitati di default
        tblDb.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //ad ogni selezione/deselezione imposto i bottoni come attivati/disattivati basandomi sul risultato dell' operazione
                //di casting della tabella (chiamata e nel metodo in quanto fonte dell' evento) in un ListSelectionModel, dal quale poi controllo se ci siano righe selezionate
                if(!selectedTable.equals("GEN")) {
                    btnModifica.setEnabled(!((ListSelectionModel) e.getSource()).isSelectionEmpty());
                    btnRimuovi.setEnabled(!((ListSelectionModel) e.getSource()).isSelectionEmpty());
                }
            }
        });
        JPanel pnlCentrale = new JPanel(new GridLayout(2, 1));
        JPanel pnlPrinc=new JPanel(new GridLayout(1,2));
        pnlPrinc.add(scrollTabellaMagazzino);
        JPanel pnlTabella=new JPanel(new GridLayout(2,1));


        /*
         * Creo: una JComboBox che conterrà la categoria del prodotto, un'area di testo nella quale verrà specificato il prodotto
         * da ricercare e l'istanza di un nuovo bottone che farà partire la ricerca
         * sul prodotto selezionato nella comboBox.
         * Infine aggiungo i tre componenti al pannello Ricerca che verrà a sua volta aggiunto al pannello centrale
         * */
        JPanel pnlRicerca= new JPanel(new FlowLayout(FlowLayout.LEFT));
        reloadComboCategorie(cmbCategoria);
        JTextField txtDescrizione=new JTextField(40);
        BottoniOperazioni btnRicerca=new BottoniOperazioni("CERCA");
        btnRicerca.setEnabled(false);

        /*
            -Aggiungo un document listener che verifichi che il campo di ricerca non sia vuoto. Se lo è setto il tasto di ricerca come disabilitato
            -Aggiungo un action listener per gestire la ricerca
         */
        txtDescrizione.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                btnRicerca.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(txtDescrizione.getText().equals("")){
                    btnRicerca.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) { }
        });
        btnRicerca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtDescrizione.getText()!=null) {
                    ricerca(txtDescrizione.getText());
                }
            }
        });
        pnlRicerca.add(cmbCategoria);
        pnlRicerca.add(txtDescrizione);
        pnlRicerca.add(btnRicerca);
        pnlTabella.add(pnlRicerca);
        pnlRicerca.setBackground(Color.WHITE);

        pnlCentrale.add(pnlPrinc);

        /*
            TABELLA RICHIESTE
            Carico la tabella delle richieste con l'apposito metodo
            Aggiungo la tabella ad uno scroll pane
            Imposto la tabella nel pannello centrale del BorderLayout
         */

        tblRichieste.setModel(loadTableRichieste());
        JScrollPane scrollRichieste=new JScrollPane(tblRichieste);
        pnlTabella.add(scrollRichieste);
        pnlCentrale.add(pnlTabella);

        this.add(pnlCentrale,BorderLayout.CENTER);



        /*
            BOTTONI EST
            Setto i necessari actionlistener sui bottoni, in modo che quelli degli indirizzi impostino
            un' interfaccia e quello del grafico un' altra
         */

        btnGrafico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //rimuovo tutti i panel dall' interfaccia
                adminMode.getContentPane().removeAll();
                //aggiungo nuovamente il panel degli indirizzi
                adminMode.getContentPane().add(pnlIndirizzi, BorderLayout.WEST);
                //creo il GraficoTabella(estende JPanel) che contiene grafico e relativa tabella
                GraficoTabella pnlGraficoTabella = new GraficoTabella();
                //aggiungo GraficoTabella al pannello centrale
                adminMode.getContentPane().add(pnlGraficoTabella,BorderLayout.CENTER);


                //rendo effettive le modifiche grafiche
                adminMode.getContentPane().validate();
                adminMode.getContentPane().repaint();


            }
        });

        btnTutto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //rimuovo tutti i panel dall' interfaccia
                adminMode.getContentPane().removeAll();
                //aggiungo nuovamente il panel degli indirizzi a
                adminMode.getContentPane().add(pnlIndirizzi, BorderLayout.WEST);
                //aggiungo il pannello centrale e quello est dell' interfaccia, già istanziati durante il primo avvio della schermata
                adminMode.getContentPane().add(pnlCentrale, BorderLayout.CENTER);
                adminMode.getContentPane().add(pnlWest, BorderLayout.EAST);
                //rendo effettive le modifiche grafiche
                adminMode.getContentPane().validate();
                adminMode.getContentPane().repaint();
                btnAggiungi.setEnabled(false);
                btnModifica.setEnabled(false);
                btnRimuovi.setEnabled(false);
                selectedTable="GEN";
                reloadTable();
            }
        });

        btnInf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //rimuovo tutti i panel dall' interfaccia
                adminMode.getContentPane().removeAll();
                //aggiungo nuovamente il panel degli indirizzi a
                adminMode.getContentPane().add(pnlIndirizzi, BorderLayout.WEST);
                //aggiungo il pannello centrale e quello est dell' interfaccia, già istanziati durante il primo avvio della schermata
                adminMode.getContentPane().add(pnlCentrale, BorderLayout.CENTER);
                adminMode.getContentPane().add(pnlWest, BorderLayout.EAST);
                //rendo effettive le modifiche grafiche
                adminMode.getContentPane().validate();
                adminMode.getContentPane().repaint();
                btnAggiungi.setEnabled(true);
                btnModifica.setEnabled(false);
                btnRimuovi.setEnabled(false);
                selectedTable="INF";
                reloadTable();
            }
        });

        btnChi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //rimuovo tutti i panel dall' interfaccia
                adminMode.getContentPane().removeAll();
                //aggiungo nuovamente il panel degli indirizzi a
                adminMode.getContentPane().add(pnlIndirizzi, BorderLayout.WEST);
                //aggiungo il pannello centrale e quello est dell' interfaccia, già istanziati durante il primo avvio della schermata
                adminMode.getContentPane().add(pnlCentrale, BorderLayout.CENTER);
                adminMode.getContentPane().add(pnlWest, BorderLayout.EAST);
                //rendo effettive le modifiche grafiche
                adminMode.getContentPane().validate();
                adminMode.getContentPane().repaint();
                btnAggiungi.setEnabled(true);
                btnModifica.setEnabled(false);
                btnRimuovi.setEnabled(false);
                selectedTable="CHI";
                reloadTable();
            }
        });


        btnEle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //rimuovo tutti i panel dall' interfaccia
                adminMode.getContentPane().removeAll();
                //aggiungo nuovamente il panel degli indirizzi a
                adminMode.getContentPane().add(pnlIndirizzi, BorderLayout.WEST);
                //aggiungo il pannello centrale e quello est dell' interfaccia, già istanziati durante il primo avvio della schermata
                adminMode.getContentPane().add(pnlCentrale, BorderLayout.CENTER);
                adminMode.getContentPane().add(pnlWest, BorderLayout.EAST);
                //rendo effettive le modifiche grafiche
                adminMode.getContentPane().validate();
                adminMode.getContentPane().repaint();
                btnAggiungi.setEnabled(true);
                btnModifica.setEnabled(false);
                btnRimuovi.setEnabled(false);
                selectedTable="ELE";
                reloadTable();
            }
        });


        btnMec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //rimuovo tutti i panel dall' interfaccia
                adminMode.getContentPane().removeAll();
                //aggiungo nuovamente il panel degli indirizzi a
                adminMode.getContentPane().add(pnlIndirizzi, BorderLayout.WEST);
                //aggiungo il pannello centrale e quello est dell' interfaccia, già istanziati durante il primo avvio della schermata
                adminMode.getContentPane().add(pnlCentrale, BorderLayout.CENTER);
                adminMode.getContentPane().add(pnlWest, BorderLayout.EAST);
                //rendo effettive le modifiche grafiche
                adminMode.getContentPane().validate();
                adminMode.getContentPane().repaint();
                btnAggiungi.setEnabled(true);
                btnModifica.setEnabled(false);
                btnRimuovi.setEnabled(false);
                selectedTable="MEC";
                reloadTable();
            }
        });

        btnRob.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //rimuovo tutti i panel dall' interfaccia
                adminMode.getContentPane().removeAll();
                //aggiungo nuovamente il panel degli indirizzi a
                adminMode.getContentPane().add(pnlIndirizzi, BorderLayout.WEST);
                //aggiungo il pannello centrale e quello est dell' interfaccia, già istanziati durante il primo avvio della schermata
                adminMode.getContentPane().add(pnlCentrale, BorderLayout.CENTER);
                adminMode.getContentPane().add(pnlWest, BorderLayout.EAST);
                //rendo effettive le modifiche grafiche
                adminMode.getContentPane().validate();
                adminMode.getContentPane().repaint();
                btnAggiungi.setEnabled(true);
                btnModifica.setEnabled(false);
                btnRimuovi.setEnabled(false);
                selectedTable="ROB";
                reloadTable();
            }
        });

    }

    private void eliminaRichiesta() {
        Connection dbConnection = MainMagazzino.dbConnect();
        try {
            PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM Richieste WHERE IdRichiesta = ?");
            statement.setString(1, tblRichieste.getModel().getValueAt(tblRichieste.getSelectedRow(), 0).toString());
            statement.executeUpdate();
            tblRichieste.setModel(loadTableRichieste());
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private DefaultTableModel loadTableRichieste() {
        Object[] headers = {"ID Richiesta", "Categoria", "Nome/Descrizione", "Quantità", "Data e ora", "Messaggio"};
        DefaultTableModel tblModel = new DefaultTableModel(headers, 0);
        try {
            Connection dbConnection = MainMagazzino.dbConnect();
            ResultSet dbReturn = dbConnection.createStatement().executeQuery("SELECT * FROM Richieste");
            while(dbReturn.next()){
                String prodotto = dbReturn.getString("Prodotto");
                String categoria = prodotto.substring(0, prodotto.indexOf(";"));
                String nome = prodotto.substring(prodotto.indexOf(";")+1);
                Object[] singleDbRow = {
                        String.valueOf(dbReturn.getInt("IdRichiesta")),
                        categoria,
                        nome,
                        String.valueOf(dbReturn.getInt("Quantita")),
                        dbReturn.getString("Data"),
                        dbReturn.getString("Messaggio"),
                };
                tblModel.addRow(singleDbRow);
            }
            dbConnection.close();
            return tblModel;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void ricerca(String indexNome) {
        //se nella combo è stato selezionato "Tutte le categorie" richiamo un metodo apposito che ignora la ricerca per categoria e controllo il suo valore di ritorno per sapere se ci sono risultati
        if(cmbCategoria.getSelectedItem().toString().equals("Tutte le categorie")){
            if(!selectTableRow(indexNome)){
                JOptionPane.showMessageDialog(null, "Nessun prodotto corrispondente trovato.");
            }
        }else {
            //nella if richiamo il metodo che seleziona il prodotto cercato e che resitituisce falso se non è stato trovato nulla
            if (!selectTabelRow(cmbCategoria.getSelectedItem().toString(), indexNome)) {
                JOptionPane.showMessageDialog(null, "Nessun prodotto corrispondente trovato.");
            }
        }
    }


    /*
        GESTIONE COMBOBOX CATEGORIE
        Il metodo, usando un ComboBoxModel, ricarica di volta in volta la lista di categorie ottenuta grazie all' apposito metodo nella classe MainMagazzino
     */
    private void reloadComboCategorie(JComboBox cmbCategoria) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(MainMagazzino.getDbCategories(listaCategorie).toArray());

        cmbCategoria.setModel(model);
        model.addElement("Tutte le categorie");
        cmbCategoria.setSelectedIndex(0);
    }

    /*
        GESTIONE TABELLA PRODOTTI
        Il metodo richiama l' apposito metodo nella classe MainMagazzino per ricaricare la tabella dei prodotti quando necessario
     */
    public static void reloadTable() {
        tblDb.setModel(MainMagazzino.tableBuilder(selectedTable));
    }


    /*
        SELEZIONE NELLA TABELLA
        Il metodo seleziona una riga della tabella basandosi sul parametro ricevuto. Viene richiamato dalle classi di aggiunta e modifica
        nel caso in cui un prodotto con lo stesso nome sia già stato trovato nel database e scorre tutta la tabella finchè non viene
        trovato al suo interno un prodotto avente come codiceProdotto l' indice ricevuto.
        Il metodo è overridato in modo da supportare la ricerca per indice del prodotto, per nome e per nome e categoria
     */

    //metodo mai usato, sostituito dai due sotto per ottimizzazione
    public static boolean selectTableRow(int index){
        //ricevuto un indice, scorro tutta la tabella finchè non trovo un prodotto con lo stesso CodiceProdotto
        boolean found=false;
        for(int i=0; i<tblDb.getModel().getRowCount(); i++) {
            if((tblDb.getModel().getValueAt(i, 0).equals(String.valueOf(index)))&&!found){
                //quando trovo il prodotto lo seleziono(i come intervallo minimo e massimo di selezione per ottenere una sola riga selezionata)
                tblDb.setRowSelectionInterval(i, i );
                found=true;
            }
        }
        return found;
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
        //ricevuti un nome e una categoria, scorro tutta la tabella finchè non trovo un prodotto con uguale nome e categoria
        boolean found = false;
        for (int i=0; i<tblDb.getModel().getRowCount(); i++){
            //la if da precedenza al nome in quanto indice univoco, quindi se il nome fosse diverso per cortocircuito degli operatori
            //il programma non guarda la categoria. Al conrtario, essendo la categoria duplicabile, il cortocircuito non avrebbe funzionato sempre
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
