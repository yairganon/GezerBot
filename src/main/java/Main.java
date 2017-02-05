import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import javax.security.auth.login.FailedLoginException;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private final static String BaseUrl= "https://gezer1.bgu.ac.il/meser/";
    private final static String EntranceEndPoint= BaseUrl+"entrance.php";
    private final static String CrslistEndPoint= BaseUrl+"crslist.php";
    private final static String UserNameKey = "uname";
    private final static String PasswordKey = "passwd";
    private final static String IdKey = "id";
    private final static String AgreeKey = "agree";
    private final static String RefererKey= "Referer";
    private final static String RandomString = "SomeThing";
    private final static String Charset = "windows-1255";
    private final static String PatternInSuccessfulLogIn = "input type=submit name=\"agree\" value";
    private static int lastConnectNumberOfExams = 0;
    private static int lastConnectNumberOfFileScores = 0;

    private static final String userName = "XXXXXXX";
    private static final String pass = "XXXXXXX";
    private static final String userId = "XXXXXXX";

    public static void main(String[] args) throws Exception {
        System.setProperty("jsse.enableSNIExtension", "false");

        while (true) {
            connectToGezerAndPrint();
            Thread.sleep(10000);
        }
    }

    private static void connectToGezerAndPrint() throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost requestEntrance = new HttpPost(EntranceEndPoint);
        HttpPost requestGrades = new HttpPost(CrslistEndPoint);

        initRequests(requestEntrance, requestGrades);
        logInUser(client, requestEntrance);

        String responseFromGrades = responceString(client, requestGrades);
        ArrayList<String> x = new ArrayList<String>(Arrays.asList(responseFromGrades.split("<")));
        ArrayList<String> scanExams = new ArrayList<String>();
        ArrayList<String> fileScores = new ArrayList<String>();

        for (String s :x) {
            if(s.contains("input") && s.contains("קובץ המחברת")) {
                scanExams.add(s);
            }else if(s.contains("input") && s.contains("קובץ שאלון/ציונים")){
                fileScores.add(s);
            }
        }
        if(lastConnectNumberOfExams != 0 && lastConnectNumberOfExams < scanExams.size()){
            JFrame frame = new JFrame("MainFrame");
            System.out.println("New scanExams");
            JOptionPane.showMessageDialog(frame, "New scanExams");

        }
        if(lastConnectNumberOfFileScores != 0 && lastConnectNumberOfFileScores < fileScores.size()){
            JFrame frame = new JFrame("MainFrame");
            System.out.println("New file score");
            JOptionPane.showMessageDialog(frame, "New file score");

        }
        lastConnectNumberOfExams = scanExams.size();
        lastConnectNumberOfFileScores = fileScores.size();
        System.out.println("\n\nNumber of scan exams:"+scanExams.size()+"\nNumber of File scores:"+fileScores.size());
    }

    private static void logInUser(HttpClient client, HttpPost requestEntrance) throws Exception {
        String responceFromLogIn = responceString(client, requestEntrance);
        if(!responceFromLogIn.contains(PatternInSuccessfulLogIn))
            throw new FailedLoginException("Fail to log in");
    }

    private static void initRequests(HttpPost requestEntrance, HttpPost requestGrades) throws UnsupportedEncodingException {
        List<NameValuePair> paramForLogIn = new ArrayList<NameValuePair>();
        paramForLogIn.add(new BasicNameValuePair(UserNameKey, userName));
        paramForLogIn.add(new BasicNameValuePair(PasswordKey, pass));
        paramForLogIn.add(new BasicNameValuePair(IdKey, userId));
        List<NameValuePair> paramsForCrslist = new ArrayList<NameValuePair>();
        paramsForCrslist.add(new BasicNameValuePair(AgreeKey, RandomString));
        requestEntrance.setEntity(new UrlEncodedFormEntity(paramForLogIn));
        requestGrades.setHeader(RefererKey, EntranceEndPoint);
        requestGrades.setEntity(new UrlEncodedFormEntity(paramsForCrslist));
    }

    private static String responceString(HttpClient client, HttpPost request) throws Exception {
        HttpResponse response = client.execute(request);
        if( response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            throw new NotHttpOkStatusException();
        return readStringFromResponce(response);
    }

    private static String readStringFromResponce(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(), Charset));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}
