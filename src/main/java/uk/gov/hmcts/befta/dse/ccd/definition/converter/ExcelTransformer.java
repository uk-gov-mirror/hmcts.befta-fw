package uk.gov.hmcts.befta.dse.ccd.definition.converter;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Streams;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

public class ExcelTransformer {

    private static final String INPUT_FILE_PATH = "/Users/dev/code/ccd/ccd-testing-support/src/main/resources/CCD_CaseRoleDemo_v38.xlsx";
    private static final String OUTPUT_FOLDER = "definition_json";

    private ObjectWriter writer = new ObjectMapper().writer(new DefaultPrettyPrinter());
    private SheetTransformer sheetTransformer = new SheetTransformer();
    private HashMap<String,ArrayNode> defFileMap = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        ExcelTransformer excelTransformer = new ExcelTransformer();

        //        excelTransformer.xlxsToJson(INPUT_FILE_PATH, OUTPUT_FOLDER);

        excelTransformer.parseXlxs(INPUT_FILE_PATH);
        excelTransformer.writeToJson(OUTPUT_FOLDER);
    }


    /**
     * Convert xlsx to json in a single function
     * @param xlxsFilePath
     * @param outputFolderPath
     */
    private void xlxsToJson(String xlxsFilePath, String outputFolderPath) {
        String jurisdiction = null;
        Workbook workbook = new ExcelReader(xlxsFilePath).parseXLXS();
        SheetTransformer sheetTransformer = new SheetTransformer();
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            ArrayNode sheetObject = sheetTransformer.transformToJson(sheet);

            String sheetName = workbook.getSheetAt(i).getSheetName();
            if (sheetName.equals("Jurisdiction")) {
                jurisdiction = sheetObject.get(i).get("ID").asText();
            }

            try {
                if (i == 0){
                    outputFolderPath = outputFolderPath + File.separator + jurisdiction;
                    createDirectoryHierarchy(outputFolderPath);
                }

                writer.writeValue(new File(outputFolderPath + File.separator + sheetName + ".json"), sheetObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Parse an xlxs document into a map of excel sheets k=sheet name v=object representation of that sheet.
     * Saving into a Map object will allow reuse of this object in the future
     * @param xlxsPath
     * @return HashMap<String,ArrayNode>
     */
    private HashMap parseXlxs(String xlxsPath) {
        Workbook workbook = new ExcelReader(xlxsPath).parseXLXS();

        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);

            String sheetName = workbook.getSheetAt(i).getSheetName();
            ArrayNode sheetObject = sheetTransformer.transformToJson(sheet);

            defFileMap.put(sheetName, sheetObject);
        }
        return defFileMap;
    }

    /**
     * use HashMap<String,ArrayNode> defFileMap to write out json files, a file for each def file sheet.
     * List output folder, jurisdiction subfolder is automatically created
     * @param outputFilePath
     */
    private void writeToJson(String outputFilePath) {
        String jurisdiction = defFileMap.get("Jurisdiction").get(0).get("ID").asText();
        String outputFolder = outputFilePath + File.separator + jurisdiction;
        createDirectoryHierarchy(outputFolder);

        defFileMap.forEach((key, value) -> {
            try {
                writer.writeValue(new File(outputFolder + File.separator + key + ".json"), value);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void createDirectoryHierarchy(String path){
        File dir = new File(path);

        if (!dir.mkdirs()){
            System.out.println("Could not create directory for " + path);
        }
    }

}
