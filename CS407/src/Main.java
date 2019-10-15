import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(final String[] args) {
        String coverImageFileName = "MARBLES.BMP";
        Scanner reader = new Scanner(System.in);  // Reading from System.in
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
        if (example == 0) {
            file = "example.bmp";
        }
        if (example == 1) {
            file = "example.jpg";
        }
        if (example == 2) {
            file = "example.png";
        }
        if (example == 3) {
            file = "test.txt";
        }
        if (example == 4) {
            file = "source.gif";
        }
        if (example == 5) {
            file = "mpthreetest.mp3";
        }
        if (example == 6) {
            file = "Project work breakdown.pdf";
        }
        if (example == 7) {
            file = "Project breakdown.odt";
        }
        if (example == 8) {
            //enter your own
            reader = new Scanner(System.in);
            allowedInput = false;

            //input validation
            while (!allowedInput) {
                System.out.println("Please enter the file name of your coverImage");
                coverImageFileName = reader.nextLine();
                File coverfile = new File("CS407/" + coverImageFileName);
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
                    File f = new File("CS407/" + file);
                    if (f.exists() && !f.isDirectory()) {
                        // file exists
                        allowedInput = true;
                    } else {
                        allowedInput = false;
                    }
                }
            }
            reader.close();
        }
        if (example == 9) {
            File stegoFile = new File("CS407/stegoImage.BMP");
            if (stegoFile.exists() && !stegoFile.isDirectory()) {
                stego.revealFile(stegoFile);
            } else {
                System.out.println("Sorry, there is no stego file, please ensure the file is called 'stegoImage.bmp'");
            }
        } else {
            File coverImage = stego.getImage("CS407/" + coverImageFileName);
            File fileToHide = new File("CS407/" + file);
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
