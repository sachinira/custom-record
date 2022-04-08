module io.ballerina.customrecords.customRecordGenerator  {
    requires io.ballerina.lang;
    requires org.eclipse.lsp4j.jsonrpc;
    requires io.ballerina.language.server.commons;
    requires io.ballerina.formatter.core;
    requires org.slf4j;
    requires org.eclipse.lsp4j;
    requires com.github.mustachejava;
    requires io.ballerina.parser;
    requires io.ballerina.tools.api;
    requires httpclient;
    requires httpcore;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires json;
//    requires io.ballerina.language.server.commons;

    exports io.ballerina.customrecords;
    exports io.ballerina.customrecords.utils;
    exports io.ballerina.customrecords.exceptions;
    exports io.ballerina.customrecords.utils.pojo;

}