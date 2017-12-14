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

import java.io.IOException;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;

import eu.bittrade.libs.steemj.image.upload.config.SteemJImageUploadConfig;

/**
 * 
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class HttpClientRequestInitializer implements HttpRequestInitializer {

    @Override
    public void initialize(HttpRequest request) throws IOException {
        request.setConnectTimeout(SteemJImageUploadConfig.getInstance().getConnectTimeout());
        request.setReadTimeout(SteemJImageUploadConfig.getInstance().getReadTimeout());
        request.setNumberOfRetries(0);
    }
}
