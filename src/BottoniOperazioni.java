import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BottoniOperazioni extends JButton{

    public BottoniOperazioni(String nome) {
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
        setBackground(new Color(220, 220, 220));
        setContentAreaFilled(false);
        setOpaque(true);
        setFocusPainted(false);
        setHorizontalAlignment(SwingConstants.LEFT);
        setForeground(Color.BLACK);

        setUI(new MetalButtonUI() {
            protected Color getDisabledTextColor() {
                return new Color(120,120,120);
            }
        });


        /*
            con il mouse adapter imposto le varie opzioni grafiche in base all' azione eseguita dal mouse
            (entra nel bottone, preme il bottone, rilascia la pressione e esce dal bottone)
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if(isEnabled()) {
                    setBackground(Color.lightGray);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(isEnabled()) {
                    setBackground(new Color(200, 200, 200));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(Color.lightGray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(220, 220, 220));
            }
        });

    }
}
