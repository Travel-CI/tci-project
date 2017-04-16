package com.travelci.webhook.services.json.extractor;

import com.travelci.webhook.entities.PayLoad;

public interface Extractor {

    boolean jsonHasGoodFormat(String jsonPayLoad);

    PayLoad transformJsonToPayload(String jsonPayLoad);
}
