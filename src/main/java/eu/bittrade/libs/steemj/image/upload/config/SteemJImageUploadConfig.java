package eu.bittrade.libs.steemj.image.upload.config;

/**
 * This class stores the configuration that is used for the communication to the
 * defined server.
 * 
 * The setters can be used to override the default values.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SteemJImageUploadConfig {
    /** The endpoint to send the upload request to. */
    private String steemitImagesEndpoint = "https://steemitimages.com";
    /** The image signing challenge added to the hash. */
    private String imageSigningChallenge = "ImageSigningChallenge";

    private static SteemJImageUploadConfig steemJImageUploadConfig;

    /**
     * Receive a
     * {@link eu.bittrade.libs.steemj.image.upload.config.SteemJImageUploadConfig
     * SteemJImageUploadConfig} instance.
     * 
     * @return A SteemJImageUploadConfig instance.
     */
    public static SteemJImageUploadConfig getInstance() {
        if (steemJImageUploadConfig == null) {
            steemJImageUploadConfig = new SteemJImageUploadConfig();
        }

        return steemJImageUploadConfig;
    }

    /**
     * Get the configured endpoint the upload requests are send to.
     * 
     * @return The configured endpoint the upload requests are send to.
     */
    public String getSteemitImagesEndpoint() {
        return steemitImagesEndpoint;
    }

    /**
     * Override the default steemit images endpoint.
     * 
     * @param steemitImagesEndpoint
     *            The endpoint the upload requests are send to.
     */
    public void setSteemitImagesEndpoint(String steemitImagesEndpoint) {
        this.steemitImagesEndpoint = steemitImagesEndpoint;
    }

    /**
     * Get the configured image signing challenge.
     * 
     * The signing challenge is a String that will be added to the generated
     * hash of the image and needs to be equivalent to the one used by the
     * server.
     * 
     * @return The configured image signing challenge.
     */
    public String getImageSigningChallenge() {
        return imageSigningChallenge;
    }

    /**
     * Override the default image signing challenge.
     * 
     * The signing challenge is a String that will be added to the generated
     * hash of the image and needs to be equivalent to the one used by the
     * server.
     * 
     * @param imageSigningChallenge
     *            The image signing challenge to set.
     */
    public void setImageSigningChallenge(String imageSigningChallenge) {
        this.imageSigningChallenge = imageSigningChallenge;
    }
}
