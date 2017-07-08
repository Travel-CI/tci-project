package com.travelci.projects.project.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
public class BranchListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(final List<String> branchList) {
        return String.join(",", branchList);
    }

    @Override
    public List<String> convertToEntityAttribute(final String branches) {
        return Arrays.asList(branches.split(","));
    }
}
