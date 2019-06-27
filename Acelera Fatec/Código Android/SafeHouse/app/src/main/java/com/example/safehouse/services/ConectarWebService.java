package com.example.safehouse.services;

public class ConectarWebService
{
    private static String endServer = "";

    public static String getEndServer()
    {
        endServer = "192.168.1.107";

        //IP Auditório Fatec 192.168.0.124

        return endServer;
    }
}
