package totalpos;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author shidalgo
 */
public class Bottom extends JPanel{

    private Image wallpaper;

    public Bottom(Image wallpaper) {
        super();
        this.wallpaper = wallpaper;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(wallpaper, 0, 0, getWidth(), getHeight(),this);
        setOpaque(false);
        super.paint(g);
    }

}
