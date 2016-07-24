package com.shc.easyxml;

/**
 * Represents a parsing exception. This is a runtime exception because usually it is not expected since the xml files
 * are ensured to be valid and well formed, and most editors provide checking to XML documents right at their creation.
 *
 * @author Sri Harsha Chilakapati
 */
public class ParseException extends RuntimeException
{
    ParseException(String message)
    {
        super(message);
    }

    ParseException(XmlToken.Type expected, XmlToken got)
    {
        super("Expected " + expected
              + "; got " + got.type + "(" + got.text + ") at "
              + got.lineFrom + ":" + got.start + "-" + got.end);
    }
}
