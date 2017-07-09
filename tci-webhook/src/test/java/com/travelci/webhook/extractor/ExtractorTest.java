package com.travelci.webhook.extractor;

import com.travelci.webhook.extractor.exceptions.ExtractorWrongFormatException;
import com.travelci.webhook.payload.entities.PayLoad;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtractorTest {

    private BitbucketExtractorImpl bitbucketExtractor;
    private String goodJsonPayLoad;

    @Before
    public void setup() {

        bitbucketExtractor = new BitbucketExtractorImpl(
            Arrays.asList("{\"bitbucket\":{", "\"commit\":"),
            "/bitbucket/repository/url",
            "/bitbucket/branch/name",
            "/bitbucket/commit/author",
            "/bitbucket/commit/hash",
            "/bitbucket/commit/message",
            Validation.buildDefaultValidatorFactory().getValidator()
        );

        goodJsonPayLoad = "{\"bitbucket\":" +
            "{\"repository\":{\"url\":\"https://github.com/travelci/test.git\"}," +
            "\"branch\":{\"name\":\"master\"}," +
            "\"commit\":" +
                "{\"author\":\"mboisnard\"," +
                "\"hash\":\"aaaa\"," +
                "\"message\":\"first commit\"," +
                "\"date\":\"\"}}}";
    }

    @Test
    public void shouldReturnTrueWhenJsonPayLoadMatchWithIdentifiers() {

        final boolean hasGoodFormat = bitbucketExtractor.jsonHasGoodFormat(goodJsonPayLoad);
        assertThat(hasGoodFormat).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenJsonPayLoadNotMatchWithAllIdentifiers() {

        final String jsonPayLoad = "{\"github\":" + // Bad Identifier
            "{\"repository\":{\"url\":\"https://github.com/travelci/test.git\"}," +
            "\"branch\":{\"name\":\"master\"}," +
            "\"commit\":" + // Good Identifier
                "{\"author\":\"mboisnard\"," +
                "\"hash\":\"aaaa\"," +
                "\"message\":\"first commit\"," +
                "\"date\":\"\"}}}";

        final boolean hasGoodFormat = bitbucketExtractor.jsonHasGoodFormat(jsonPayLoad);

        assertThat(hasGoodFormat).isFalse();
    }

    @Test
    public void shouldReturnFalseWhenJsonPayLoadMatchWithNoOneIdentifiers() {

        final String jsonPayLoad = "{\"github\":" + // Bad Identifier
            "{\"repository\":{\"url\":\"https://github.com/travelci/test.git\"}," +
            "\"branch\":{\"name\":\"master\"}," +
            "\"message\":" + // Bad Identifier
            "{\"author\":\"mboisnard\"," +
            "\"hash\":\"aaaa\"," +
            "\"message\":\"first commit\"," +
            "\"date\":\"\"}}}";
        final boolean hasGoodFormat = bitbucketExtractor.jsonHasGoodFormat(jsonPayLoad);

        assertThat(hasGoodFormat).isFalse();
    }

    @Test
    public void shouldReturnPayLoadWhenTransformJsonToPayLoadWithGoodFormat()
        throws ExtractorWrongFormatException {

        final PayLoad payLoad = bitbucketExtractor.transformJsonToPayload(goodJsonPayLoad);

        assertThat(payLoad).isNotNull();
        assertThat(payLoad.getRepositoryUrl()).isEqualTo("https://github.com/travelci/test.git");
        assertThat(payLoad.getBranchName()).isEqualTo("master");
        assertThat(payLoad.getCommitAuthor()).isEqualTo("mboisnard");
        assertThat(payLoad.getCommitHash()).isEqualTo("aaaa");
        assertThat(payLoad.getCommitMessage()).isEqualTo("first commit");
    }

    @Test(expected = ExtractorWrongFormatException.class)
    public void shouldThrowExceptionWhenConstraintsAreNotRespectedWhenTransformJsonToPayLoad()
        throws ExtractorWrongFormatException {

        final String jsonPayLoad = "{\"bitbucket\":" +
            "{\"repository\":{\"url\":\"https://github.com/travelci/test.git\"}," +
            "\"branch\":{\"name\":\"master\"}," +
            "\"commit\":" +
                "{\"author\":\"\"," + // Empty Field
                "\"hash\":\"aaaa\"," +
                "\"message\":\"first commit\"," +
                "\"date\":\"\"}}}";

        bitbucketExtractor.transformJsonToPayload(jsonPayLoad);
    }

    @Test(expected = ExtractorWrongFormatException.class)
    public void shouldThrowExceptionWhenJsonHadUnknownFieldsWhenTransformJsonToPayLoad()
        throws ExtractorWrongFormatException {

        final String jsonPayLoad = "{\"bitbucket\":" +
            "{\"repository\":{\"url\":\"https://github.com/travelci/test.git\"}," +
            "\"branch\":{\"name\":\"master\"}," +
            "\"commit\":" +
            "{\"author\":\"mboisnard\"," +
            "\"test1\":\"aaaa\"," + // Unknown field
            "\"message\":\"first commit\"," +
            "\"date\":\"\"}}}";

        bitbucketExtractor.transformJsonToPayload(jsonPayLoad);
    }
}
