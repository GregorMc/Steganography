import java.io.File;
public class Main {
    public static void main(final String[] args) {
        Stegonography stego = new Stegonography();
        File coverImage = stego.getImage("CS407/MARBLES.BMP");
        File fileToHide = new File("CS407/test.txt");
        stego.hideFile(coverImage,fileToHide);
    }
}
