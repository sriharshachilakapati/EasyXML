package com.shc.easyxml.test;

import com.shc.easyxml.Xml;
import com.shc.easyxml.XmlTag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Sri Harsha Chilakpati
 */
public class TagTest
{
    private static final String test1 = Util.readToString("Test1.xml");

    @Test
    public void testParse()
    {
        Xml.parse(test1);
    }

    @Test
    public void testRootTagName()
    {
        XmlTag root = Xml.parse(test1);
        assertEquals(root.name, "test");
    }
}
