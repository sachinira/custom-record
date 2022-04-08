package io.ballerina.customrecords;

import org.ballerinalang.langserver.commons.registration.BallerinaClientCapability;

/**
 * Client capabilities for the custom records service.
 * The methods which are exposed to the client from the extension.
 */
public class CustomRecordClientCapabilities extends BallerinaClientCapability {
    private boolean getResults;
    private boolean getRemoteFunctionCalls;

    public CustomRecordClientCapabilities() {
        super(CustomRecordConstants.CAPABILITY_NAME);
    }

    public boolean isGetResults() {
        return getResults;
    }

    public void setGetResults(boolean getResults) {
        this.getResults = getResults;
    }

    public boolean isGetRemoteFunctionCalls() {
        return getRemoteFunctionCalls;
    }

    public void setGetRemoteFunctionCalls(boolean getRemoteFunctionCalls) {
        this.getRemoteFunctionCalls = getRemoteFunctionCalls;
    }
}
