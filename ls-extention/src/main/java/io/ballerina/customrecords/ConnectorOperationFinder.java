package io.ballerina.customrecords;

import io.ballerina.compiler.syntax.tree.NodeVisitor;
import io.ballerina.compiler.syntax.tree.RemoteMethodCallActionNode;
import io.ballerina.compiler.syntax.tree.SyntaxKind;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor to find the base url of connector endpoints.
 */
public class ConnectorOperationFinder extends NodeVisitor {

    List<ConnectorOperation> resources = new ArrayList<>();

    public List<ConnectorOperation> getConnectors() {
        return resources;
    }

    @Override
    public void visit(RemoteMethodCallActionNode remoteMethodCallActionNode) {
        if (remoteMethodCallActionNode.kind() == SyntaxKind.REMOTE_METHOD_CALL_ACTION &&
                (remoteMethodCallActionNode.methodName().toString().equalsIgnoreCase("getRecord") ||
                        remoteMethodCallActionNode.methodName().toString().equalsIgnoreCase("query"))) {
            resources.add(new ConnectorOperation(remoteMethodCallActionNode.lineRange()));
        }
    }
}
