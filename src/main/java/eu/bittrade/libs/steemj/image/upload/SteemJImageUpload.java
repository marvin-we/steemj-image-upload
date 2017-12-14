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
package eu.bittrade.libs.steemj.image.upload;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.javanet.NetHttpTransport;

import eu.bittrade.crypto.core.CryptoUtils;
import eu.bittrade.crypto.core.DumpedPrivateKey;
import eu.bittrade.crypto.core.ECKey;
import eu.bittrade.crypto.core.Sha256Hash;
import eu.bittrade.crypto.core.base58.Sha256ChecksumProvider;
import eu.bittrade.libs.steemj.image.upload.config.SteemJImageUploadConfig;
import eu.bittrade.libs.steemj.image.upload.models.AccountName;

/**
 * This class provides the core functionality to upload images to
 * steemimages.com.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SteemJImageUpload {
    private static final Logger LOGGER = LoggerFactory.getLogger(SteemJImageUpload.class);

    /**
     * This class should not be instantiated as all methods are private.
     */
    private SteemJImageUpload() {
    }

    /**
     * Upload the given <code>fileToUpload</code> to the
     * {@link SteemJImageUploadConfig#getSteemitImagesEndpoint()
     * SteemitImagesEndpoint}.
     * 
     * In addition to the file, the method also requires a valid Steem
     * <code>accountName</code> and the <code>privatePostingKey</code> of this
     * account, as uploading is only available for Steem users.
     * 
     * <p>
     * Example: <code>
     * uploadImage(new AccountName("dez1337"), 5KMamixsFoUkdlz7sNG4RsyaKQyJMBBqrdT6y54qr4cdVhU9rz7, new File("C:\Path\To\Image.png"))
     * </code>
     * </p>
     * 
     * @param accountName
     *            The Steem account used to sign the upload.
     * @param privatePostingKey
     *            The private posting key of the <code>accountName</code>.
     * @param fileToUpload
     *            The image to upload.
     * @return A URL object that contains the download URL of the image.
     * @throws IOException
     *             In case the given <code>fileToUpload</code> could not be
     *             transformed into its byte representation.
     * @throws HttpResponseException
     *             In case the
     *             {@link SteemJImageUploadConfig#getSteemitImagesEndpoint()
     *             SteemitImagesEndpoint} returned an error.
     */
    public static URL uploadImage(AccountName accountName, String privatePostingKey, File fileToUpload)
            throws IOException {
        // Transform the WIF private Key into a real ECKey object.
        ECKey privatePostingKeyAsKey = DumpedPrivateKey
                .fromBase58(null, privatePostingKey, new Sha256ChecksumProvider()).getKey();

        byte[] inputData = new byte[(int) fileToUpload.length()];
        // Generate the byte representation for the given file.
        try (FileInputStream fileInputStream = new FileInputStream(fileToUpload.getAbsoluteFile());
                DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(fileInputStream))) {
            dataInputStream.readFully(inputData);
        } catch (IOException e) {
            throw new IOException("Could not transform the given file into its byte representation.", e);
        }

        Sha256Hash imageHash = null;
        // Create a hash for the byte representation and the image signing
        // challenge.
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(
                    SteemJImageUploadConfig.getInstance().getImageSigningChallenge().getBytes(StandardCharsets.UTF_8));
            outputStream.write(inputData);
            imageHash = Sha256Hash.of(outputStream.toByteArray());
        } catch (IOException e) {
            throw new IOException("Could not transform the given file into its byte representation.", e);
        }

        String signature = privatePostingKeyAsKey.signMessage(imageHash);

        return executeMultipartRequest(accountName, CryptoUtils.HEX.encode(Base64.decode(signature)), fileToUpload);
    }

    /**
     * This method handles the final upload to the
     * {@link SteemJImageUploadConfig#getSteemitImagesEndpoint()
     * SteemitImagesEndpoint}.
     * 
     * @param accountName
     *            The Steem account used to sign the upload.
     * @param signature
     *            The signature for this upload.
     * @param fileToUpload
     *            The image to upload.
     * @return A URL object that contains the download URL of the image.
     * @throws HttpResponseException
     *             In case the
     *             {@link SteemJImageUploadConfig#getSteemitImagesEndpoint()
     *             SteemitImagesEndpoint} returned an error.
     */
    private static URL executeMultipartRequest(AccountName accountName, String signature, File fileToUpload)
            throws IOException {
        NetHttpTransport.Builder builder = new NetHttpTransport.Builder();

        MultipartContent content = new MultipartContent().setMediaType(new HttpMediaType("multipart/form-data")
                .setParameter("boundary", "----WebKitFormBoundaryaAsqCuJ0UrJUS0dz"));

        FileContent fileContent = new FileContent(URLConnection.guessContentTypeFromName(fileToUpload.getName()),
                fileToUpload);

        MultipartContent.Part part = new MultipartContent.Part(fileContent);

        part.setHeaders(new HttpHeaders().set("Content-Disposition",
                String.format("form-data; name=\"image\"; filename=\"%s\"", fileToUpload.getName())));

        content.addPart(part);

        HttpRequest httpRequest = builder.build().createRequestFactory(new HttpClientRequestInitializer())
                .buildPostRequest(new GenericUrl(SteemJImageUploadConfig.getInstance().getSteemitImagesEndpoint() + "/"
                        + accountName.getName() + "/" + signature), content);

        LOGGER.debug("{} {}", httpRequest.getRequestMethod(), httpRequest.getUrl().toURI());

        HttpResponse httpResponse = httpRequest.execute();

        LOGGER.debug("{} {} {} ({})", httpResponse.getRequest().getRequestMethod(),
                httpResponse.getRequest().getUrl().toURI(), httpResponse.getStatusCode(),
                httpResponse.getStatusMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode response = objectMapper.readTree(httpResponse.parseAsString());

        return new URL(response.get("url").asText());
    }
}
