import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditFrame {
    public EditFrame(AdminMode adminMode, JTable tblDb, ArrayList listaCategorie) {
        JDialog dlgEdit = new JDialog(adminMode, "Modifica prodotto?", true);

        //impostazione del layout della finestra di dialogo
        dlgEdit.setLayout(new FlowLayout());
        dlgEdit.setLocationRelativeTo(adminMode);
        dlgEdit.setSize(680,180);
        dlgEdit.setResizable(false);



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


        for(int i=0; i<model.getSize(); i++){
            boolean found=false;
            if(cmbCategoria.getModel().getElementAt(i).equals(tblDb.getModel().getValueAt(tblDb.getSelectedRow(),1).toString())&&!found){
                cmbCategoria.setSelectedIndex(i);
                found=true;
            }
        }


        JTextField txtNome = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 2).toString(), 17);
        JTextField txtQuantita = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 3).toString(),17);
        JTextField txtNumScaffale = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 4).toString(),17);
        JTextField txtCosto = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 5).toString(),17);
        JTextField txtCostoTot = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 6).toString(),17);
        JTextField txtData = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 9).toString(),17);
        JTextField txtAliquota = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 7).toString(),17);



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

        dlgEdit.getContentPane().add(pnl);

        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection dbConnection = MainMagazzino.dbConnect();
                try {
                    PreparedStatement statement = dbConnection.prepareStatement("UPDATE " + AdminMode.getSelectedTable() + " SET Categoria = ?, Nome = ?, Quantita = ?, NumScaffale = ?, Costo = ?, CostoTotale = ?, Aliquota = ?, DataAcquisto = ? WHERE CodiceProdotto = ?");
                    statement.setString(1, cmbCategoria.getSelectedItem().toString());
                    statement.setString(2, txtNome.getText());
                    statement.setInt(3, Integer.parseInt(txtQuantita.getText()));
                    statement.setString(4, txtNumScaffale.getText());
                    statement.setInt(5, Integer.parseInt(txtCosto.getText()));
                    statement.setInt(6, Integer.parseInt(txtCostoTot.getText()));
                    statement.setInt(7, Integer.parseInt(txtAliquota.getText()));
                    statement.setString(8, txtData.getText());
                    statement.setInt(9, Integer.parseInt(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 0).toString()));

                    statement.executeUpdate();
                    dbConnection.close();
                    //richiamo il metodo del main apposito ad aggiornare la tabella delle modifiche
                    MainMagazzino.updateModifiche("Modificato " + txtNome.getText() + ",quantita " + txtQuantita.getText());

                    AdminMode.reloadTable();

                    dlgEdit.dispose();
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Errore: impossibile aggiungere il prodotto. Verifico che non sia già presente in questa o altre categorie...");
                    try {
                        if(!AdminMode.selectTableRow(txtNome.getText())){
                            JOptionPane.showMessageDialog(adminMode, "Nessun prodotto corrispondente trovato. Verificare che il database non sia già in uso da altri processi e riprovare.");
                        }
                        dlgEdit.dispose();
                        dbConnection.close();
                    } catch (SQLException e2) {
                        e2.printStackTrace();
                    }
                }

            }
        });



        dlgEdit.setVisible(true);
    }
}
