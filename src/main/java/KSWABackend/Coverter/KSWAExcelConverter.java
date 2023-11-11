package KSWABackend.Coverter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import KSWABackend.Model.KSWAChildren;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class KSWAExcelConverter {
    public static void main(String[] args) {
        List<KSWAChildren> childrenList = new ArrayList<>();
        /*
         * TODO: Take excel from ui
         */
        writeExcelFile(childrenList, "output.xlsx");

        List<KSWAChildren> readChildrenList = readExcelFile("output.xlsx");
        for (KSWAChildren child : readChildrenList) {
            System.out.println(child);
        }
    }

    public static void writeExcelFile(List<KSWAChildren> childrenList, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        try {
            Sheet sheet = workbook.createSheet("KSWAChildren");

            int rowIdx = 0;
            for (KSWAChildren child : childrenList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(child.getChprename());
                row.createCell(1).setCellValue(child.getChname());
                row.createCell(2).setCellValue(child.getChbirthday());
                String subjects = String.join(",", child.getChsubjects());
                row.createCell(3).setCellValue(subjects);
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<KSWAChildren> readExcelFile(String filePath) {
        List<KSWAChildren> childrenList = new ArrayList<>();

        Workbook workbook = null;
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                String chprename = row.getCell(0).getStringCellValue();
                String chname = row.getCell(1).getStringCellValue();
                String chbirthday = row.getCell(2).getStringCellValue();
                String[] subjectStrings = row.getCell(3).getStringCellValue().split(",");
                ArrayList<String> chsubjects = new ArrayList<>();
                for (String subjectStr : subjectStrings) {
                    chsubjects.add(subjectStr);
                }
                KSWAChildren child = new KSWAChildren(chprename, chname, chbirthday, chsubjects);
                childrenList.add(child);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return childrenList;
    }
}
