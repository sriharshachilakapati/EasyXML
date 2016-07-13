package com.shc.easyxml;

/**
 * @author Sri Harsha Chilakapati
 */
public class XmlAttribute
{
    public String name;
    public String value;

    public XmlAttribute(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "XmlAttribute{" +
               "name='" + name + '\'' +
               ", value='" + value + '\'' +
               '}';
    }
}
