package io.ballerina.customrecords;

import org.ballerinalang.annotation.JavaSPIService;
import org.ballerinalang.langserver.commons.registration.BallerinaClientCapabilitySetter;

/**
 * Client Capability setter for the {@link CustomRecordService}.
 */
@JavaSPIService("org.ballerinalang.langserver.commons.registration.BallerinaClientCapabilitySetter")
public class CustomRecordClientCapabilitySetter extends
        BallerinaClientCapabilitySetter<CustomRecordClientCapabilities> {
    @Override
    public String getCapabilityName() {
        return CustomRecordConstants.CAPABILITY_NAME;
    }

    @Override
    public Class<CustomRecordClientCapabilities> getCapability() {
        return CustomRecordClientCapabilities.class;
    }
}
