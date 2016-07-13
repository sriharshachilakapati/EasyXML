package com.shc.easyxml;

/**
 * @author Sri Harsha Chilakapati
 */
public class ParseException extends RuntimeException
{
    public ParseException(String message)
    {
        super(message);
    }

    public ParseException(XmlToken.Type expected, XmlToken got)
    {
        super("Expected " + expected
              + "; got " + got.type + "(" + got.text + ") at "
              + got.lineFrom + ":" + got.start + "-" + got.end);
    }
}
