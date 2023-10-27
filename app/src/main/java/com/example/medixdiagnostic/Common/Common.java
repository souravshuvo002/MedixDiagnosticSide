package com.example.medixdiagnostic.Common;

import com.example.medixdiagnostic.Api.FCMClient;
import com.example.medixdiagnostic.Api.IFCMService;
import com.example.medixdiagnostic.Model.Diagnostic;
import com.example.medixdiagnostic.Model.Test;

public class Common {

    public static String Diagnostic_email = "";
    public static String Diagnostic_phone = "";
    public static String DIAGNOSTIC_ID = "";
    public static String TEST_ID = "";
    public static Diagnostic currentDiagnostic;
    public static Test currentTestItem;
    public static String CENTER_ID = "";


    private static final String FCM_API = "https://fcm.googleapis.com/";

    public static IFCMService getFCMService(){
        return FCMClient.getClient(FCM_API).create(IFCMService.class);
    }

    public static String convertCodeToStatus(String code)
    {
        if(code.equals("1"))
            return "Pending";
        else if(code.equals("2"))
            return "Accepted";
        else if(code.equals("3"))
            return "Rejected";
        else if(code.equals("4"))
            return "Completed";
        else
            return "No result";
    }
}
