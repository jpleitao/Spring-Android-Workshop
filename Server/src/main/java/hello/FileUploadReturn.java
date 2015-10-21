package hello;

public class FileUploadReturn {

    private String status;
    private String imageTitle;
    private byte[] resultingImage;

    public FileUploadReturn(byte[] resultingImage, String status, String imageTitle) {
        this.status = status;
        this.resultingImage = resultingImage;
        this.imageTitle = imageTitle;
    }

    public String getStatus() {
        return status;
    }

    public byte[] getResultingImage() {
        return resultingImage;
    }

    public String getImageTitle() {
        return imageTitle;
    }
}
