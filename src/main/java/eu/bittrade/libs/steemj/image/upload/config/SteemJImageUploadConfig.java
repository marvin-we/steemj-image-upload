/*
 * Copyright (c) [2017] [ bittrade.eu ]
 * This file is part of steemj-image-upload.
 * 
 * Steemj-image-upload is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Steemj-image-upload is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Foobar. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.bittrade.libs.steemj.image.upload.config;

/**
 * The SteemJImageUpload configuration. The class is already initiated with
 * proper default values and does not need to be changed in general.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SteemJImageUploadConfig {
    /** The wrapped <code>SteemJImageUploadConfig</code> instance. */
    private static SteemJImageUploadConfig steemJImageUploadConfig;

    /** The endpoint to send the upload request to. */
    private String steemitImagesEndpoint = "https://steemitimages.com";
    /** The image signing challenge added to the hash. */
    private String imageSigningChallenge = "ImageSigningChallenge";
    /** The timeout in milliseconds to establish a connection. */
    private int connectTimeout = 0;
    /**
     * The timeout in milliseconds to read data from an established connection.
     */
    private int readTimeout = 0;

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

    /**
     * Get the configured timeout to establish a connection in millisecond (0
     * for an infinite timeout).
     * 
     * @return the timeout to establish a connection in millisecond.
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Override the default <code>connectTimeout</code>.
     * 
     * @param connectTimeout
     *            the timeout to establish a connection in millisecond or 0 for
     *            an infinite timeout.
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * Sets the timeout in milliseconds to read data from an established
     * connection or 0 for an infinite timeout.
     * 
     * @return the readTimeout
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * Override the default <code>readTimeout</code>.
     * 
     * Sets the timeout in milliseconds to read data from an established
     * connection or 0 for an infinite timeout.
     * 
     * @param readTimeout
     *            the readTimeout to set
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

}
