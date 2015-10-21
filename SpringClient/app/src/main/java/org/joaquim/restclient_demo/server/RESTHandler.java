package org.joaquim.restclient_demo.server;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/***
 * Class that handles the communications with the REST Service. Extends the android.os.AsyncTask
 *
 * The three types used in the class definition, <String, Void, Bitmap>, are the following:
 * 1 - Params, the type of the parameters sent to the task upon execution
 * 2 - Progress, the type of the progress units published during the background computation
 * 3 - Result, the type of the result of the background computation
 *
 * Since we want to send an image to the REST Service and grab its response, we pass String
 * parameters to the task upon execution, and it returns a RESTHandlerReturn class, containing the
 * image processed and sent by the REST Service and an error status (String), in case of error
 */
public class RESTHandler extends AsyncTask<String, Void, RESTHandlerReturn> {

    private static final String TAG = RESTHandler.class.getSimpleName();
    private final String uploadURL = "http://liis-lab.dei.uc.pt:8080/upload";

    private FileSystemResource fileToUpload;

    public RESTHandler(String picturePath) {
        fileToUpload = new FileSystemResource(picturePath);
    }

    /**
     * Override the doInBackground method, invoked on the background thread immediately after the
     * onPreExecute() finishes executing. This method is used to perform background computations
     * that can take a long time
     * @param params An array of String objects that, in this case, only contains one element: The
     *               text to include in the image
     * @return A RESTHandlerReturn object, containing the user's selected image after it has been
     *         processed by the REST Service and an error status (String), in case of error
     */
    @Override
    protected RESTHandlerReturn doInBackground(String... params) {
        return doUploadFile(params[0]);
    }

    /**
     * Private method where we implement the file upload and download
     * @param memeText The text to be added to the file
     * @return A RESTHandlerReturn object, containing the user's selected image after it has been
     *         processed by the REST Service and an error status (String), in case of error
     */
    private RESTHandlerReturn doUploadFile(String memeText) {
        // Start off by instantiating the RestTemplate from Spring
        RestTemplate restTemplate = new RestTemplate();

        /**
         * Since we will be passing objects from one side to another in HTTP requests and responses
         * we need to be able to convert them into valid formats for the web. That is what the
         * HttpMessageConverter class does: It is a "strategy interface that specifies a converter
         * that can convert from and to HTTP requests and responses"
         * To do this we simply need to create an ArrayList with the following Message Converters:
         * - StringHttpMessageConverter: Implementation of HttpMessageConverter that can read and
         *                               write strings
         * - FormHttpMessageConverter: Implementation of HttpMessageConverter to read and write
         *                             'normal' HTML forms and also to write (but not read)
         *                             multipart data (e.g. file uploads)
         * - MappingJackson2HttpMessageConverter: Implementation of HttpMessageConverter that can
         *                                        read and write JSON using Jackson 2.x's
         *                                        ObjectMapper
         */
        ArrayList<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        messageConverters.add(new ResourceHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        // Set the request factory that the RestTemplate uses for obtaining HttpRequests
        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());

        /**
         * Set the parameters to be passed to the REST Service method. This needs to be a map
         * so we can assign the desire value for each parameter.
         * A MultiValueMap is an extension of the Map interface that stores multiple values
         */
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", fileToUpload);
        map.add("memeText", memeText);

        // Call the service method and wait for its response
        FileUploadReturn response = restTemplate.postForObject(uploadURL, map,
                                                               FileUploadReturn.class);

        // Get the response's data
        byte[] imageBytes = response.getResultingImage();
        String status = response.getStatus();

        if (status.equals("OK")) {
            if (imageBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                return new RESTHandlerReturn(bitmap, null, response.getImageTitle());
            }
            return new RESTHandlerReturn(null, "Could not retrieve the image", "");
        } else {
            return new RESTHandlerReturn(null, status, "");
        }
    }
}
