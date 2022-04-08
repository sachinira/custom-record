package io.ballerina.customrecords;

import io.ballerina.tools.text.LineRange;

/**
 * Represents a connector operation resource.
 */
public class ConnectorOperation {
    private LineRange lineRange;

    public ConnectorOperation(LineRange lineRange) {
        this.lineRange = lineRange;
    }

    public LineRange getLineRange() {
        return lineRange;
    }

    public void setLineRange(LineRange lineRange) {
        this.lineRange = lineRange;
    }
}
