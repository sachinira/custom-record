package io.ballerina.customrecords;

/**
 * Response format for salesforceSObjectSchemaToRecord endpoint.
 **/
public class CustomRecordSchemaResponse {
    private String codeBlock;

    public String getCodeBlock() {
        return codeBlock;
    }

    public void setCodeBlock(String codeBlock) {
        this.codeBlock = codeBlock;
    }
}

