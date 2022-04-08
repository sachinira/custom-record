package io.ballerina.customrecords;

/**
 *  Request format for JsonToSalesforceBalRecord endpoint.
 */
public class CustomRecordSchemaRequest {
    private String jsonString;
    private boolean isRecordTypeDesc;
    private boolean isClosed;

    public CustomRecordSchemaRequest(String jsonString, boolean isRecordTypeDesc, boolean isClosed) {
        this.jsonString = jsonString;
        this.isRecordTypeDesc = isRecordTypeDesc;
        this.isClosed = isClosed;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public boolean isRecordTypeDesc() {
        return isRecordTypeDesc;
    }

    public void setRecordTypeDesc(boolean recordTypeDesc) {
        isRecordTypeDesc = recordTypeDesc;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
