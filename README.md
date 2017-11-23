<center>

![SteemJImageUploadIcon](https://steemitimages.com/DQmfWn1z6HwwxfaWhrpY28MYh43wEEyNsV25GnGSGZUDo2U/SteemJImageSteemFriendlyWithUpdatetBanner.png)
  
</center>

This project is a sub-project of [SteemJ](https://github.com/marvin-we/steem-java-api-wrapper) - The Steem API for Java. 

[SteemJ Image Upload](https://github.com/marvin-we/steemj-image-upload) allows to upload images to ([https://steemitimages.com](https://steemitimages.com)) out of your Java application.

The project has been initialized by <a href="https://steemit.com/@dez1337">dez1337 on steemit.com</a>.

# Binaries
SteemJ Image Upload binaries are pushed into the maven central repository and can be integrated with a bunch of build management tools like Maven.

## Maven
File: <i>pom.xml</i>
```Xml
<dependency>
    <groupId>eu.bittrade.libs</groupId>
    <artifactId>steemj-image-upload</artifactId>
    <version>1.0.0pre1</version>
</dependency>
```

# How to build the project
The project requires Maven and Java to be installed on your machine. It can be build with the default maven command:

> mvn clean package

The resulting JAR can be found in the target directory as usual. 

# Bugs and Feedback and Communication
For bugs or feature requests please create a [GitHub Issue](https://github.com/marvin-we/steem-java-api-wrapper/issues). 

For general discussions or questions you can also:
* Post your questions in the [Discord Java Channel](https://discord.gg/fsJjr3Q)
* Reply to one of the SteemJ update posts on [Steemit.com](https://steemit.com/@dez1337)
* Contact me on [steemit.chat](https://steemit.chat/channel/dev)

# Example
SteemJ Image Upload is really easy to use. The following code shows a full sample:

```Java
import java.io.File;
import java.io.IOException;
import java.net.URL;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.image.upload.SteemJImageUpload;

public class ExampleApplication {
    public static void main(String args[]) {
        AccountName myAccount = new AccountName("dez1337");
        // Please be informed that this is not the correct private key of dez1337.
        String myPrivatePostingKeyAsWif = "5KMamixsFoUkdlz7sNG4RsyaKQyJMBBqrdT6y54qr4cdVhU9rz7";
        File imageToUpload = new File("C:/Users/dez1337/Desktop/SteemJImageBanner.png");

        try {
            URL myImageUrl = SteemJImageUpload.uploadImage(myAccount, myPrivatePostingKeyAsWif, imageToUpload);
            System.out.println("The image has been uploaded and is reachable using the URL: " + myImageUrl);
        } catch (IOException e) {
            System.out.println("There was a problem uploading the image.");
            e.printStackTrace();
        }
    }
}
```

An output of the code above will look like this:
> The image has been uploaded and is reachable using the URL:  https://steemitimages.com/DQmfWn1z6HwwxfaWhrpY28MYh43wEEyNsV25GnGSGZUDo2U/SteemJImageSteemFriendlyWithUpdatetBanner.png
