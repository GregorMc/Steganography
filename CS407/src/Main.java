import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(final String[] args) {
        String coverImageFileName = "CS407/MARBLES.BMP";
        Scanner reader = new Scanner(System.in);
        boolean allowedInput = false;
        int n = 1;
        Stegonography stego = new Stegonography();
        while (!allowedInput) {
            System.out.println("Please select one of the options below");
            System.out.println("1: A bmp image of a stickman\n2: A jpg image of a stickman\n3: A png image of a stickman\n4: A txt file containing the first 10 chapters of A Tale of 2 Cities by Charles Dickens" +
                    "\n5: A gif of Stewie from Family Guy\n6: Part of the Rick and Morty theme song\n7: Our Project Work breakdown pdf document \n8: Project breakdown.odt" +
                    "\n9: Enter your own Cover Image and Payload file \n10: reveal file only");
            System.out.println("Which example would you like to run?: ");
            if (reader.hasNextInt()) {
                n = reader.nextInt();
                if (n > 10 || n < 1) {
                    System.out.println("Sorry this is not a valid input");
                } else {
                    allowedInput = true;
                }
            } else {
                reader.next();
                continue;
            }
        }

        int example = n - 1;
        String file = null;
        switch (example) {
            case 0:
                file = "CS407/example.bmp";
                break;
            case 1:
                file = "CS407/example.jpg";
                break;
            case 2:
                file = "CS407/example.png";
                break;
            case 3:
                file = "CS407/test.txt";
                break;
            case 4:
                file = "CS407/source.gif";
                break;
            case 5:
                file = "CS407/R&MSample.mp3";
                break;
            case 6:
                file = "CS407/Project work breakdown.pdf";
                break;
            case 7:
                file = "CS407/Project breakdown.odt";
                break;
            case 8:
                //enter your own
                reader = new Scanner(System.in);
                allowedInput = false;

                //input validation
                while (!allowedInput) {
                    System.out.println("Please enter the file name of your coverImage");
                    coverImageFileName = reader.nextLine();
                    File coverfile = new File(coverImageFileName);
                    String coverfileType = stego.getFileTypeToHide(coverImageFileName);

                    if (coverfile.exists() && !coverfile.isDirectory()) {
                        //file exists
                        coverfileType = coverfileType.toLowerCase();
                        allowedInput = true;
                        if (!coverfileType.equals("bmp")) {
                            System.out.println("Sorry the cover image can only be a Bitmap");
                            allowedInput = false;
                        }
                    } else {
                        allowedInput = false;
                    }
                    if (allowedInput) {
                        System.out.println("Please enter the file name of your payload");
                        file = reader.nextLine();
                        File f = new File(file);
                        if (f.exists() && !f.isDirectory()) {
                            // file exists
                            allowedInput = true;
                        } else {
                            allowedInput = false;
                        }
                    }
                }
                reader.close();
                break;
        }
        if (example == 9) {
            File stegoFile = new File("stegoImage.BMP");
            if (stegoFile.exists() && !stegoFile.isDirectory()) {
                stego.revealFile(stegoFile);
            } else {
                System.out.println("Sorry, there is no stego file, please ensure the file is called 'stegoImage.bmp'");
            }
        } else {
            File coverImage = stego.getImage(coverImageFileName);
            File fileToHide = new File(file);
            boolean hiddenSuccessfully = false;
            try {
                hiddenSuccessfully = stego.hideFile(coverImage, fileToHide);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (hiddenSuccessfully) {
                File stegoFile = new File("CS407/stegoImage.BMP");
                stego.revealFile(stegoFile);
            }
        }
    }
}
