package com.shc.easyxml.test;

import com.shc.easyxml.Xml;
import com.shc.easyxml.XmlToken;
import com.shc.easyxml.XmlTokenizer;

/**
 * @author Sri Harsha Chilakapati
 */
public class XmlTokenTest
{
    public static void main(String[] args)
    {
        String xml = Util.readToString("Test1.xml");

        XmlTokenizer tokenizer = new XmlTokenizer(xml);
        XmlToken token;

        while ((token = tokenizer.extract()).type != XmlToken.Type.EOF)
            System.out.println(token);

        Xml.parse(xml);
    }
}
