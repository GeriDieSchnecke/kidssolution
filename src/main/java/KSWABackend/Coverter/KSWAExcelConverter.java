package KSWABackend.Coverter;

import KSWABackend.Model.KSWAChildren;
import KSWABackend.Model.KSWASubject;
import KSWABackend.Model.KSWATest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/kswa")
public class KSWAExcelConverter {


    @GetMapping("/children")
    public ResponseEntity<List<KSWAChildren>> getChildrenData() {
        List<KSWAChildren> childrenData = readDataFromExcel();
        return new ResponseEntity<>(childrenData, HttpStatus.OK);
    }

    @PostMapping("/children/write")
    public ResponseEntity<String> writeDataToExcel(@RequestBody List<KSWAChildren> childrenList) {
        writeToExcel(childrenList);
        return new ResponseEntity<>("Excel file has been created successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/children/export")
    public ResponseEntity<String> exportExcel(@RequestParam String filePath) {
        List<KSWAChildren> childrenList = readDataFromExcel();
        exportExcel(childrenList, filePath);
        return new ResponseEntity<>("Excel file has been exported successfully!", HttpStatus.OK);
    }


    public static List<KSWAChildren> readDataFromExcel() {
        String filePath = System.getProperty("user.dir");
        List<KSWAChildren> childrenList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                KSWAChildren child = new KSWAChildren();
                child.setId((long) row.getCell(0).getNumericCellValue());
                child.setChprename(row.getCell(1).getStringCellValue());
                child.setChname(row.getCell(2).getStringCellValue());
                child.setChbirthday(row.getCell(3).getStringCellValue());

                List<KSWASubject> subjects = new ArrayList<>();
                int cellIndex = 4;
                while (cellIndex < row.getLastCellNum()) {
                    KSWASubject subject = new KSWASubject();
                    subject.setSuname(row.getCell(cellIndex++).getStringCellValue());
                    subject.setSugrade(row.getCell(cellIndex++).getNumericCellValue());

                    ArrayList<KSWATest> tests = new ArrayList<KSWATest>();
                    while (cellIndex < row.getLastCellNum() && row.getCell(cellIndex) != null) {
                        KSWATest test = new KSWATest();
                        test.setTename(row.getCell(cellIndex++).getStringCellValue());
                        test.setTegrade(row.getCell(cellIndex++).getNumericCellValue());
                        test.setTefactor(row.getCell(cellIndex++).getNumericCellValue());
                        test.setTedate(row.getCell(cellIndex++).getDateCellValue());

                        tests.add(test);
                    }
                    subject.setTests(tests);
                    subjects.add(subject);
                }

                child.setChsubjects(subjects);
                childrenList.add(child);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return childrenList;
    }

    public static void writeToExcel(List<KSWAChildren> childrenList) {
        String projectDirectory = System.getProperty("user.dir");
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("KSWA Data");

        int rowIndex = 0;
        for (KSWAChildren child : childrenList) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(child.getId());
            row.createCell(1).setCellValue(child.getChprename());
            row.createCell(2).setCellValue(child.getChname());
            row.createCell(3).setCellValue(child.getChbirthday());

            List<KSWASubject> subjects = child.getChsubjects();
            if (subjects != null && !subjects.isEmpty()) {
                int cellIndex = 4;
                for (KSWASubject subject : subjects) {
                    row.createCell(cellIndex++).setCellValue(subject.getSuname());
                    row.createCell(cellIndex++).setCellValue(subject.getSugrade());

                    List<KSWATest> tests = subject.getTests();
                    if (tests != null && !tests.isEmpty()) {
                        for (KSWATest test : tests) {
                            row.createCell(cellIndex++).setCellValue(test.getTename());
                            row.createCell(cellIndex++).setCellValue(test.getTegrade());
                            row.createCell(cellIndex++).setCellValue(test.getTefactor());
                            row.createCell(cellIndex++).setCellValue(test.getTedate().toString()); // Konvertierung des Datums in String
                        }
                    }
                }
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(projectDirectory)) {
            workbook.write(fileOut);
            System.out.println("Excel file has been created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

}
