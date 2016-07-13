package com.shc.easyxml;

/**
 * @author Sri Harsha Chilakapati
 */
public class XmlTokenizer
{
    XmlToken currentToken;

    private StringStream input;

    private boolean textParseMode = false;
    private boolean cdataMode = false;

    public XmlTokenizer(String input)
    {
        this.input = new StringStream(input);
    }

    public XmlToken extract()
    {
        // Eat the whitespace before the token
        char ch = input.getCurrentChar();

        while (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n')
            ch = input.getNextChar();

        if (input.getCurrentChar() == ']')
        {
            // Must be a CDATA_END token
            expect("]]>");
            textParseMode = true;
            cdataMode = false;
            return currentToken =
                    new XmlToken(XmlToken.Type.CDATA_END, "]]>", input.line, input.column - 3, input.column);
        }

        if (input.getCurrentChar() == '<' && !cdataMode)
        {
            // Should check if the next chars are !--, if it is then it is a comment
            // Otherwise it can also be a CDATA_BEGIN ![CDATA[
            if (input.peekNextChar() == '!')
            {
                input.getNextChar();

                // If the next character is [ then it should be a CDATA_BEGIN
                if (input.peekNextChar() == '[')
                {
                    input.getNextChar();

                    int start = input.column - 3;
                    int line = input.line;

                    expect("[CDATA[");

                    textParseMode = true;
                    cdataMode = true;
                    return currentToken
                            = new XmlToken(XmlToken.Type.CDATA_BEGIN, "<!CDATA[", line, start, input.column);
                }
                else
                {
                    // The next two chars should also be hyphens, otherwise throw a parse exception
                    for (int i = 0; i < 2; i++)
                        if (input.getNextChar() != '-')
                            throw new ParseException("Error at " + input.line + ":"
                                                     + input.column + ": Expected a hyphen (-), but got "
                                                     + input.getCurrentChar());

                    // Now eat everything until a sequence of --> appears
                    while (true)
                    {
                        if (input.getCurrentChar() == '-')
                        {
                            if (input.getNextChar() == '-')
                                if (input.getNextChar() == '>')
                                {
                                    input.getNextChar();
                                    textParseMode = true;
                                    return extract();
                                }
                        }
                        else
                            input.getNextChar();
                    }
                }
            }

            textParseMode = false;

            // Should check if the next one is a ?, if it is, just skip until the end is reached
            if (input.peekNextChar() == '?')
            {
                while (input.getCurrentChar() != '>')
                    input.getNextChar();

                input.getNextChar();

                // Extract a new token after the prologue
                return currentToken = extract();
            }

            // Consume the < symbol
            input.getNextChar();

            // Should check if the next char is /, if it is, then it is a TAG_END token
            if (input.getCurrentChar() == '/')
            {
                input.getNextChar();
                return currentToken
                        = new XmlToken(XmlToken.Type.TAG_CLOSE, "</", input.line, input.column - 2, input.column);
            }

            // Otherwise it's a tag begin token

            return currentToken
                    = new XmlToken(XmlToken.Type.TAG_BEGIN, "<", input.line, input.column - 1, input.column);
        }

        if (input.getCurrentChar() == '"' || input.getCurrentChar() == '\'')
        {
            // The STRING token, which is the value of the attribute.
            int line = input.line;
            int column = input.column;

            // Check for ' or " as both are valid strings
            char stringStart = input.getCurrentChar();

            StringBuilder builder = new StringBuilder();

            while ((ch = input.getNextChar()) != stringStart)
                builder.append(ch);

            input.getNextChar();

            String value = builder.toString()
                    .replaceAll("&lt;", "<")
                    .replaceAll("&gt;", ">")
                    .replaceAll("&apos;", "'")
                    .replaceAll("&quot;", "\"")
                    .replaceAll("&amp;", "&");

            return currentToken = new XmlToken(XmlToken.Type.STRING, value, line, column, input.column - 1);
        }

        if (textParseMode)
        {
            // Read text between tags here. We read until a tag begin character is found.
            int lineStart = input.line;
            int start = input.column;

            StringBuilder builder = new StringBuilder();
            builder.append(ch);

            while ((ch = input.getNextChar()) != StringStream.EOF)
            {
                if (cdataMode)
                {
                    if (follows("]]>"))
                        break;
                }
                else
                    if (ch == '<')
                        break;

                builder.append(ch);
            }

            String value = builder.toString().trim()
                    .replaceAll("\\n(\\s)*", " ");

            int lineEnd = input.line;
            int end = input.column;

            textParseMode = false;

            if (!value.equals(""))
                return currentToken = new XmlToken(XmlToken.Type.TEXT, value, lineStart, lineEnd, start, end);

            return currentToken = extract();
        }
        else
        {
            char previousChar;

            if (Character.isLetter(input.getCurrentChar()) || input.getCurrentChar() == '_')
            {
                // It is a name, so read the name as the word
                StringBuilder builder = new StringBuilder();

                int line = input.line;
                int start = input.column;

                builder.append(previousChar = input.getCurrentChar());

                while (Character.isLetterOrDigit(input.getNextChar())
                       || input.getCurrentChar() == '_'
                       || input.getCurrentChar() == ':')
                {
                    if (input.getCurrentChar() == ':' && previousChar == ':')
                        throw new ParseException("Unexpected namespace separator at "
                                                 + input.line + ":" + input.column);

                    builder.append(previousChar = input.getCurrentChar());
                }

                return currentToken
                        = new XmlToken(XmlToken.Type.NAME, builder.toString(), line, start, input.column);
            }
        }

        if (input.getCurrentChar() == '>')
        {
            // The TAG_END token is this one. Ends a tag, and goes into it's contents.
            int line = input.line;
            int column = input.column;

            input.getNextChar();
            textParseMode = true;

            return currentToken = new XmlToken(XmlToken.Type.TAG_END, ">", line, column, column + 1);
        }

        if (input.getCurrentChar() == '=')
        {
            // Equals token
            int line = input.line;
            int column = input.column;

            input.getNextChar();

            return currentToken = new XmlToken(XmlToken.Type.EQUALS, "=", line, column, column + 1);
        }

        if (input.getCurrentChar() == '/')
        {
            // Check if the next char is > symbol. If so, it must be a simple close tag
            if (input.peekNextChar() == '>')
            {
                int line = input.line;
                int column = input.column;

                input.getNextChar();
                input.getNextChar();
                textParseMode = true;

                return currentToken = new XmlToken(XmlToken.Type.SIMPLE_TAG_CLOSE, "/>", line, column, column + 2);
            }
        }

        return currentToken = new XmlToken(XmlToken.Type.EOF, "", input.line, input.column, input.column);
    }

    private void expect(String text)
    {
        char ch = input.getCurrentChar();

        for (char c : text.toCharArray())
        {
            if (c != ch)
                throw new ParseException("Expected '" + c + "' at " + input.line + ":" + input.column +
                                         " but got '" + ch + "'");

            ch = input.getNextChar();
        }
    }

    private boolean follows(String text)
    {
        int column = input.column;
        int line = input.line;
        int index = input.index;
        boolean result = true;

        for (char ch : text.toCharArray())
        {
            if (input.getCurrentChar() != ch)
            {
                result = false;
                break;
            }

            input.getNextChar();
        }

        input.column = column;
        input.line = line;
        input.index = index;

        return result;
    }
}
