import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BottoniIndirizzi extends JButton{
    //metodo che setta il bottone attivo manualmente
    public static void setBtn(BottoniIndirizzi btn) {
        BottoniIndirizzi.btn = btn;
    }
    //propeità che salva l'ultimo tasto premuto
    static BottoniIndirizzi btn = null;

    public BottoniIndirizzi(String nome) {
        /*
            imposto testo del bottone in base alla stringa passata,
            colore di sfondo a bianco
            rimuovo il rimepimento interno del bottone
            imposto l'opacità
            rimuovo la grafica del focus quando il bottone lo ottiene
            imposto l'allineamento del testo a sinistra
            imposto i bordi di 8x8
         */
        setText(nome);
        setFont(new Font("Arial", Font.PLAIN, 11));
        setBackground(Color.WHITE);
        setContentAreaFilled(false);
        setOpaque(true);
        setFocusPainted(false);
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        setForeground(new Color(120,120,120));

        /*
            con il mouse adapter imposto le varie opzioni grafiche in base all' azione eseguita dal mouse
            (entra nel bottone, preme il bottone, rilascia la pressione e esce dal bottone)
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Color.lightGray);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(new Color(200,200,200));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(Color.lightGray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.WHITE);
            }
        });

        //alla pressione del bottone, imposto a nero il colore del testo dell' ultimo bottone salvato, salvo il nuovo bottone e imposto il suo testo a blu
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(btn!=null) {
                    btn.setForeground(new Color(120, 120, 120));
                    btn.setFont(new Font("Arial", Font.PLAIN, 11));
                }
                btn=(BottoniIndirizzi)e.getSource();
                btn.setForeground(Color.BLACK);
                btn.setFont(new Font("Arial", Font.PLAIN, 12));


            }
        });

    }
}
