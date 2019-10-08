import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Stegonography {
    public void hideFile(File coverImage, File fileToHide) throws IOException {
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

            int amount = ((int) getFileSize(fileToHide) + 96) / 3;
            Color[] rgb = getRGB(cover, amount);
            byte[] payloadBytes;
            payloadBytes = getBytesFromFile(fileToHide, getFileSize(fileToHide), getFileTypeToHide(fileToHide.getPath()));

            char[] payloadBits = new char[(payloadBytes.length) * 8];
            int payloadbitCounter = 0;
            for (int i = 0; i < payloadBytes.length; i++) {
                String currentByte = String.format("%8s", Integer.toBinaryString((int) payloadBytes[i])).replace(' ', '0');
                for (int j = 0; j < 8; j++) {
                    payloadBits[payloadbitCounter] = currentByte.charAt(j);
                    payloadbitCounter++;
                }
            }

            payloadbitCounter = 0;
            for (int i = 0; i < rgb.length; i++) {

                Integer red = rgb[i].getRed();
                // get payload bit function

//                System.out.println("PIXEL NUMBER: " + i);
//                System.out.println("red " + red);
//                System.out.println("bytes: " + String.format("%8s", Integer.toBinaryString(red)).replace(' ', '0'));
//                System.out.println("payloadBit: " + payloadBits[payloadbitCounter]);
//                System.out.println("after change: " + bitManipulation(String.format("%8s", Integer.toBinaryString(red)).replace(' ', '0'), payloadBits[payloadbitCounter]));


                int redBit = Integer.parseInt(bitManipulation(String.format("%8s", Integer.toBinaryString(red)).replace(' ', '0'), payloadBits[payloadbitCounter]));
                payloadbitCounter++;
                //System.out.println("newREd " + redBit);
                Integer green = rgb[i].getGreen();
                // get payload bit function
                int greenBit = Integer.parseInt(bitManipulation(String.format("%8s", Integer.toBinaryString(green)).replace(' ', '0'), payloadBits[payloadbitCounter]));
                payloadbitCounter++;
                Integer blue = rgb[i].getBlue();
                // get payload bit function
                int blueBit = Integer.parseInt(bitManipulation(String.format("%8s", Integer.toBinaryString(blue)).replace(' ', '0'), payloadBits[payloadbitCounter]));
                payloadbitCounter++;

                Color newrgb = new Color(redBit, greenBit, blueBit);
                //System.out.println(rgb[i]);
                rgb[i] = newrgb;
                //System.out.println(rgb[i]);
            }

            BufferedImage cover2 = cover;
            int x = cover2.getWidth();
            int y = cover2.getHeight();
            int counter = 0;

            Color check = null;

            for (int i = 0; (i < x) && (counter < amount); i++) {
                for (int j = 0; (j < y) && (counter < amount); j++) {
                    if (counter == 28) {
                        Color[] originalrgb = getRGB(cover, amount);
                        check = originalrgb[28];
                    }
                    cover2.setRGB(i, j, rgb[counter].getRGB());
                    counter++;
                }
            }


            //write altered cover image to new file
            File stegoImage = new File("CS407/stegoImage.BMP");
            try {
                ImageIO.write(cover2, "BMP", stegoImage);
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
                System.out.println("x: "+i +" y: "+j +"Color: "+ rgb[counter]);
                counter++;
            }
        }
        return rgb;
    }

    public void revealFile(File stegoImage) {
        BufferedImage stego = null;
        try {
            stego = ImageIO.read(stegoImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] theBytes;
        theBytes = getHiddenFileTypeAndSize(stego);
        for (int i = 0; i < theBytes.length; i++) {
            System.out.println(theBytes[i]);
        }


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

    public byte[] getBytesFromFile(File payloadFile, long fileSize, String fileType) throws IOException {
        byte[] bytesArray = new byte[(int) payloadFile.length() + 12]; //4 bytes for file length 8 bytes for filetype

        byte[] fileTypeByteArray = new byte[8];
        int counter = 0;
        counter = fileType.getBytes().length;
        int position = 0;
        while (counter < 8) {
            fileTypeByteArray[position] = 0;
            counter = counter + 1;
            position++;
        }
        counter = 0;
        for (int i = position; i < 8; i++) {
            fileTypeByteArray[i] = fileType.getBytes()[counter];
            counter++;
        }
        for (int i = 4; i < 12; i++) {
            bytesArray[i] = fileTypeByteArray[i - 4];
        }
        //the above writes the fileType to the file
        byte[] filesizebytes = new byte[]{
                (byte) (fileSize >>> 24),
                (byte) (fileSize >>> 16),
                (byte) (fileSize >>> 8),
                (byte) fileSize};


        for (int i = 0; i < 4; i++) {
            bytesArray[i] = filesizebytes[i];
        }
        byte[] filebytes = new byte[(int) payloadFile.length()];
        FileInputStream fis = new FileInputStream(payloadFile);
        fis.read(filebytes);
        fis.close();

        for (int i = 0; i < filebytes.length; i++) {
            bytesArray[i + 12] = filebytes[i];
        }

//        for (int i = 0; i < bytesArray.length; i++) {
//            System.out.println(String.format("%8s", Integer.toBinaryString((int) bytesArray[i])).replace(' ', '0'));
//        }
        return bytesArray;
    }


    public String[] getHiddenFileTypeAndSize(BufferedImage stegoImage) {
        Color[] rgb = getRGB(stegoImage, 32);
        Integer[] bits = new Integer[96];

        int counter = 0;
        for (int i = 0; i < rgb.length; i++) {
            Integer red = rgb[i].getRed();
            String redByte = String.format("%8s", Integer.toBinaryString(red)).replace(' ', '0');
            redByte = redByte.substring(7, 8);
            bits[counter] = Integer.parseInt(redByte);
            counter++;

            Integer green = rgb[i].getGreen();
            String greenByte = String.format("%8s", Integer.toBinaryString(green)).replace(' ', '0');
            greenByte = greenByte.substring(7, 8);
            bits[counter] = Integer.parseInt(greenByte);
            counter++;

            Integer blue = rgb[i].getBlue();
            String blueByte = String.format("%8s", Integer.toBinaryString(blue)).replace(' ', '0');
            blueByte = blueByte.substring(7, 8);
            bits[counter] = Integer.parseInt(blueByte);
            counter++;
        }

        String[] bytes = new String[12];
        for(int i = 0; i <12; i ++){
            bytes[i] = "";
        }
        counter = 0;
        int index = 0;
        for (int i = 0; i < 96; i++) {
            if (counter < 8) {
                bytes[index] = bytes[index] + bits[i];
                counter ++;
            } else {
                i--;
                index++;
                counter = 0;
            }
        }

        return bytes;
    }

    public String getFileTypeToHide(String filepath) {
        //get the file type of the file to hide
        int typeStart = filepath.indexOf(".");
        String fileType = filepath.substring((typeStart + 1), filepath.length());
        return fileType;
    }


    public String bitManipulation(String payload, char payloadBit) {

        if (payloadBit == (payload.charAt(payload.length() - 1))) {
        } else {
            payload = payload.substring(0, payload.length() - 1);
            payload = payload + payloadBit;

        }

        return payload;
    }
}
