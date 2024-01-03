package KSWABackend.Coverter;

import KSWABackend.Model.KSWAChildren;
import KSWABackend.Model.KSWASubject;
import KSWABackend.Model.KSWATest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

import static KSWABackend.Authentication.KSWAUserAuthentication.generateRandomId;

public class KSWAExcelConverter {


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

                    ArrayList<KSWATest> tests = new ArrayList<>();
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
                            row.createCell(cellIndex++).setCellValue(test.getTedate().toString());
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

    public static List<KSWAChildren> importExcel(String filePath) {
        List<KSWAChildren> childrenList = new ArrayList<>();

        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> iterator = sheet.iterator();
            if (iterator.hasNext()) {
                iterator.next();
            }
            KSWAChildren child = null;
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                child = parseRowToChild(currentRow);
                childrenList.add(child);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return childrenList;
    }

    private static KSWAChildren parseRowToChild(Row row) throws ParseException {
        KSWAChildren child = new KSWAChildren();

        child.setId((long) row.getCell(0).getNumericCellValue());
        child.setChprename(row.getCell(1).getStringCellValue());
        child.setChname(row.getCell(2).getStringCellValue());
        child.setChbirthday(row.getCell(3).getStringCellValue());
        child.setChsubjects(parseSubjectsFromString(child, row.getCell(4).getStringCellValue()));
        for (KSWASubject subject : child.getChsubjects()) {
            subject.setTests(parseTestsFromString(row.getCell(5).getStringCellValue()));
        }
        return child;
    }

    private static List<KSWASubject> parseSubjectsFromString(KSWAChildren child, String subjectStrings) {
        String[] strings = subjectStrings.split(", ");
        List<KSWASubject> subjects = new ArrayList<>();

        for (String str : strings) {
            KSWASubject subject = new KSWASubject();
            subject.setId(generateRandomId());
            subject.setSuname(String.valueOf(str));
            subject.setSugrade(new Random().nextInt(10));
            subject.setChildren(child);
            subjects.add(subject);
        }

        return subjects;
    }

    private static List<KSWATest> parseTestsFromString(String testsStr) throws ParseException {
        List<KSWATest> tests = new ArrayList<>();
        String[] testLines = testsStr.split("\n");
        testLines[0] = "";
        for (String testLine : testLines) {
            if (!testLine.isEmpty()) {
                KSWATest test = parseTestLine(testLine);
                tests.add(test);
            }
        }
        return tests;
    }

    private static KSWATest parseTestLine(String testStr) throws ParseException {
        KSWATest test = new KSWATest();
        String[] testparts = testStr.split(",\\s*");

        List<String> tempList = new ArrayList<>();
        for (String str : testparts) {
            String[] subParts = str.split(":");
            for (int i = 0; i < subParts.length; i++) {
                subParts[i] = subParts[i].replace(",", "").replace(":", "");
            }
            tempList.addAll(Arrays.asList(subParts));
        }


        String[] newparts = IntStream.range(0, tempList.size())
                .filter(i -> i % 2 != 0)
                .mapToObj(tempList::get)
                .toArray(String[]::new);

        test.setTename(newparts[0]);
        test.setTegrade(Double.parseDouble(newparts[1]));
        test.setTefactor(Double.parseDouble(newparts[2]));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedDate = dateFormat.parse(newparts[3]);
        Date sqlDate = new Date(parsedDate.getTime());
        test.setTedate(sqlDate);
        test.setId(new Random().nextInt(1000));
        return test;
    }


    public static void exportExcel(List<KSWAChildren> childrenList, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("KSWA Data");

        createHeaderRow(sheet);

        int rowNumChildren = 1;
        for (KSWAChildren child : childrenList) {
            Row row = sheet.createRow(rowNumChildren++);
            createChildRow(child, row);
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            System.out.println("Excel file has been created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createHeaderRow(Sheet sheet) {
        Row headerRowChildren = sheet.createRow(0);
        String[] headers = {"ID", "Prename", "Name", "Birthday", "Subjects", "Tests"};
        for (int i = 0; i < headers.length; i++) {
            headerRowChildren.createCell(i).setCellValue(headers[i]);
        }
    }

    private static void createChildRow(KSWAChildren child, Row row) {
        row.createCell(0).setCellValue(child.getId());
        row.createCell(1).setCellValue(child.getChprename());
        row.createCell(2).setCellValue(child.getChname());
        row.createCell(3).setCellValue(child.getChbirthday());

        Cell subjectsCell = row.createCell(4);
        List<KSWASubject> subjects = child.getChsubjects();
        if (subjects != null && !subjects.isEmpty()) {
            StringBuilder subjectsStr = new StringBuilder();
            for (KSWASubject subject : subjects) {
                subjectsStr.append(subject.getSuname()).append(", ");
            }
            subjectsCell.setCellValue(subjectsStr.substring(0, subjectsStr.length() - 2));
        }

        Cell testsCell = row.createCell(5);
        if (child.getChsubjects() != null && !child.getChsubjects().isEmpty()) {
            StringBuilder testsStr = new StringBuilder();
            for (KSWASubject subject : child.getChsubjects()) {
                testsStr.append("Subject Name: ").append(subject.getSuname()).append("\n");

                List<KSWATest> tests = subject.getTests();
                if (tests != null && !tests.isEmpty()) {
                    for (KSWATest test : tests) {
                        appendTestInfo(testsStr, test);
                    }
                }
            }
            testsCell.setCellValue(testsStr.toString());
        }
    }

    private static void appendTestInfo(StringBuilder testsStr, KSWATest test) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = test.getTedate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter);

        testsStr.append("\tName: ").append(test.getTename())
                .append(", Grade: ").append(test.getTegrade())
                .append(", Factor: ").append(test.getTefactor())
                .append(", Date: ").append(formattedDate).append("\n");
    }

}
