package io.ballerina.customrecords.utils.pojo;

import java.util.List;

/**
 * A POJO class representing the extracted data for generating Ballerina record type.
 */
public class SalesforceRecord {
    String recordName;
    List<SalesforceFieldItemPojo> fields;

    public SalesforceRecord() {

    }

    public SalesforceRecord(String recordName, List<SalesforceFieldItemPojo> fields) {
        this.recordName = recordName;
        this.fields = fields;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public List<SalesforceFieldItemPojo> getFields() {
        return fields;
    }

    public void setFields(List<SalesforceFieldItemPojo> fields) {
        this.fields = fields;
    }
}
