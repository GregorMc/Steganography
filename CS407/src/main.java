import java.io.File;
public class main {
    public static void main(final String[] args) {
        stegonography stego = new stegonography();
        File coverImage = stego.getImage("CS407/MARBLES.BMP");
        System.out.println("#ofBytes: " + stego.getFileSize(coverImage));
//        File fileToHide = stego.getFileToHide("");
//        stego.hideFile(coverImage, fileToHide);
        System.out.println("file type: " + stego.getFileTypeToHide("CS407/MARBLES.BMP"));
    }
}
