package KSWABackend.Licencing;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Licencing {

    public void runGetLicences(){
    String[] CustomerLicences = getLicences("I7KI9KY9S");
        assert CustomerLicences != null;
        for (String a : CustomerLicences)
            System.out.println(a);
    }

    public static String[] getLicences(String licenceID){
        String s = null;
        String[] CustomerLicences;

        try {
            // pip install json
            // pip install pycurl
            // Set path to python.exe in environment where json and pycurl is installed

            String pythonCommand = "E:/Envs/envs/rest/python.exe app.py " + licenceID;
            Process p = Runtime.getRuntime().exec(pythonCommand);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            String returnListString = "";
            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                returnListString = s;
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            CustomerLicences = returnListString.split("@");

            return CustomerLicences;

        } catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

}
