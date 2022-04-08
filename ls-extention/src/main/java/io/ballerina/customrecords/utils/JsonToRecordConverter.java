package io.ballerina.customrecords.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ballerina.compiler.syntax.tree.AbstractNodeFactory;
import io.ballerina.compiler.syntax.tree.ImportDeclarationNode;
import io.ballerina.compiler.syntax.tree.ModuleMemberDeclarationNode;
import io.ballerina.compiler.syntax.tree.ModulePartNode;
import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.NodeFactory;
import io.ballerina.compiler.syntax.tree.NodeList;
import io.ballerina.compiler.syntax.tree.RecordFieldNode;
import io.ballerina.compiler.syntax.tree.RecordTypeDescriptorNode;
import io.ballerina.compiler.syntax.tree.Token;
import io.ballerina.compiler.syntax.tree.TypeDefinitionNode;
import io.ballerina.customrecords.CustomRecordSchemaResponse;
import io.ballerina.customrecords.exceptions.CustomRecordGeneratorException;
import io.ballerina.customrecords.utils.pojo.SalesforceFieldItemPojo;
import io.ballerina.customrecords.utils.pojo.SalesforceRecord;
import org.ballerinalang.formatter.core.Formatter;
import org.ballerinalang.formatter.core.FormatterException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.ballerina.compiler.syntax.tree.AbstractNodeFactory.createEmptyNodeList;
import static io.ballerina.compiler.syntax.tree.AbstractNodeFactory.createIdentifierToken;
import static io.ballerina.compiler.syntax.tree.AbstractNodeFactory.createNodeList;
import static io.ballerina.compiler.syntax.tree.AbstractNodeFactory.createToken;
import static io.ballerina.compiler.syntax.tree.NodeFactory.createRecordFieldNode;
import static io.ballerina.compiler.syntax.tree.NodeFactory.createRecordTypeDescriptorNode;
import static io.ballerina.compiler.syntax.tree.NodeFactory.createTypeDefinitionNode;
import static io.ballerina.compiler.syntax.tree.SyntaxKind.CLOSE_BRACE_TOKEN;
import static io.ballerina.compiler.syntax.tree.SyntaxKind.OPEN_BRACE_TOKEN;
import static io.ballerina.compiler.syntax.tree.SyntaxKind.PUBLIC_KEYWORD;
import static io.ballerina.compiler.syntax.tree.SyntaxKind.QUESTION_MARK_TOKEN;
import static io.ballerina.compiler.syntax.tree.SyntaxKind.RECORD_KEYWORD;
import static io.ballerina.compiler.syntax.tree.SyntaxKind.SEMICOLON_TOKEN;
import static io.ballerina.compiler.syntax.tree.SyntaxKind.TYPE_KEYWORD;
import static io.ballerina.customrecords.utils.Constants.SF_WHITESPACE_TYPE;
import static io.ballerina.customrecords.utils.ConverterUtils.convertSalesforceTypeToBallerina;
import static io.ballerina.customrecords.utils.ConverterUtils.escapeIdentifier;

/**
 * Implements functionality to convert Json (object or schema) to a ballerina record.
 */
public class JsonToRecordConverter {
    private JsonToRecordConverter() {}

