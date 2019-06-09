import javax.swing.table.DefaultTableModel;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.sql.*;

/**
 * Created by inf.carfagnol2912 on 19/11/2018.
 */
public class AggiornaAmmortamento {

    public static DefaultTableModel RefreshAmmortamento(DefaultTableModel tblModel){
        if(!(AdminMode.getSelectedTable().equals("GEN")||UserMode.getSelectedTable().equals("GEN"))) {
            Connection dbConnection = MainMagazzino.dbConnect();
            try {
                for (int i = 1; i < tblModel.getRowCount(); i++) {


                    float costoTotale = Float.parseFloat(tblModel.getValueAt(i, 6).toString());
                    float aliquota = Integer.parseInt(tblModel.getValueAt(i, 7).toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dataOra = sdf.format(new Timestamp(System.currentTimeMillis()));
                    //separo la stringa in due sottostringhe
                    String annoAttuale = dataOra.substring(0, dataOra.indexOf("-"));
                    int numeroAnni = Integer.parseInt(annoAttuale) - Integer.parseInt(tblModel.getValueAt(i, 9).toString().substring(0, tblModel.getValueAt(i, 9).toString().indexOf("-")));

                    float quotaAnnuale = costoTotale * (aliquota/100);
                    float quotePeriodo = quotaAnnuale * numeroAnni;
                    float valoreAggiornato = costoTotale - quotePeriodo;


                    PreparedStatement statement = dbConnection.prepareStatement("UPDATE "+AdminMode.getSelectedTable()+" SET CostoAmmortato = ? WHERE CodiceProdotto = ?");
                    statement.setString(1, String.valueOf(valoreAggiornato));
                    statement.setString(2, tblModel.getValueAt(i-1, 0).toString());
                    statement.execute();


                }
                dbConnection.close();
                return tblModel;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
