package com.iktdev.nanostat.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Brage on 18.02.2018.
 */

public class HttpHandler
{
    public HttpURLConnection client;
    private URL url;

    public boolean setUrl(String value)
    {
        try
        {
            URL url = new URL(value);
            this.url = url;
            return true;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public URL getUrl()
    {
        return this.url;
    }

    public String getApiResponse(URL url)
    {
        try
        {
            client = (HttpURLConnection)url.openConnection();
            client.setRequestMethod("GET");
            client.setReadTimeout(5000);
            client.setConnectTimeout(5000);
            client.setDoOutput(false);
            client.connect();
            return readFromConnection(new InputStreamReader((url.openStream())));

        }
        catch (IOException e)
        {

        }
        finally {
            client.disconnect();
        }

        return "";
    }

    private String readFromConnection(InputStreamReader isr)
    {
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while(((line = br.readLine())) != null)
            {
                sb.append(line + "\n");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