    /**
     * This method takes in a json string and returns the Ballerina code block for createRecord/updateRecord operation.
     *
     * @param jsonString Json string for the schema
     * @param isRecordTypeDesc To denote final record, a record type descriptor
     * @param isClosed To denote the whether the response record is closed
     * @return {@link String} Ballerina code block
     * @throws IOException In case of Json parse error
     * @throws FormatterException In case of invalid syntax
     */
    public static CustomRecordSchemaResponse salesforceSchemaConverter(String jsonString, boolean isRecordTypeDesc,
                                                                       boolean isClosed)
            throws IOException, FormatterException, CustomRecordGeneratorException {
        SalesforceRecord recordData = salesforceRecordExtractor(jsonString);
        String name = ((recordData.getRecordName() != null) && !recordData.getRecordName().equals("")) ?
                recordData.getRecordName() : "NewRecord";
        CustomRecordSchemaResponse response = new CustomRecordSchemaResponse();
        NodeList<ImportDeclarationNode> imports = createEmptyNodeList();

//        if (isRecordTypeDesc) {
            List<Node> recordFieldList = new ArrayList<>();
            recordData.getFields().forEach((fieldData) -> {
                RecordFieldNode recordFieldNode = null;
                String dataType = convertSalesforceTypeToBallerina(fieldData.getType());
                String fieldName = escapeIdentifier(fieldData.getName());
                boolean isNillable = fieldData.isNillable();

                // TODO: Refactor
                if (isNillable) {
                    recordFieldNode = createRecordFieldNode(null, null,
                            createIdentifierToken(dataType +
                                    createToken(QUESTION_MARK_TOKEN) + SF_WHITESPACE_TYPE),
                            createIdentifierToken(fieldName),
                            createToken(QUESTION_MARK_TOKEN),
                            createToken(SEMICOLON_TOKEN));
                } else {
                    recordFieldNode = createRecordFieldNode(null, null,
                            createIdentifierToken(dataType + SF_WHITESPACE_TYPE),
                            createIdentifierToken(fieldName),
                            createToken(QUESTION_MARK_TOKEN),
                            createToken(SEMICOLON_TOKEN));
                }
                recordFieldList.add(recordFieldNode);
            });
            NodeList<Node> recordFilds = createNodeList(recordFieldList);
            RecordTypeDescriptorNode customRecord = createRecordTypeDescriptorNode(
                    createToken(RECORD_KEYWORD),
                    createToken(OPEN_BRACE_TOKEN), recordFilds, null,
                    createToken(CLOSE_BRACE_TOKEN));
            TypeDefinitionNode typeDefinitionNode = createTypeDefinitionNode(null,
                    createToken(PUBLIC_KEYWORD),
                    createToken(TYPE_KEYWORD),
                    createIdentifierToken(name),
                    customRecord,
                    createToken(SEMICOLON_TOKEN));
            NodeList<ModuleMemberDeclarationNode> moduleMembers = AbstractNodeFactory.
                    createNodeList(typeDefinitionNode);
            Token eofToken = AbstractNodeFactory.createIdentifierToken("");
            ModulePartNode modulePartNode = NodeFactory.createModulePartNode(imports, moduleMembers, eofToken);
            response.setCodeBlock(Formatter.format(modulePartNode.syntaxTree()).toSourceCode());
//        } else {
//            // TODO: Check this
//             // Sets generated type definition code block
//            NodeList<ModuleMemberDeclarationNode> moduleMembers = createNodeList(
//                    typeDefinitionNodeList.toArray(new TypeDefinitionNode[0]));
//            Token eofToken = createIdentifierToken("");
//            ModulePartNode modulePartNode = NodeFactory.createModulePartNode(imports, moduleMembers, eofToken);
//            response.setCodeBlock(Formatter.format(modulePartNode.syntaxTree()).toSourceCode());
//        }
        return response;
    }

    public static SalesforceRecord salesforceRecordExtractor(String jsonString) throws
            CustomRecordGeneratorException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SalesforceRecord record = new SalesforceRecord();

        ObjectNode node = objectMapper.readValue(jsonString, ObjectNode.class);
        if (node.has("name")) {
            record.setRecordName(node.get("name").textValue());
        }

        JsonNode fields = objectMapper.readTree(jsonString).at("/fields");
        List<SalesforceFieldItemPojo> fieldItemObjects =  new ArrayList<>();
        if (fields.isArray()) {
            ArrayNode items = (ArrayNode) fields;
            for (JsonNode item : items) {
                SalesforceFieldItemPojo fieldItemObject = null;
                try {
                    fieldItemObject = objectMapper.readValue(item.toString(), SalesforceFieldItemPojo.class);
                } catch (JsonProcessingException e) {
                    throw new CustomRecordGeneratorException(e.getMessage());
                }
                fieldItemObjects.add(fieldItemObject);
            }
            record.setFields(fieldItemObjects);
        } else {
            throw new CustomRecordGeneratorException("Unsupported data");
        }
        return record;
    }
}
