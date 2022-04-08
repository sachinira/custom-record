package io.ballerina.customrecords;

import io.ballerina.customrecords.exceptions.CustomRecordGeneratorException;
import io.ballerina.customrecords.utils.JsonToRecordConverter;
import org.ballerinalang.formatter.core.FormatterException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class CustomRecordServiceTest {
//    @Test
//    public void testFieldExtraction() throws IOException, CustomRecordGeneratorException {
//        String file = "src/test/resources/fields.json";
//        String json = new String(Files.readAllBytes(Paths.get(file)));
//
//        List<SalesforceFieldItemPojo> fieldList = salesforceFieldExtractor(json);
//        fieldList.forEach((item) -> {
//        });
//    }

    @Test
    public void testGetRecordTypeGeneration() throws IOException, FormatterException, CustomRecordGeneratorException {
        String file = "src/test/resources/fields.json";
        String json = new String(Files.readAllBytes(Paths.get(file)));
        CustomRecordSchemaResponse response = JsonToRecordConverter.salesforceSchemaConverter(json, true, false);
        System.out.println(response.getCodeBlock());
    }
}
