import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class RequestFrame {
    UserMode userMode = null;
    JTable tblDb = null;

    public RequestFrame(JTable tblDb, UserMode usermode) {
        this.tblDb=tblDb;
        this.userMode = usermode;
        initUI();
    }

    private void initUI() {
        JDialog dlgRequest = new JDialog(userMode, "Richiedere il prodotto?", true);
        dlgRequest.setSize(600,250);
        dlgRequest.setLocationRelativeTo(null);
        dlgRequest.setResizable(false);
        dlgRequest.setLayout(new GridLayout());


        JPanel pnlProdotto = new JPanel(new FlowLayout());
        JComboBox cmbCategoria = new JComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 1).toString());
        cmbCategoria.setModel(model);
        cmbCategoria.setSelectedIndex(0);
        cmbCategoria.setEnabled(false);
        JTextField txtNome =  new JTextField((tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 2)).toString(), 23);
        txtNome.setEditable(false);
        JTextField txtQuantita = new JTextField((tblDb.getModel().getValueAt(tblDb.getSelectedRow(), 3)).toString(), 5);

        JButton btnConferma = new JButton("Conferma");
        pnlProdotto.add(cmbCategoria);
        pnlProdotto.add(txtNome);
        pnlProdotto.add(txtQuantita);
        pnlProdotto.add(btnConferma);

        dlgRequest.add(pnlProdotto);

        JTextArea txtMessaggio = new JTextArea();
        txtMessaggio.setSize(190,190);
        JScrollPane scrollPaneMessaggio = new JScrollPane(txtMessaggio);
        txtMessaggio.setBackground(new Color(220, 220, 220));
        txtMessaggio.setLineWrap(true);

        dlgRequest.add(scrollPaneMessaggio);


        btnConferma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = txtMessaggio.getText();
                input = input.replace("\n", " ");
                sendRequest(cmbCategoria.getSelectedItem().toString(), txtNome.getText(), Integer.parseInt(txtQuantita.getText()), input);
                JOptionPane.showMessageDialog(null, "Richiesta inviata.");
                dlgRequest.dispose();
            }
        });

        dlgRequest.setVisible(true);
    }

    private void sendRequest(String categoria, String nome, int quantita, String messaggio) {
        String prodotto = categoria + ";" + nome;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataOra = sdf.format(new Timestamp(System.currentTimeMillis()));
        Connection dbConnection = MainMagazzino.dbConnect();
        try {
            PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO Richieste(Prodotto ,Quantita, Data, Messaggio, Conferma) VALUES(?,?,?,?,?)");
            statement.setString(1, prodotto);
            statement.setInt(2, quantita);
            statement.setString(3, dataOra);
            statement.setString(4, messaggio);
            statement.setInt(5,0);
            statement.executeUpdate();
            dbConnection.close();



        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


}
