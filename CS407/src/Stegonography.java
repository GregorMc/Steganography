import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Stegonography {
    public void hideFile(File coverImage, File fileToHide) {
        if (enoughSpace(coverImage, fileToHide)) {
            //start hiding the image
            //get the type of the file
            String fileType = getFileTypeToHide(coverImage.getPath());
            //how big is the file to hide
            long fileSize = getFileSize(coverImage);
            //both of the above are at the start of every stegoimage
            BufferedImage cover = null;
            try {
                cover = ImageIO.read(coverImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //start changing bits in the coverImage but ignore the first 54 Bytes

            //write the hidden file size

            //write the hidden file type

            //write the rest of the file

            //write altered cover image to new file
            File stegoImage = new File("CS407/stegoImage.BMP");
            try {
                ImageIO.write(cover,"BMP", stegoImage);
                System.out.println("Writing complete");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The coverImage does not have a big enough file size to hide the given file");
        }

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
        //4 Bytes for image
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

    public String getHiddenFileType() {
        // get the file type of hidden file within the Stego-image
        return null;
    }

    public String getFileTypeToHide(String filepath) {
        //get the file type of the file to hide
        int typeStart = filepath.indexOf(".");
        String fileType = filepath.substring((typeStart + 1), filepath.length());
        return fileType;
    }
}
