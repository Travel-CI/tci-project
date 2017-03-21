package com.travelci.bitbuckets;

import ch.qos.logback.core.CoreConstants;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Created by Julien on 20/03/2017.
 */
//@JsonRootName(value = "actor")
public class Bitbuckets {

    public int id;
    private String display_name;


    @JsonGetter("display_name")
    public String getTheName() {
         return display_name;
    }






}
