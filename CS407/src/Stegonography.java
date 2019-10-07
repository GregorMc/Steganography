import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class Stegonography {
    public void hideFile(File coverImage, File fileToHide) {
        if (enoughSpace(coverImage, fileToHide)) {
            //start hiding the image
            //get the type of the file
            String fileType = getFileTypeToHide(fileToHide.getPath());
            //how big is the file to hide
            long fileSize = getFileSize(fileToHide);
            //both of the above are at the start of every stegoimage
            BufferedImage cover = null;
            try {
                cover = ImageIO.read(coverImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //start changing bits in the coverImage but ignore the first 54 Bytes


//colour to bin test
//            System.out.println(getRGB(cover, (int) getFileSize(fileToHide) + 96).length);
//            Color mycolor = (getRGB(cover,50000)[25000]);
//            Integer red = mycolor.getRed();
//            System.out.println(mycolor);
//            System.out.println(Integer.toBinaryString(red));




            //write the hidden file size

            //write the hidden file type

            //write the rest of the file

            //write altered cover image to new file
            File stegoImage = new File("CS407/stegoImage.BMP");
            try {
                ImageIO.write(cover, "BMP", stegoImage);
                System.out.println("Writing complete");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The coverImage does not have a big enough file size to hide the given file");
        }

    }

    public Color[] getRGB(BufferedImage img, int amount) {
        int x = img.getWidth();
        int y = img.getHeight();
        int counter = 0;
        Color[] rgb = new Color[amount];

        for (int i = 0; (i < x) && (counter < amount); i++) {
            for (int j = 0; (j < y) && (counter < amount); j++) {
                rgb[counter] = new Color(img.getRGB(i, j));
                counter++;
            }
        }
        return rgb;
    }

    public void revealFile() {
        //get the file from a Stego-image

        //get the number of hidden bits

        //get the file type

        //ignore first 150 Bytes then get the last bit of each byte and add to array

        //write generated array to a file of the file type extracted earlier
    }

    public File getImage(String imagePath) {
        //get the coverImage from file system
        File coverImage = new File(imagePath);
        return coverImage;
    }

    public File getFileToHide(String filePath) {
        //get file to hide from cover system
        File fileToHide = new File(filePath);
        return fileToHide;
    }

    public long getFileSize(File file) {
        //how many bytes in any given file
        return file.length();
    }

    public long maxPossibleFileSize(long coverImageSize) {
        // how many LSB are useable?
        //54 bytes reserved for file info
        //32 LSB for size of hidden file
        //64 LSB for file type to hide
        long maximum = coverImageSize - (54 + 96);
        return maximum;
    }

    public boolean enoughSpace(File coverImage, File fileToHide) {
        // is the maximum possible file size > file to be hidden file size
        long coverImageSize = getFileSize(coverImage);
        long fileToHideSize = getFileSize(fileToHide);
        if (fileToHideSize < maxPossibleFileSize(coverImageSize)) {
            return true;
        }
        return false;
    }

    public String getHiddenFileType(BufferedImage image) {
        // get the file type of hidden file within the Stego-image

        //ignore first 54Bytes
        //ignore another 32Bytes
        //read next 64Bytes

        for (int i = 0; i < image.getWidth(); i++) {

        }

        return null;
    }

    public String getFileTypeToHide(String filepath) {
        //get the file type of the file to hide
        int typeStart = filepath.indexOf(".");
        String fileType = filepath.substring((typeStart + 1), filepath.length());
        return fileType;
    }

//still need to take in image as parameter
    public int bitManipulation(byte[] payload, int bytesToManipulate) {
        int offset = (54 + 96);

        for (int i=0; i<bytesToManipulate; i++, offset++) {
            int payloadByte = payload[offset];
            for (int bit =7; bit>=0; --bit) {
               int b = (payloadByte >> bit) & 1;

               payload[offset] = (byte)((payload[offset] & 0x1) | b);
            }

        }


    }
}
