package KSWABackend.Authentication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import KSWABackend.Licencing.Licencing;
import KSWABackend.Model.KSWATeacher;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Random;

public class KSWAUserAuthentication {

    private static final String FILE_NAME = "user_credentials.xlsx";
    private static final String SHEET_NAME = "Users";


    public static void main(String[] args) {
        try {
            createSheetIfNotExists();

            String username = "new_test";
            String password = "new_password";
            KSWATeacher isUserAuthenticated = authenticateUser(username, password);
            System.out.println("Is User Authenticated: " + isUserAuthenticated);

            String newUsername = "new_test";
            String newPassword = "new_password";
            String newLicence = "I7KI9KY9S";
            boolean isUserRegistered = registerUser(newUsername, newPassword, newLicence);
            System.out.println("Is User Registered: " + isUserRegistered);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createSheetIfNotExists() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(SHEET_NAME);
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Username");
            headerRow.createCell(2).setCellValue("Password");
            FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
            workbook.write(fileOut);
            fileOut.close();
        }
    }

    public static KSWATeacher authenticateUser(String username, String password) throws IOException {
        createSheetIfNotExists();
        FileInputStream file = new FileInputStream(FILE_NAME);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet(SHEET_NAME);
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            String usernameCell = row.getCell(1).getStringCellValue();
            String passwordCell = row.getCell(2).getStringCellValue();

            if (username.equals(usernameCell) && password.equals(passwordCell)) {
                String userId = row.getCell(0).getStringCellValue();
                return new KSWATeacher(userId, username, password);
            }
        }
        return null;
    }

    public static boolean registerUser(String username, String password, String licenceID) throws IOException {
        createSheetIfNotExists();
        FileInputStream file = new FileInputStream(FILE_NAME);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet(SHEET_NAME);
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (username.equals(row.getCell(1).getStringCellValue())) {
                return false;
            }
            if (licenceID.equals(row.getCell(0).getStringCellValue())) {
                return false;
            }
        }

        if (Licencing.validateLicence(licenceID)){
            // pass
        } else {
            return false;
        }

        Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
        newRow.createCell(0).setCellValue(licenceID);
        newRow.createCell(1).setCellValue(username);
        newRow.createCell(2).setCellValue(password);

        FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
        workbook.write(fileOut);
        fileOut.close();

        return true;
    }
    public static int generateRandomId() {
        Random random = new Random();
        return random.nextInt(10000);
    }
}