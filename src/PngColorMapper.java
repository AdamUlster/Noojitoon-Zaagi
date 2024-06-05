import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;

public class PngColorMapper {
    public static void main(String[] args) {


        try {
            // Read the PNG file
            BufferedImage image = ImageIO.read(new File("src/map.png"));

            int[][] mapCode = new int[50][50];

            for (int y = 0; y < 50; y++) {
                for (int x = 0; x < 50; x++) {
                    int rgbCode = image.getRGB(x,y);

                    if (rgbCode == -5708259) {
                        mapCode[x][y] = 0;
                    } else if (rgbCode == -13683047){
                        mapCode[x][y] = 2;
                    } else if (rgbCode == -14503604) {
                        mapCode[x][y] = 3;
                    }
                }
            }

            for (int y = 0; y < 50; y++) {
                for (int x = 0; x < 50; x++) {
                    System.out.print(mapCode[x][y] + " ");
                }
                System.out.println();
            }
            System.out.println("Color mapping completed successfully.");
        } catch (IOException e) {
            System.out.println("Error reading or writing file: " + e.getMessage());
        }
    }
}
