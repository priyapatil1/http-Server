package response;

import main.Response;
import main.request.Request;
import main.responses.ResourceResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static main.Status.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class ResourceResponseTest {

    private final String publicDirectory = "/Users/priyapatil/cob_spec/public";
    List content = new ArrayList<>();
    private final TestHelper helper = new TestHelper();
    private Request getImageGif;
    private Request getTextFile;
    private Request getImageJPEG;
    private Request getImagePNG;
    private ResourceResponse resourceResponse;
    private Request getPartial;
    private Request getPartialTwo;
    private Request getPartialThree;
    private Request getPartialFour;
    private Request getPartialFive;
    private Request patchWithMatch;
    private Request misMatchedPatch;

    @Before
    public void setUp() throws IOException {
        getImageGif = helper.create("GET /image.gif");
        getTextFile = helper.create("GET /text-file.txt");
        getImageJPEG = helper.create("GET /image.jpeg");
        getImagePNG = helper.create("GET /image.png");
        getPartial = helper.createPartial("GET /partial_content.txt", 7);
        getPartialTwo = helper.createPartial("GET /partial_content.txt", 9);
        getPartialThree = helper.createPartialEnd("GET /partial_content.txt", 3);
        getPartialFour = helper.createPartialBeginning("GET /partial_content.txt", 3);
        getPartialFive = helper.createPartialBeginning("GET /partial_content.txt", 5);
        patchWithMatch = helper.requestWithEtag("PATCH /patch-content.txt", "5c36acad75b78b82be6d9cbbd6143ab7e0cc04b0");
        resourceResponse = new ResourceResponse(publicDirectory, content);
    }

    @After
    public void tearDown() throws IOException {
        File[] directory = new File(publicDirectory).listFiles();
        for (File file : directory) {
            if (file.getName().equals("patch-content.txt")) {
                overWriteFileContents(file);
            }
        }
    }

    private void overWriteFileContents(File file) throws IOException {
        FileWriter writer = new FileWriter(file, false);
        writer.write("default content");
        writer.close();
    }

    @Test
    public void OKStatusForImageGifFile() {
        Response response = resourceResponse.get(getImageGif);
        assertThat(response.getHeader(), containsString(OK.get()));
    }

    @Test
    public void addsImageGIFContentsToBody() {
        Response response = resourceResponse.get(getImageGif);
        assertTrue(response.getBody().length != 0);
    }

    @Test
    public void OKStatusForTextFile() {
        Response response = resourceResponse.get(getTextFile);
        assertThat(response.getHeader(), containsString(OK.get()));
    }

    @Test
    public void addsTextFileContentsToBody() {
        Response response = resourceResponse.get(getTextFile);
        assertTrue(response.getBody().length != 0);
    }

    @Test
    public void OKStatusForJPEGFile() {
        Response response = resourceResponse.get(getImageJPEG);
        assertThat(response.getHeader(), containsString(OK.get()));
    }

    @Test
    public void addsImagePNGContentToBody() {
        Response response = resourceResponse.get(getImagePNG);
        assertTrue(response.getBody().length != 0);
    }

    @Test
    public void headerContainsGifMediaType() {
        Response response = resourceResponse.get(getImageGif);
        assertThat(response.getHeader(), containsString("Content-Type: image/gif\n"));
    }

    @Test
    public void headerContainsJpegMediaType() {
        Response response = resourceResponse.get(getImageJPEG);
        assertThat(response.getHeader(), containsString("Content-Type: image/jpeg\n"));
    }

    @Test
    public void headerContainsPlainTextMediaType() {
        Response response = resourceResponse.get(getTextFile);
        assertThat(response.getHeader(), containsString("Content-Type: text/plain\n"));
    }

    @Test
    public void partialRequestReturnsPartialStatus() {
        Response response = resourceResponse.get(getPartial);
        assertThat(response.getHeader(), containsString(PARTIAL.get()));
    }

    @Test
    public void partialRequestReturnsHeaderWithCorrectBytes() {
        Response response = resourceResponse.get(getPartial);
        assertTrue(response.getBody().length == 8);
    }

    @Test
    public void anotherPartialRequestReturnsHeaderWithCorrectBytes() {
        Response response = resourceResponse.get(getPartialTwo);
        assertTrue(response.getBody().length == 10);
    }
    
    @Test
    public void partialAskingForBytesFromEndOfFile() {
        Response response = resourceResponse.get(getPartialThree);
        assertTrue(response.getBody().length == 3);
    }

    @Test
    public void partialAskingForBytesFromBeginning() {
        Response response = resourceResponse.get(getPartialFour);
        assertTrue(response.getBody().length == 74);
    }

    @Test
    public void anotherPartialAskingForBytesFromBeginning() {
        Response response = resourceResponse.get(getPartialFive);
        assertTrue(response.getBody().length == 72);
    }

    @Test
    public void patchRequestReturnsNoContentMessage() throws NoSuchAlgorithmException, IOException {
        resourceResponse = new ResourceResponse(publicDirectory, content);
        Response response = resourceResponse.patch(patchWithMatch);
        assertThat(response.getHeader(), containsString(NO_CONTENT.get()));
    }

    @Test
    public void setsETagHeader() throws NoSuchAlgorithmException, IOException {
        resourceResponse = new ResourceResponse(publicDirectory, content);
        Response response = resourceResponse.patch(patchWithMatch);
        assertThat(response.getHeader(), containsString("Etag: 5c36acad75b78b82be6d9cbbd6143ab7e0cc04b0"));
    }

    @Test
    public void updatesFileIfETagsMatch() throws NoSuchAlgorithmException, IOException {
        resourceResponse = new ResourceResponse(publicDirectory, content);
        patchWithMatch = helper.requestWithEtag("PATCH /patch-content.txt", "dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec");
        Response getResponse = resourceResponse.patch(patchWithMatch);
        assertEquals("patched content", new String(getResponse.getBody()));
    }

    @Test
    public void eTagsDoNotMatchNoUpdate() throws NoSuchAlgorithmException, IOException {
        resourceResponse = new ResourceResponse(publicDirectory, content);
        misMatchedPatch = helper.requestWithEtag("PATCH /patch-content.txt", "kjsdljsbdlj");
        Response getResponse = resourceResponse.patch(misMatchedPatch);
        assertEquals("default content", new String(getResponse.getBody()));
    }
}
