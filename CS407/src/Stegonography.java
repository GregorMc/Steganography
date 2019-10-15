import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;

public class Stegonography {
    public boolean hideFile(File coverImage, File fileToHide) throws IOException {
        BufferedImage cover = null;
        cover = ImageIO.read(coverImage);

        if (enoughSpace(cover, fileToHide)) {
            int amount = ((int) getFileSize(fileToHide) * 8 + 96) / 3;
            Color[] rgb = getRGB(cover, amount);
            byte[] payloadBytes;
            payloadBytes = getBytesFromFile(fileToHide, getFileSize(fileToHide), getFileTypeToHide(fileToHide.getPath()));

            byte[] payloadBits = new byte[(payloadBytes.length) * 8];
            int payloadbitCounter = 0;

            for (int i = 0; i < payloadBytes.length; i++) {
                byte currentByteByte = (byte) (payloadBytes[i] & 0xFF);
                for (int j = 0; j < 8; j++) {
                    //we have to read in the left most bit first, if not then the file is in reverse
                    payloadBits[payloadbitCounter] = (byte) (currentByteByte & 128);
                    //the bit that we want is now in payloadBits at the 128 bit segment of the byte
                    payloadBits[payloadbitCounter] = (byte) (payloadBits[payloadbitCounter] >> 7);
                    //the bit we want is now in the rightmost segment of the byte
                    payloadBits[payloadbitCounter] = (byte) (payloadBits[payloadbitCounter] & 0x1);
                    //we now have only the bit we want
                    currentByteByte = (byte) (currentByteByte << 1);
                    //we have shifted the byte to the left so we can get the next bit in the byte
                    payloadbitCounter++;
                }
            }

            payloadbitCounter = 0;

            for (int i = 0; i < rgb.length; i++) {

                Integer red = rgb[i].getRed();
                int redBit = (bitManipulation(red.byteValue(), payloadBits[payloadbitCounter]) & 0xff);
                payloadbitCounter++;

                Integer green = rgb[i].getGreen();
                int greenBit = (bitManipulation(green.byteValue(), payloadBits[payloadbitCounter]) & 0xff);
                payloadbitCounter++;

                Integer blue = rgb[i].getBlue();
                int blueBit = (bitManipulation(blue.byteValue(), payloadBits[payloadbitCounter]) & 0xff);
                payloadbitCounter++;

                Color newrgb = new Color(redBit, greenBit, blueBit);
                rgb[i] = newrgb;
            }

            BufferedImage cover2 = cover;
            int x = cover2.getWidth();
            int y = cover2.getHeight();
            int counter = 0;

            for (int i = 0; (i < x) && (counter < amount); i++) {
                for (int j = 0; (j < y) && (counter < amount); j++) {
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
            return true;
        } else {
            System.out.println("The coverImage does not have a big enough file size to hide the given file");
            return false;
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

    public void revealFile(File stegoImage) {
        BufferedImage stego = null;
        try {
            stego = ImageIO.read(stegoImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] theBytes;
        theBytes = getHiddenFileTypeAndSize(stego);

        String sizeBytes = "";
        String typeBytes = "";
        String type = "";
        for (int i = 0; i < 4; i++) {
            sizeBytes = sizeBytes + theBytes[i];
        }


        for (int i = 4; i < 12; i++) {
            if (Integer.parseUnsignedInt(theBytes[i], 2) != 0) {
                type = type + (char) Integer.parseUnsignedInt(theBytes[i], 2);
            }
            typeBytes = typeBytes + theBytes[i];
        }

        int numberOfBytes = Integer.parseInt(sizeBytes, 2);

        String[] fileData = getHiddenFileData(stego, numberOfBytes);

        File revealed = new File("CS407/revealed." + type);

        byte[] fileBytes = new byte[fileData.length];

        for (int i = 0; i < fileData.length; i++) {
            Integer theByte = Integer.parseUnsignedInt(fileData[i], 2);
            fileBytes[i] = theByte.byteValue();
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(revealed);
            fileOutputStream.write(fileBytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Writing for revealed file complete");
    }

    public File getImage(String imagePath) {
        //get the coverImage from file system
        File coverImage = new File(imagePath);
        return coverImage;
    }

    public int getFileSize(File file) {
        //how many bytes in any given file
        return (int) file.length();
    }

    public boolean enoughSpace(BufferedImage cover, File fileToHide) {

        long coverImageSize = (cover.getWidth() * cover.getHeight() * 3);
        long fileToHideSize = getFileSize(fileToHide) * 8;

        if (fileToHideSize < (coverImageSize)) {
            return true;
        }
        return false;
    }

    public byte[] getBytesFromFile(File payloadFile, int fileSize, String fileType) throws IOException {
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
        byte[] filesizebytes = ByteBuffer.allocate(4).putInt(fileSize).array();

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
        return bytesArray;
    }


    public String[] getHiddenFileTypeAndSize(BufferedImage stegoImage) {
        Color[] rgb = getRGB(stegoImage, 32);
        int[] bits = new int[96];

        int counter = 0;
        for (int i = 0; i < rgb.length; i++) {
            Integer red = rgb[i].getRed();
            bits[counter] = (red & 0x1);
            counter++;

            Integer green = rgb[i].getGreen();
            bits[counter] = (green & 0x1);
            counter++;

            Integer blue = rgb[i].getBlue();
            bits[counter] = (blue & 0x1);
            counter++;
        }

        String[] bytes = new String[12];
        for (int i = 0; i < 12; i++) {
            bytes[i] = "";
        }
        counter = 0;
        int index = 0;
        for (int i = 0; i < 96; i++) {
            if (counter < 8) {
                bytes[index] = bytes[index] + bits[i];
                counter++;
            } else {
                i--;
                index++;
                counter = 0;
            }
        }
        return bytes;
    }

    public String[] getHiddenFileData(BufferedImage stegoImage, int numberOfBytes) {
        int numberOfBits = numberOfBytes * 8;
        int pixels = 1 + (int) Math.ceil((32 + (numberOfBits / 3)));
        Color[] rgb = getRGB(stegoImage, pixels);
        Integer[] bits = new Integer[numberOfBits];
        int counter = 0;
        for (int i = 32; i < rgb.length; i++) {
            if (counter == numberOfBits) {
                i = rgb.length + 1;
            } else {
                Integer red = rgb[i].getRed();
                bits[counter] = red & 0x1;
                counter++;
                if (counter == numberOfBits) {
                    i = rgb.length + 1;
                } else {
                    Integer green = rgb[i].getGreen();
                    bits[counter] = green & 0x1;
                    counter++;
                    if (counter == numberOfBits) {
                        i = rgb.length + 1;
                    } else {
                        Integer blue = rgb[i].getBlue();
                        bits[counter] = blue & 0x1;
                        counter++;
                    }
                }
            }
        }

        String[] bytes = new String[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
            bytes[i] = "";
        }
        counter = 0;
        int index = 0;
        for (int i = 0; i < numberOfBits; i++) {
            if (counter < 8) {
                bytes[index] = bytes[index] + bits[i];
                counter++;
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
        String fileType = filepath.substring((typeStart + 1));
        return fileType;
    }

    public byte bitManipulation(byte payload, byte payloadByte) {
        int payloadBit = payloadByte & 0x1;
        int lsb = payload & 0x1;
        if (lsb == 1) {
            //its even
            if (payloadBit == 0) {
                //swap last bit
                payload &= ~0x1;
            }
        } else {
            //its odd
            if (payloadBit == 1) {
                //swap last bit
                payload |= 0x1;
            }
        }
        return payload;
    }
}