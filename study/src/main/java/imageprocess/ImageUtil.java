import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * @author 오연지
 * cineplex movie 이미지 처리 유틸
 */
public class ImageUtil {
    public static void lpMoviePosterOriginDownload(String posterOrigin, String lpPosterOrigin, String posterExtension) throws IOException {
        URL url = new URL(posterOrigin);
        BufferedImage img = ImageIO.read(url);
        File file = new File(lpPosterOrigin);
        ImageIO.write(img, posterExtension, file);
    }

    public static void lpMoviePosterOriginResize(String lpPosterOrigin, String lpPosterThumb, String extension) throws IOException {
        ImageIcon introIc = new ImageIcon(lpPosterOrigin);
        Image appIcon = introIc.getImage();

        int imageType = BufferedImage.TYPE_INT_RGB;
        BufferedImage scaledBI = new BufferedImage(1032, 1448, imageType);

        Graphics2D g2 = scaledBI.createGraphics();
        g2.drawImage(appIcon, 0, 0, 1032, 1448, null);
        g2.dispose();

        ImageIO.write(scaledBI, extension, new File(lpPosterThumb));
    }
}
