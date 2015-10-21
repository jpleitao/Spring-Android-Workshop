package hello;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class GreetingController {

    private static final int MAX_FONT_SIZE = 48;
    private static final int TOP_MARGIN = 5;
    private static final int SIDE_MARGIN = 10;

    private File convertToFile(MultipartFile file) {
        long unixTime;
        boolean result;
        FileOutputStream fos;
        File output;
        String originalFilename, extension, fileNameWithoutExtension, newFilePath;

        originalFilename = file.getOriginalFilename();
        extension = FilenameUtils.getExtension(originalFilename);
        fileNameWithoutExtension = FilenameUtils.removeExtension(originalFilename);
        unixTime = System.currentTimeMillis() / 1000L;
        newFilePath = "src/main/resources/uploadedImages/" + fileNameWithoutExtension + "_upload" + unixTime + "."
                      + extension;

        output = new File(newFilePath);

        try {
            result = output.createNewFile();

            if (result) {
                System.out.println("Created the file");
                fos = new FileOutputStream(output);

                fos.write(file.getBytes());
                fos.close();

                return output;
            }
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * Draws the given string centered, as big as possible, on either the top or
     * bottom 20% of the image given.
     */
    private static byte[] drawStringCentered(File uploadedFile, String memeText)
            throws InterruptedException, IOException {

        int height, fontSize, maxCaptionHeight, maxLineWidth;
        BufferedImage bufferedImage;
        String formattedString;
        Graphics graphics;

        if (memeText == null)
            memeText = "";

        bufferedImage = ImageIO.read(uploadedFile);

        fontSize = MAX_FONT_SIZE;
        maxCaptionHeight = bufferedImage.getHeight() / 5;
        maxLineWidth = bufferedImage.getWidth() - SIDE_MARGIN * 2;
        graphics = bufferedImage.getGraphics();

        do {
            graphics.setFont(new Font("Arial", Font.BOLD, fontSize));

            // first inject newlines into the text to wrap properly
            StringBuilder sb = new StringBuilder();
            int left = 0;
            int right = memeText.length() - 1;
            while ( left < right ) {

                String substring = memeText.substring(left, right + 1);
                Rectangle2D stringBounds = graphics.getFontMetrics().getStringBounds(substring, graphics);
                while ( stringBounds.getWidth() > maxLineWidth ) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    // look for a space to break the line
                    boolean spaceFound = false;
                    for ( int i = right; i > left; i-- ) {
                        if ( memeText.charAt(i) == ' ' ) {
                            right = i - 1;
                            spaceFound = true;
                            break;
                        }
                    }
                    substring = memeText.substring(left, right + 1);
                    stringBounds = graphics.getFontMetrics().getStringBounds(substring, graphics);

                    // If we're down to a single word and we are still too wide,
                    // the font is just too big.
                    if ( !spaceFound && stringBounds.getWidth() > maxLineWidth ) {
                        break;
                    }
                }
                sb.append(substring).append("\n");
                left = right + 2;
                right = memeText.length() - 1;
            }

            formattedString = sb.toString();

            // now determine if this font size is too big for the allowed height
            height = 0;
            for ( String line : formattedString.split("\n") ) {
                Rectangle2D stringBounds = graphics.getFontMetrics().getStringBounds(line, graphics);
                height += stringBounds.getHeight();
            }
            fontSize--;
        } while ( height > maxCaptionHeight );

        // draw the string one line at a time
        int y = TOP_MARGIN + graphics.getFontMetrics().getHeight();

        for ( String line : formattedString.split("\n") ) {
            // Draw each string twice for a shadow effect
            Rectangle2D stringBounds = graphics.getFontMetrics().getStringBounds(line, graphics);
            graphics.setColor(Color.BLACK);
            graphics.drawString(line, (bufferedImage.getWidth() - (int) stringBounds.getWidth()) / 2 + 2, y + 2);
            graphics.setColor(Color.WHITE);
            graphics.drawString(line, (bufferedImage.getWidth() - (int) stringBounds.getWidth()) / 2, y);
            y += graphics.getFontMetrics().getHeight();
        }

        // Load the image to a ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();

        byte[] byteImage = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return byteImage;
    }


    private byte[] processImage(File uploadedFile, String memeText) {
        int x;
        try{
            BufferedImage bufferedImage = ImageIO.read(uploadedFile);

            Graphics graphics = bufferedImage.getGraphics();
            graphics.setFont(graphics.getFont().deriveFont(60f));
            FontMetrics fontMetrics = graphics.getFontMetrics();

            Rectangle2D rectangle2D = fontMetrics.getStringBounds(memeText, graphics);

            if ( (bufferedImage.getWidth() - rectangle2D.getWidth() ) > 0)  {
                x = (int) (bufferedImage.getWidth() - rectangle2D.getWidth()) / 2;
            } else {
                x = 0;
            }

            graphics.setColor(Color.WHITE);
            graphics.drawString(memeText, x, 100);
            graphics.dispose();

            // Write the image to disk
            ImageIO.write(bufferedImage, "jpg", new File(uploadedFile.getAbsolutePath()));
            System.out.println("Wrote to the image");

            // Load the image to a ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            byteArrayOutputStream.flush();

            byte[] byteImage = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return byteImage;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // The @ResponseBody can be put on a method and indicates that the return type should be written straight to the HTTP response body
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    @ResponseBody
    public FileUploadReturn handleFileUpload(@RequestParam MultipartFile file, @RequestParam String memeText){
        File converToFileReturn;
        byte[] resultingImage;

        if (file.isEmpty()) {
            return new FileUploadReturn(null, "EMPTY FILE", "");
        }

        converToFileReturn = convertToFile(file);

        if (converToFileReturn == null) {
            return new FileUploadReturn(null, "ERROR PROCESSING IMAGE", "");
        }

        try {
            resultingImage = drawStringCentered(converToFileReturn, memeText);
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
            return new FileUploadReturn(null, "ERROR PROCESSING IMAGE", "");
        }

        return new FileUploadReturn(resultingImage, "OK", converToFileReturn.getName());
    }
}