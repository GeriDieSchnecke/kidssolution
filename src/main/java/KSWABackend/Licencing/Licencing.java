package KSWABackend.Licencing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Licencing {

    // Lizenzen
    // I7KI9KY9S    Show diagram
    // IUX5N4DU6    Kid profile
    // I7G6NPH6C    Show diagram & Kid profile



    public static void runGetLicences(){
    String[] CustomerLicences = getLicences("I7KI9KY9S");
        assert CustomerLicences != null;
        for (String a : CustomerLicences)
            System.out.println(a);
    }

    public static boolean validateLicence(String licenceID){
        String[] licences = getLicences(licenceID);

        if(licences[0].equals("invalid")){
            return false;
        } else {
            return true;
        }
    }

    public static String[] getLicences(String licenceID){
        String s = null;
        String[] CustomerLicences;

        // For api testing use licenceID "1" to return valid licence
        if (licenceID.equals("1")){
            String testLicence = "Show kids profile@true@Show diagram@true";
            return testLicence.split("@");
        }

        try {
            // pip install json
            // pip install pycurl
            // Set path to python.exe in environment where json and pycurl is installed

            String absPathWorkingdir = System.getProperty("user.dir");

            // set the path to your systems Python installation
            String pythonPath = "E:/Envs/python.exe";
            //String pythonPath = "C:/python/python.exe";

            String pythonCommand = pythonPath + " " + absPathWorkingdir + "/src/main/java/KSWANetLicensing/Handler/netlicensinghandler.py " + licenceID;
            Process p = Runtime.getRuntime().exec(pythonCommand);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            String returnListString = "";
            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                returnListString = s;
            }

            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            System.out.println(returnListString);

            CustomerLicences = returnListString.split("@");

            return CustomerLicences;

        } catch (IOException ioException){
            ioException.printStackTrace();
            System.out.println("IOException: Python installation not found. Set pythonPath variable in Class Licencing.java to the Path of your systems Python.exe. For testing use licenceID \"1\" to return valid licence");
        }
        catch (Exception exception){
            exception.printStackTrace();

        }
        System.out.println("For testing use licenceID \"1\" to return valid licence");
        String[] invalid = new String[1];
        invalid[0] = "invalid";
        return invalid;
    }

}
