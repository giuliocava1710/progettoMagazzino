import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveFrame {

    public RemoveFrame(AdminMode adminMode, JTable tblDb) {
        JDialog dlgRemove = new JDialog(adminMode, "Rimuovere prodotto?", true);

        //impostazione del layout della finestra di dialogo
        dlgRemove.setLayout(new FlowLayout());
        dlgRemove.setLocationRelativeTo(adminMode);
        dlgRemove.setSize(1600,130);
        dlgRemove.setResizable(false);



        //creazione e aggiunta elementi grafici
        JButton btnConfirm = new JButton("Conferma");

        JComboBox cmbCategoria = new JComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 1).toString());
        cmbCategoria.setModel(model);
        cmbCategoria.setSelectedIndex(0);
        cmbCategoria.setEnabled(false);

        JTextField txtNome = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 2).toString(), 23);
        JTextField txtQuantita = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 3).toString(),5);
        JTextField txtNumScaffale = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 4).toString(),10);
        JTextField txtCosto = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 5).toString(),5);
        JTextField txtCostoTot = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 6).toString(),5);
        JTextField txtAliquota = new JTextField(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 7).toString(), 5);


        txtNome.setEnabled(false);
        txtQuantita.setEnabled(false);
        txtNumScaffale.setEnabled(false);
        txtCosto.setEnabled(false);
        txtCostoTot.setEnabled(false);
        txtAliquota.setEnabled(false);

        JPanel pnl = new JPanel(new GridLayout(2,7));
        JLabel lblCategoria = new JLabel("Categoria");
        JLabel lblNome = new JLabel("Nome");
        JLabel lblQuantita = new JLabel("Quantità");
        JLabel lblScaffale = new JLabel("Scaffale");
        JLabel lblCosto = new JLabel("Costo singolo prodotto");
        JLabel lblCostoTot = new JLabel("Costo totale");
        JLabel lblAliquota = new JLabel("Aliquota");
        JLabel lblSpazio = new JLabel("");

        pnl.add(lblCategoria);
        pnl.add(lblNome);
        pnl.add(lblQuantita);
        pnl.add(lblScaffale);
        pnl.add(lblCosto);
        pnl.add(lblCostoTot);
        pnl.add(lblAliquota);
        pnl.add(lblSpazio);

        pnl.add(cmbCategoria);
        pnl.add(txtNome);
        pnl.add(txtQuantita);
        pnl.add(txtNumScaffale);
        pnl.add(txtCosto);
        pnl.add(txtCostoTot);
        pnl.add(txtAliquota);
        pnl.add(btnConfirm);
        dlgRemove.getContentPane().add(pnl);


        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection dbConnection = MainMagazzino.dbConnect();
                try {
                    PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM " + AdminMode.getSelectedTable() +" WHERE CodiceProdotto = ?" );
                    statement.setInt(1, Integer.parseInt(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 0).toString()));
                    statement.executeUpdate();

                    //richiamo il metodo del main apposito ad aggiornare la tabella delle modifiche
                    MainMagazzino.updateModifiche("Rimosso totalmente " + txtNome.getText());

                    adminMode.reloadTable();
                    dlgRemove.dispose();
                    dbConnection.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });


        dlgRemove.setVisible(true);


    }
}
