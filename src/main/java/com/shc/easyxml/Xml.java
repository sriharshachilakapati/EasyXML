package com.shc.easyxml;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Xml
{
    private Xml()
    {
    }

    public static XmlTag parse(String xmlString)
    {
        XmlTokenizer tokenizer = new XmlTokenizer(xmlString);
        XmlTag tag = parseTag(tokenizer);

        if (tokenizer.currentToken.type != XmlToken.Type.EOF)
            throw new ParseException(XmlToken.Type.EOF, tokenizer.currentToken);

        return tag;
    }

    private static XmlTag parseTag(XmlTokenizer tokenizer)
    {
        XmlToken token = tokenizer.currentToken;

        if (token == null)
            token = tokenizer.extract();

        if (token.type != XmlToken.Type.TAG_BEGIN)
            throw new ParseException(XmlToken.Type.TAG_BEGIN, token);

        // Consume the begin token
        token = tokenizer.extract();

        if (token.type != XmlToken.Type.NAME)
            throw new ParseException(XmlToken.Type.NAME, token);

        String name = token.text;
        XmlTag tag = new XmlTag(name);

        // Consume the name token
        token = tokenizer.extract();

        // If the next token here is a NAME too, then we have to extract attributes
        while (token.type == XmlToken.Type.NAME)
        {
            XmlAttribute attribute = parseAttribute(tokenizer);
            tag.attributes.put(attribute.name, attribute);
            token = tokenizer.extract();
        }

        // Now the token should be either TAG_END or SIMPLE_TAG_CLOSE
        if (token.type == XmlToken.Type.SIMPLE_TAG_CLOSE)
        {
            tokenizer.extract();
            return tag;
        }

        if (token.type != XmlToken.Type.TAG_END)
            throw new ParseException(XmlToken.Type.TAG_END, token);

        // Now consume the TAG_END token
        token = tokenizer.extract();

        // Now we should be looping until we reach a TAG_CLOSE token
        while (token.type != XmlToken.Type.TAG_CLOSE)
        {
            // It might be text token
            if (token.type == XmlToken.Type.TEXT)
            {
                tag.text += token.text
                                    .replaceAll("&lt;", "<")
                                    .replaceAll("&gt;", ">")
                                    .replaceAll("&apos;", "'")
                                    .replaceAll("&quot;", "\"")
                                    .replaceAll("&amp;", "&") + " ";
            }

            // It might be a CDATA_BEGIN token
            else if (token.type == XmlToken.Type.CDATA_BEGIN)
            {
                token = tokenizer.extract();

                if (token.type != XmlToken.Type.TEXT)
                    throw new ParseException(XmlToken.Type.TEXT, token);

                tag.text += token.text + " ";

                token = tokenizer.extract();

                if (token.type != XmlToken.Type.CDATA_END)
                    throw new ParseException(XmlToken.Type.CDATA_END, token);
            }

            // It might be the beginning of a new tag
            else if (token.type == XmlToken.Type.TAG_BEGIN)
            {
                tag.children.add(parseTag(tokenizer));
                token = tokenizer.currentToken;
                continue;
            }

            else
                throw new ParseException(XmlToken.Type.TEXT, token);

            token = tokenizer.extract();
        }

        // Consume the TAG_CLOSE token
        token = tokenizer.extract();

        if (token.type != XmlToken.Type.NAME)
            throw new ParseException(XmlToken.Type.NAME, token);

        // Validate, the closing tag name should be the same as the opening tag name
        if (!token.text.equals(tag.name))
            throw new ParseException("Error at " + token.lineFrom + ":" + token.start +
                                     ": Tag name not matched.");

        token = tokenizer.extract();

        if (token.type != XmlToken.Type.TAG_END)
            throw new ParseException(XmlToken.Type.TAG_END, token);

        // Consume the TAG_END token
        tokenizer.extract();

        // Trim the tag text
        tag.text = tag.text.trim();

        return tag;
    }

    private static XmlAttribute parseAttribute(XmlTokenizer tokenizer)
    {
        String name = tokenizer.currentToken.text;
        XmlToken token = tokenizer.extract();

        if (token.type != XmlToken.Type.EQUALS)
            throw new ParseException(XmlToken.Type.EQUALS, token);

        token = tokenizer.extract();

        if (token.type != XmlToken.Type.STRING)
            throw new ParseException(XmlToken.Type.STRING, token);

        return new XmlAttribute(name, token.text);
    }
}
