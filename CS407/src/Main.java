import java.io.File;
public class Main {
    public static void main(final String[] args) {
        Stegonography stego = new Stegonography();
        File coverImage = stego.getImage("CS407/MARBLES.BMP");
        System.out.println("#ofBytes: " + stego.getFileSize(coverImage));
//        File fileToHide = stego.getFileToHide("");
//        stego.hideFile(coverImage, fileToHide);
        System.out.println("file type: " + stego.getFileTypeToHide("CS407/MARBLES.BMP"));
    }
}
