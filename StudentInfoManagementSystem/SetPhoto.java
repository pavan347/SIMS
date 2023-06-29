
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author pavan kumar
 */
public class SetPhoto {

    ImageIcon photo;

    public ImageIcon seticon(String myphoto, byte[] image, int width, int height) {
        if (myphoto != null) {
            photo = new ImageIcon(myphoto);
        } else {
            photo = new ImageIcon(image);
        }
        Image img1 = photo.getImage();
        Image img2 = img1.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon i = new ImageIcon(img2);
        return i;
    }
    public String path;

    public boolean checkPhoto() {
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File("C:/Users/pavan kumar/Pictures/photos"));
        int a = file.showSaveDialog(null);
        if (a == JFileChooser.APPROVE_OPTION) {
            File f = file.getSelectedFile();
            String im = f.getAbsolutePath();
            path = im;
            if (im.endsWith(".jpeg") || im.endsWith("png") || im.endsWith("jpg") || im.endsWith("img")) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid File format.");
            }
        }
        return false;
    }

    public String getPath() {
        return path;
    }
}
