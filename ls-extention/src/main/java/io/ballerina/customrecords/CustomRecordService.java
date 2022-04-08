package io.ballerina.customrecords;

import io.ballerina.compiler.api.SemanticModel;
import io.ballerina.compiler.syntax.tree.SyntaxTree;
import io.ballerina.customrecords.exceptions.CustomRecordGeneratorException;
import io.ballerina.customrecords.utils.JsonToRecordConverter;
import io.ballerina.projects.Document;
import io.ballerina.projects.Module;
import io.ballerina.tools.text.LineRange;
import org.ballerinalang.annotation.JavaSPIService;
import org.ballerinalang.formatter.core.FormatterException;
import org.ballerinalang.langserver.commons.service.spi.ExtendedLanguageServerService;
import org.ballerinalang.langserver.commons.workspace.WorkspaceManager;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;
import org.eclipse.lsp4j.services.LanguageServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static io.ballerina.customrecords.CustomRecordConstants.CAPABILITY_NAME;
import static io.ballerina.customrecords.CustomRecordConstants.SUCCESS;

/**
 * The extended service for the custom record generation.
 */
@JavaSPIService("org.ballerinalang.langserver.commons.service.spi.ExtendedLanguageServerService")
@JsonSegment("customRecordGenerator")
public class CustomRecordService implements ExtendedLanguageServerService {

    private WorkspaceManager workspaceManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomRecordService.class);

    @Override
    public void init(LanguageServer langServer, WorkspaceManager workspaceManager) {
        this.workspaceManager = workspaceManager;
    }

    @Override
    public Class<?> getRemoteInterface() {
        return getClass();
    }

    @JsonRequest
    public CompletableFuture<List<ConnectorOperationFinderResponse>>
        getConnectorFunctionCalls(ConnectorOperationFinderRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            List<ConnectorOperationFinderResponse> callFinderResponses = new ArrayList<>();

            String fileUri = request.getDocumentIdentifier().getUri();
            Path path = Path.of(fileUri);

            Optional<SemanticModel> semanticModel = this.workspaceManager.semanticModel(path);
            Optional<Module> module = this.workspaceManager.module(path);

            if (semanticModel.isEmpty() || module.isEmpty()) {
                return callFinderResponses;
            }

            ConnectorOperationFinder nodeVisitor = new ConnectorOperationFinder();

            Optional<Document> document = this.workspaceManager.document(path);

            if (document.isEmpty()) {
                return callFinderResponses;
            }

            SyntaxTree syntaxTree = document.get().syntaxTree();
            syntaxTree.rootNode().accept(nodeVisitor);
            List<ConnectorOperation> connectorCallList = nodeVisitor.getConnectors();

            for (ConnectorOperation connectorCall : connectorCallList) {
                LineRange range = connectorCall.getLineRange();
                Range lineRange = new Range(new Position(range.startLine().line(), range.startLine().offset()),
                        new Position(range.endLine().line(), range.endLine().offset()));
                ConnectorOperationFinderResponse response = new ConnectorOperationFinderResponse();
                response.setConnectorCallPos(lineRange);
                response.setType(SUCCESS);
                response.setMessage(SUCCESS);
                callFinderResponses.add(response);
            }
            return callFinderResponses;
        });
    }

    @JsonRequest
    public CompletableFuture<CustomRecordSchemaResponse> salesforceSObjectSchemaToRecord
            (CustomRecordSchemaRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            CustomRecordSchemaResponse response = null;
            String jsonString = request.getJsonString();
            boolean isRecordTypeDesc = request.isRecordTypeDesc();
            boolean isClosed = request.isClosed();
            try {
                response = JsonToRecordConverter.salesforceSchemaConverter(jsonString, isRecordTypeDesc, isClosed);
            } catch (IOException | FormatterException | CustomRecordGeneratorException e) {
                response = new CustomRecordSchemaResponse();
                response.setCodeBlock("");
            }
            return response;
        });
    }

    @Override
    public String getName() {
        return CAPABILITY_NAME;
    }
}
