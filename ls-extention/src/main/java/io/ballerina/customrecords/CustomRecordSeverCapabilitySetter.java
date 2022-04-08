package io.ballerina.customrecords;

import org.ballerinalang.annotation.JavaSPIService;
import org.ballerinalang.langserver.commons.registration.BallerinaServerCapabilitySetter;

import java.util.Optional;

/**
 * Capability setter for the {@link CustomRecordService}.
 */
@JavaSPIService("org.ballerinalang.langserver.commons.registration.BallerinaServerCapabilitySetter")
public class CustomRecordSeverCapabilitySetter extends BallerinaServerCapabilitySetter<CustomRecordServerCapabilities> {

    @Override
    public Optional<CustomRecordServerCapabilities> build() {
        CustomRecordServerCapabilities capabilities = new CustomRecordServerCapabilities();
        capabilities.setGetResults(true);
        capabilities.setGetRemoteFunctionCalls(true);
        return Optional.of(capabilities);
    }

    @Override
    public String getCapabilityName() {
        return CustomRecordConstants.CAPABILITY_NAME;
    }

    @Override
    public Class<CustomRecordServerCapabilities> getCapability() {
        return CustomRecordServerCapabilities.class;
    }
}
