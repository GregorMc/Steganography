public class stegonography {
    public void hideFile(){
        //hide the file inside the cover image
    }
    public void revealFile(){
        //get the file from a Stego-image
    }
    public void getImage(){
        //not sure about what this would return
        //get the coverImage from file system
    }
    public void getFileToHide(){
        //get file to hide from cover system
    }
    public int getFileSize(){
        //how many bytes in any given file
        return 0;
    }
    public int maxPossibleFileSize(int coverImageSize) {
        // how many LSB are useable?
        return 0;
    }
    public boolean enoughSpace (){
        // is the maximum possible file size > file to be hidden file size
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
