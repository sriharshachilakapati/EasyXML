package com.shc.easyxml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 */
public class XmlTag
{
    public String name;
    public String text;

    public Map<String, XmlAttribute> attributes = new HashMap<>();
    public List<XmlTag>              children   = new ArrayList<>();

    public XmlTag(String name)
    {
        this.name = name;
        this.text = "";
    }

    public List<XmlTag> getTagsByName(String name)
    {
        List<XmlTag> tags = new ArrayList<>();

        for (XmlTag tag : children)
            if (tag.name.equals(name))
                tags.add(tag);

        return tags;
    }
}
