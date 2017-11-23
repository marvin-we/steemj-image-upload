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

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.javanet.NetHttpTransport;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.image.upload.config.SteemJImageUploadConfig;
import eu.bittrade.libs.steemj.util.SteemJUtils;

public class SteemJImageUpload {
    public static URL uploadImage(AccountName accountName, String privatePostingKey, File fileToUpload)
            throws IOException {
        /*
         * Disable the Request Timeout
         * 
         */
        SteemJConfig.getInstance().setIdleTimeout(0);
        // Transform the WIF private Key into a real ECKey object.
        ECKey privatePostingKeyAsKey = DumpedPrivateKey.fromBase58(null, privatePostingKey).getKey();

        if (privatePostingKeyAsKey == null) {
            // TODO: Throw new Exception.
        }

        byte[] inputData = new byte[(int) fileToUpload.length()];
        // Generate the byte representation for the given file.
        try (FileInputStream fileInputStream = new FileInputStream(fileToUpload.getAbsoluteFile());
                DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(fileInputStream))) {
            dataInputStream.readFully(inputData);
        } catch (IOException e) {
            // TODO: Throw new Exception.
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
            // TODO: Throw new Exception.
        }

        ECDSASignature ecdsaSignature = privatePostingKeyAsKey.sign(imageHash);

        // TODO: Assert: byte[] der = signature.encodeToDER();short lenR =
        // der[3];short lenS = der[5 + lenR];if (lenR == 32 && lenS == 32)

        int keyType = SteemJUtils.getKeyType(ecdsaSignature, imageHash, privatePostingKeyAsKey);
        byte[] signature = SteemJUtils.createSignedTransaction(keyType, ecdsaSignature, privatePostingKeyAsKey);

        // TODO: LOG Signed Message Hash as DEBUG:
        // Utils.HEX.encode(signedTransaction)

        return executeMultipartRequest(accountName, Utils.HEX.encode(signature), fileToUpload);
    }

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

        // TODO: LOG URL

        HttpRequest httpRequest = builder.build().createRequestFactory(new HttpClientRequestInitializer())
                .buildPostRequest(new GenericUrl(SteemJImageUploadConfig.getInstance().getSteemitImagesEndpoint() + "/"
                        + accountName.getName() + "/" + signature), content);

        HttpResponse httpResponse = httpRequest.execute();

        // TODO: LOG RAW RESPONSE

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode response = objectMapper.readTree(httpResponse.parseAsString());

        // TODO: Verify response

        return new URL(response.get("url").asText());
    }
}
