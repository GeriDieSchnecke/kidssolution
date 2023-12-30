package KSWABackend.Licencing;

import java.io.BufferedReader;
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

        if (licenceID.equals("1")){
            return true;
        }

        if(licences[0].equals("invalid")){
            return false;
        } else {
            return true;
        }
    }

    public static String[] getLicences(String licenceID){
        String s = null;
        String[] CustomerLicences;

        try {
            // pip install json
            // pip install pycurl
            // Set path to python.exe in environment where json and pycurl is installed

            String absPathWorkingdir = System.getProperty("user.dir");
            String pythonCommand = "E:/Envs/envs/rest/python.exe " + absPathWorkingdir + "/src/main/java/KSWANetLicensing/Handler/netlicensinghandler.py " + licenceID;
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

            //System.out.println(returnListString);

            CustomerLicences = returnListString.split("@");

            return CustomerLicences;

        } catch (Exception exception){
            //exception.printStackTrace();
            String[] invalid = new String[1];
            invalid[0] = "invalid";
            return invalid;
        }
    }

}
