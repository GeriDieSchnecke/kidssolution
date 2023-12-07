package KSWABackend.Coverter;

import KSWABackend.Model.KSWAChildren;
import KSWABackend.Model.KSWASubject;
import KSWABackend.Model.KSWATest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KSWAExcelConverter {

    public static void exportExcel(List<KSWAChildren> childrenList, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("KSWA Data");

        Row headerRowChildren = sheet.createRow(0);
        headerRowChildren.createCell(0).setCellValue("ID");
        headerRowChildren.createCell(1).setCellValue("Prename");
        headerRowChildren.createCell(2).setCellValue("Name");
        headerRowChildren.createCell(3).setCellValue("Birthday");
        headerRowChildren.createCell(4).setCellValue("Subjects");
        headerRowChildren.createCell(5).setCellValue("Tests");

        int rowNumChildren = 1;
        for (KSWAChildren child : childrenList) {
            Row row = sheet.createRow(rowNumChildren++);
            row.createCell(0).setCellValue(child.getId());
            row.createCell(1).setCellValue(child.getChprename());
            row.createCell(2).setCellValue(child.getChname());
            row.createCell(3).setCellValue(child.getChbirthday());

            Cell subjectsCell = row.createCell(4);
            List<KSWASubject> subjects = child.getChsubjects();
            if (subjects != null && !subjects.isEmpty()) {
                StringBuilder subjectsStr = new StringBuilder();
                for (KSWASubject subject : subjects) {
                    subjectsStr.append(subject).append(", ");
                }
                subjectsCell.setCellValue(subjectsStr.substring(0, subjectsStr.length() - 2));
            }

            Cell testsCell = row.createCell(5);
            List<KSWASubject> subjectsList = child.getChsubjects();
            if (subjectsList != null && !subjectsList.isEmpty()) {
                StringBuilder testsStr = new StringBuilder();
                for (KSWASubject subject : subjectsList) {
                    testsStr.append("Subject Name: ").append(subject.getSuname()).append("\n");

                    List<KSWATest> tests = subject.getTests();
                    if (tests != null && !tests.isEmpty()) {
                        for (KSWATest test : tests) {
                            testsStr.append("\tName: ").append(test.getTename())
                                    .append(", Grade: ").append(test.getTegrade())
                                    .append(", Factor: ").append(test.getTefactor())
                                    .append(", Date: ").append(test.getTedate()).append("\n");
                        }
                    }
                }
                testsCell.setCellValue(testsStr.toString());
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            System.out.println("Excel file has been created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<KSWAChildren> importExcel(String filePath) {
        List<KSWAChildren> childrenList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                KSWAChildren child = new KSWAChildren();
                child.setId((long) row.getCell(0).getNumericCellValue());
                child.setChprename(row.getCell(1).getStringCellValue());
                child.setChname(row.getCell(2).getStringCellValue());
                child.setChbirthday(row.getCell(3).getStringCellValue());

                List<KSWASubject> subjects = new ArrayList<>();
                KSWASubject subject = new KSWASubject();
                subject.setSuname(row.getCell(4).getStringCellValue()); // Subject name from Excel
                subject.setSugrade(row.getCell(5).getNumericCellValue()); // Subject grade from Excel

                List<KSWATest> tests = new ArrayList<>();
                KSWATest test = new KSWATest();
                test.setTename(row.getCell(6).getStringCellValue()); // Test name from Excel
                test.setTegrade(row.getCell(7).getNumericCellValue()); // Test grade from Excel
                test.setTefactor(row.getCell(8).getNumericCellValue()); // Test factor from Excel
                // test.setTedate(); // You need to handle date parsing from Excel cell to Date object

                tests.add(test);
                subject.setTests((ArrayList<KSWATest>) tests);
                subjects.add(subject);

                child.setChsubjects(subjects);

                childrenList.add(child);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return childrenList;
    }

    //TODO: Remove its just for testing.
    public static void main(String[] args) {
        KSWAChildren child1 = new KSWAChildren("Max", "Mustermann", "2008-05-20", null);
        child1.setId(1L);
        List<KSWASubject> child1Subjects = new ArrayList<>();

        KSWATest test1 = new KSWATest(85.0, "Math Test 1", 0.5, new Date(), 1);
        KSWATest test2 = new KSWATest(75.0, "Math Test 2", 0.5, new Date(), 2);
        List<KSWATest> mathTests = new ArrayList<>(List.of(test1, test2));
        KSWASubject mathSubject = new KSWASubject("Math", 80.0, (ArrayList<KSWATest>) mathTests, 1);
        child1Subjects.add(mathSubject);

        KSWATest test3 = new KSWATest(70.0, "Science Test 1", 0.5, new Date(), 3);
        KSWATest test4 = new KSWATest(65.0, "Science Test 2", 0.5, new Date(), 4);
        List<KSWATest> scienceTests = new ArrayList<>(List.of(test3, test4));
        KSWASubject scienceSubject = new KSWASubject("Science", 67.5, (ArrayList<KSWATest>) scienceTests, 2);
        child1Subjects.add(scienceSubject);

        child1.setChsubjects(child1Subjects);

        List<KSWAChildren> childrenList = new ArrayList<>(List.of(child1));

        String desktopPath = System.getProperty("user.home") + "/Desktop/";
        String filePath = desktopPath + "output.xlsx";

        exportExcel(childrenList, filePath);

        String newdesktopPath = System.getProperty("user.home") + "/Desktop/";
        String newfilePath = newdesktopPath + "input.xlsx";

        List<KSWAChildren> importedData = importExcel(newfilePath);
    }

}
