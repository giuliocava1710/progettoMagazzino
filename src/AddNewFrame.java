import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AddNewFrame {
    public AddNewFrame(JFrame adminMode, ArrayList<String> listaCategorie) {
        /*
            CREAZIONE FINESTRA DI DIALOGO PER L'AGGIUNTA
          */

        //creazione fineestra di dialogo passando come parametri il frame che la genera, il titolo e il booleano che indica che debba essere modale
        final JDialog dlgNewItem = new JDialog(adminMode, "Aggiungi nuovo", true);

        /*
            CREAZIONE ELEMENTI INTERFACCIA
        */

        //impostazione del layout della finestra di dialogo
        dlgNewItem.setLayout(new FlowLayout());
        dlgNewItem.setLocationRelativeTo(null);
        dlgNewItem.setSize(680, 180);
        dlgNewItem.setResizable(false);

        //creazione e aggiunta elementi grafici
        BottoniOperazioni btnConfirm = new BottoniOperazioni("Conferma");

        JComboBox cmbCategoria = new JComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel(MainMagazzino.getDbCategories(listaCategorie).toArray());
        model.addElement("Nuova categoria...");
        cmbCategoria.setModel(model);
        cmbCategoria.setSelectedIndex(0);

        if(cmbCategoria.getItemCount()==1){
            cmbCategoria.setEditable(true);
        }

        JTextField txtNome = new JTextField(17);
        JTextField txtQuantita = new JTextField(17);
        JTextField txtNumScaffale = new JTextField(17);
        JTextField txtCosto = new JTextField(17);
        JTextField txtCostoTot = new JTextField(17);
        JTextField txtData = new JTextField(17);
        JTextField txtAliquota = new JTextField(17);



        /*NON FUNZIONANTE-AGGIORNAMENTO RECIPROCO DELLE COMBO DEL PREZZO
        txtCosto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Runnable doChange = new Runnable() {
                    @Override
                    public void run() {
                        txtCosto.setText(String.valueOf(0));
                    }
                };
                SwingUtilities.invokeLater(doChange);

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Runnable doChange = new Runnable() {
                    @Override
                    public void run() {
                        //converto in intero il valore di txtCosto e txtQuantita, li moltiplico e converto il risultato a stringa. imposto il risutlato come testo di txtCostoTot
                        txtCostoTot.setText(String.valueOf(Integer.parseInt(String.valueOf(txtCosto.getText()))*Integer.parseInt(String.valueOf(txtQuantita.getText()))));
                    }
                };
                SwingUtilities.invokeLater(doChange);

            }
        });

        txtCostoTot.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Runnable doChange = new Runnable() {
                    @Override
                    public void run() {
                        txtCostoTot.setText(String.valueOf(0));
                    }
                };
                SwingUtilities.invokeLater(doChange);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Runnable doChange = new Runnable() {
                    @Override
                    public void run() {
                        //converto in intero il valore di txtCosto e txtQuantita, li moltiplico e converto il risultato a stringa. imposto il risutlato come testo di txtCostoTot
                        txtCosto.setText(String.valueOf(Integer.parseInt(String.valueOf(txtCostoTot.getText()))/Integer.parseInt(String.valueOf(txtQuantita.getText()))));
                    }
                };
                SwingUtilities.invokeLater(doChange);

            }
        });

        */



        JPanel pnl = new JPanel(new GridLayout(6, 4));
        JLabel lblCategoria = new JLabel("Categoria");
        JLabel lblNome = new JLabel("Nome");
        JLabel lblQuantita = new JLabel("Quantità");
        JLabel lblScaffale = new JLabel("Scaffale");
        JLabel lblCosto = new JLabel("Costo singolo prodotto");
        JLabel lblCostoTot = new JLabel("Costo totale");
        JLabel lblData = new JLabel("Data acquisto");
        JLabel lblAliquota = new JLabel("Aliquota");
        JLabel lblSpazio1 = new JLabel("");
        JLabel lblSpazio2 = new JLabel("");
        JLabel lblSpazio3 = new JLabel("");

        JPanel pnlLblCategoria = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pnlLblNome = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pnlLblQuantità = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pnlLblScaffale = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pnlLblCosto = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pnlLblCostoTot = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pnlLblData = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pnlLblAliquota = new JPanel(new FlowLayout(FlowLayout.CENTER));

        pnlLblCategoria.add(lblCategoria);
        pnlLblNome.add(lblNome);
        pnlLblQuantità.add(lblQuantita);
        pnlLblScaffale.add(lblScaffale);
        pnlLblCosto.add(lblCosto);
        pnlLblCostoTot.add(lblCostoTot);
        pnlLblData.add(lblData);
        pnlLblAliquota.add(lblAliquota);


        pnl.add(pnlLblCategoria);
        pnl.add(cmbCategoria);

        pnl.add(pnlLblNome);
        pnl.add(txtNome);

        pnl.add(pnlLblQuantità);
        pnl.add(txtQuantita);

        pnl.add(pnlLblScaffale);
        pnl.add(txtNumScaffale);

        pnl.add(pnlLblCosto);
        pnl.add(txtCosto);

        pnl.add(pnlLblCostoTot);
        pnl.add(txtCostoTot);

        pnl.add(pnlLblData);
        pnl.add(txtData);

        pnl.add(pnlLblAliquota);
        pnl.add(txtAliquota);

        pnl.add(lblSpazio1);
        pnl.add(lblSpazio2);
        pnl.add(lblSpazio3);
        pnl.add(btnConfirm);

        dlgNewItem.getContentPane().add(pnl);

        cmbCategoria.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if(cmbCategoria.getSelectedItem().toString().equals("Nuova categoria...")){
                    cmbCategoria.setEditable(true);
                }else{
                    cmbCategoria.setEditable(false);
                }
            }
        });



        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection dbConnection = MainMagazzino.dbConnect();
                try {
                    //aggiornamento della tabella dei prodotti
                    PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO " + AdminMode.getSelectedTable() + "(Categoria,Nome,Quantita, NumScaffale, Costo, CostoTotale, DataAcquisto) VALUES(?,?,?,?,?,?,?)");
                    statement.setString(1, cmbCategoria.getSelectedItem().toString());
                    statement.setString(2, txtNome.getText());
                    statement.setInt(3, Integer.parseInt(txtQuantita.getText()));
                    statement.setString(4, txtNumScaffale.getText());
                    statement.setInt(5, Integer.parseInt(txtCosto.getText()));
                    statement.setInt(6, Integer.parseInt(txtCostoTot.getText()));
                    statement.setInt(7, Integer.parseInt(txtData.getText()));

                    statement.executeUpdate();
                    dbConnection.close();
                    //richiamo il metodo del main apposito ad aggiornare la tabella delle modifiche
                    MainMagazzino.updateModifiche("Aggiunto " + txtNome.getText() + ",quantita " + txtQuantita.getText());

                    AdminMode.reloadTable();
                    dlgNewItem.dispose();

                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Errore: impossibile aggiungere il prodotto. Verifico che non sia già presente in questa o altre categorie...");
                    try {
                        if(!AdminMode.selectTableRow(txtNome.getText())){
                            JOptionPane.showMessageDialog(adminMode, "Nessun prodotto corrispondente trovato. Verificare che il database non sia già in uso da altri processi e riprovare.");
                        }
                        dlgNewItem.dispose();
                        dbConnection.close();
                    } catch (SQLException e2) {
                        e2.printStackTrace();
                    }

                }

            }
        });

        dlgNewItem.setVisible(true);
    }
}
