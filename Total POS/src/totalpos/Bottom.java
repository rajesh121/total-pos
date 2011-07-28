package totalpos;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author shidalgo
 */
public class Bottom extends JPanel{

    public Bottom() {
    }

    @Override
    public void paint(Graphics g) {
        Dimension tam = getSize();
        ImageIcon img = new ImageIcon(getClass().getResource("/totalpos/resources/Fondo-Inicio.jpg"));
        g.drawImage(img.getImage(), 0, 0, tam.width,tam.height,null);
        super.paint(g);
    }

}
