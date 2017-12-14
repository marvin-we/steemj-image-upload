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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.io.Resources;

import eu.bittrade.libs.steemj.image.upload.config.SteemJImageUploadConfig;
import eu.bittrade.libs.steemj.image.upload.models.AccountName;

/**
 * This class tests the basic functionalities of the {@link SteemJImageUpload}
 * class.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SteemJImageUploadTest {
    /**
     * A sample PNG file loaded from the <code>src/test/resources</code> folder.
     */
    private static final File TEST_IMAGE01_JPG_PATH = new File(Resources.getResource("TestImage01.jpg").getFile());
    /**
     * A sample JPG file loaded from the <code>src/test/resources</code> folder.
     */
    private static final File TEST_IMAGE01_PNG_PATH = new File(Resources.getResource("testImage01.png").getFile());

    /**
     * To do not upload files with every test run WireMock is used to mock the
     * target server.
     */
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    /**
     * Initiate the test class.
     */
    @BeforeClass()
    public static void init() {
        SteemJImageUploadConfig.getInstance().setSteemitImagesEndpoint("http://127.0.0.1:8089");
    }

    /**
     * Test the {@link SteemJImageUpload#uploadImage(AccountName, String, File)}
     * method.
     * 
     * @throws IOException
     *             In case something went wrong.
     */
    @Test
    public void uploadTest() throws IOException {
        // Stub for the JPG file:
        stubFor(post(urlPathEqualTo(
                "/steemj/1ceb9383aaa3a84a9145f8c30e9970dc07287560c03810de338d323406c115f36f1e209e075e47f42df2a4fa7d577df23db89e88f775040683b06a4b5d986180cc"))
                        .willReturn(aResponse().withStatus(200)
                                .withBody("{\"url\":\"http://sample-response.com/4a54/name.jpg\"}")));
        // Stub for the PNG file:
        stubFor(post(urlPathEqualTo(
                "/dez1337/1b10d3fc17082df4ab3060dc575714cdf196f41ddb910b3d4fb4d76ace1b783f9b1413ad3faa7c727ed8f6928387a64eef91546a6892853067d2ac87fc6c15ee74"))
                        .willReturn(aResponse().withStatus(200)
                                .withBody("{\"url\":\"http://sample-response.com/4a54/name.png\"}")));

        SteemJImageUpload.uploadImage(new AccountName("steemj"), "5JSk8KCHqCWg3FgQhu5xMNsmshHk9Zfs2oxfpGYrzmUhi3KsEhV",
                TEST_IMAGE01_JPG_PATH);

        SteemJImageUpload.uploadImage(new AccountName("dez1337"), "5JSk8KCHqCWg3FgQhu5xMNsmshHk9Zfs2oxfpGYrzmUhi3KsEhV",
                TEST_IMAGE01_PNG_PATH);
    }
}
