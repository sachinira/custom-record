package io.ballerina.customrecords;

import org.eclipse.lsp4j.TextDocumentIdentifier;

/**
 * The request format for custom record request from frontend.
 */
public class ConnectorOperationFinderRequest {
    private TextDocumentIdentifier documentIdentifier;

    public TextDocumentIdentifier getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(TextDocumentIdentifier documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }
}
