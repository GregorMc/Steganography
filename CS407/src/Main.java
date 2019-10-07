import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(final String[] args) {
        Stegonography stego = new Stegonography();
        File coverImage = stego.getImage("CS407/MARBLES.BMP");
        File fileToHide = new File("CS407/test.txt");
        stego.hideFile(coverImage,fileToHide);
        try {
            stego.getBytesFromFile(fileToHide,stego.getFileSize(fileToHide),stego.getFileTypeToHide("CS407/test.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
