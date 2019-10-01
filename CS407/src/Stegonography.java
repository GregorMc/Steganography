import java.io.File;

public class Stegonography {
    public void hideFile(File coverImage, File fileToHide){
        if (enoughSpace(coverImage, fileToHide)){
            //start hiding the image

        }else{
            System.out.println("The coverImage does not have a big enough file size to hide the given file");
        }

    }
    public void revealFile(){
        //get the file from a Stego-image
    }
    public File getImage(String imagePath){
        //get the coverImage from file system
        File coverImage = new File(imagePath);
        return coverImage;
    }
    public File getFileToHide(String filePath){
        //get file to hide from cover system
        File fileToHide = new File(filePath);
        return fileToHide;
    }
    public long getFileSize(File file){
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
    public boolean enoughSpace (File coverImage, File fileToHide){
        // is the maximum possible file size > file to be hidden file size
        long coverImageSize = getFileSize(coverImage);
        long fileToHideSize = getFileSize(fileToHide);
        if(fileToHideSize < maxPossibleFileSize(coverImageSize)){
            return true;
        }
        return false;
    }
    public String getHiddenFileType(){
        // get the file type of hidden file within the Stego-image
        return null;
    }
    public String getFileTypeToHide(){
        //get the file type of the file to hide
        return null;
    }
}
