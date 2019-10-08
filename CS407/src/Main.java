import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(final String[] args) {
        Stegonography stego = new Stegonography();
        File coverImage = stego.getImage("CS407/MARBLES.BMP");
        File fileToHide = new File("CS407/test.txt");

        try {
            stego.hideFile(coverImage, fileToHide);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File stegoFile = new File("CS407/stegoImage.BMP");
        stego.revealFile(stegoFile);


    }
}
