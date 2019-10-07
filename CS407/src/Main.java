import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(final String[] args) {
        Stegonography stego = new Stegonography();
        File coverImage = stego.getImage("CS407/MARBLES.BMP");
        File fileToHide = new File("CS407/test.txt");
        stego.hideFile(coverImage,fileToHide);
        try {
            byte[] bytes = stego.getBytesFromFile(fileToHide);
            for(int i = 0;i<bytes.length;i++){
                System.out.println(String.format("%8s", Integer.toBinaryString(bytes[i])).replace(' ', '0'));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
       // System.out.println( stego.bitManipulation("00000001", 0));
    }
}
