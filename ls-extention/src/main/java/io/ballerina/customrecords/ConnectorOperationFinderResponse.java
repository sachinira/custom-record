package io.ballerina.customrecords;

import org.eclipse.lsp4j.Range;

/**
 * Response format for custom connector request.
 */
public class ConnectorOperationFinderResponse {
    private Range connectorCallPos;
    private String type;
    private String message;
//    private String name;

    public ConnectorOperationFinderResponse() {
    }

    public ConnectorOperationFinderResponse(Range connectorCallPos, String type, String message) {
        this.connectorCallPos = connectorCallPos;
        this.type = type;
        this.message = message;
    }

    public Range getConnectorCallPos() {
        return connectorCallPos;
    }

    public void setConnectorCallPos(Range connectorCallPos) {
        this.connectorCallPos = connectorCallPos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
