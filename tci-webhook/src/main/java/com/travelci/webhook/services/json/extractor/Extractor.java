package com.travelci.webhook.services.json.extractor;

import com.travelci.webhook.entities.PayLoad;
import com.travelci.webhook.exceptions.ExtractorWrongFormatException;

public interface Extractor {

    boolean jsonHasGoodFormat(String jsonPayLoad);

    PayLoad transformJsonToPayload(String jsonPayLoad) throws ExtractorWrongFormatException;
}
