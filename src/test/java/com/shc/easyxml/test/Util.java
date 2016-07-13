package com.shc.easyxml.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Sri Harsha Chilakapati
 */
class Util
{
    static String readToString(String path)
    {
        InputStream is = Util.class.getClassLoader().getResourceAsStream(path);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is)))
        {
            String line, data = "";

            while ((line = reader.readLine()) != null)
                data += line + "\n";

            reader.close();
            return data;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
