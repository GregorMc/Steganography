import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(final String[] args) {
        int example = 5;
        String file = null;
        if (example == 0) {
            file = "example.bmp";
        }
        if (example == 1) {
            file = "example.gif";
        }
        if (example == 2) {
            file = "example.jpg";
        }
        if (example == 3) {
            file = "example.png";
        }
        if (example == 4) {
            file = "test.txt";
        }
        if (example == 5) {
            file = "test2.txt";
        }
        if (example == 7) {
            file = "source.gif";
        }
        if (example == 8) {
            file = "mpthreetest.mp3";
        }
        if (example == 9){
            file = "Project work breakdown.pdf";
        }
        if (example == 10){
            file = "Project breakdown.odt";
        }

        Stegonography stego = new Stegonography();
        File coverImage = stego.getImage("CS407/MARBLES.BMP");
        File fileToHide = new File("CS407/" + file);
        try {
            stego.hideFile(coverImage, fileToHide);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File stegoFile = new File("CS407/stegoImage.BMP");
        stego.revealFile(stegoFile);


    }
}
