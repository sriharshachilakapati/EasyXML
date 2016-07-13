package com.shc.easyxml;

/**
 * @author Sri Harsha Chilakapati
 */
public class XmlToken
{
    public Type   type;
    public String text;

    public int lineFrom;
    public int lineTo;
    public int start;
    public int end;

    XmlToken(Type type, String text, int line, int start, int end)
    {
        this.type = type;
        this.text = text;
        this.lineFrom = lineTo = line;
        this.start = start;
        this.end = end;
    }

    XmlToken(Type type, String text, int lineFrom, int lineTo, int start, int end)
    {
        this.type = type;
        this.text = text;
        this.lineFrom = lineFrom;
        this.lineTo = lineTo;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString()
    {
        if (lineFrom == lineTo)
            return "XmlToken{" +
                   "type=" + type +
                   ", text='" + text + '\'' +
                   ", line=" + lineFrom +
                   ", start=" + start +
                   ", end=" + end +
                   '}';

        return "XmlToken{" +
               "type=" + type +
               ", text='" + text + '\'' +
               ", lineFrom=" + lineFrom +
               ", lineTo=" + lineTo +
               ", start=" + start +
               ", end=" + end +
               '}';
    }

    public enum Type
    {
        TAG_BEGIN,        // <
        TAG_END,          // >
        NAME,             // [a-zA-Z]+?:[a-zA-Z0-9_]*
        SIMPLE_TAG_CLOSE, // />
        TAG_CLOSE,        // </
        STRING,           // \".*\"|\'.*\'
        TEXT,             // Text between tags
        EQUALS,           // =
        CDATA_BEGIN,      // <![CDATA[
        CDATA_END,        // ]]>
        EOF
    }
}
