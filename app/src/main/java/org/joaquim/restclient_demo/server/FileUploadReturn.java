package org.joaquim.restclient_demo.server;

/**
 * Class that represents the structure returned by the REST Service after processing the uploaded
 * image.
 * It contains a byte[] attribute to store the byte representation of the processed uploaded image,
 * a String field named "imageTitle" to store the title of the processed uploaded image
 * and a String field named "status" to signal the status of the operation. The possible
 * values for this attribute are:
 *          - OK --> Everything went well, with no major problems
 *          - EMPTY FILE --> The uploaded file is empty and could not be processed
 *          - ERROR PROCESSING IMAGE --> There was an error processing the uploaded file
 */
public class FileUploadReturn {

    private byte[] resultingImage;
    private String status;
    private String imageTitle;

    /**
     * Simple class constructor
     * @param resultingImage The byte representation of the processed uploaded image
     * @param status The status of the operation
     * @param imageTitle The title of the image
     */
    public FileUploadReturn(byte[] resultingImage, String status, String imageTitle) {
        this.status = status;
        this.resultingImage = resultingImage;
        this.imageTitle = imageTitle;
    }

    /**
     * Empty constructor. Needs to be created here to allow the RestTemplate to get the result from
     * the REST Service
     */
    public FileUploadReturn() {
    }

    /**
     * Getter for the status attribute
     * @return The status attribute
     */
    public String getStatus() {
        return status;
    }

    /**
     * Getter for the byte[] attribute containing the byte representation of the processed uploaded
     * image
     * @return The byte[] attribute containing the byte representation of the processed uploaded
     *         image
     */
    public byte[] getResultingImage() {
        return resultingImage;
    }

    /**
     * Getter for the imageTitle attribute, containing the title of the processed uploaded image
     * @return The String attribute containing the title of the processed uploaded image
     */
    public String getImageTitle() {
        return imageTitle;
    }
}
