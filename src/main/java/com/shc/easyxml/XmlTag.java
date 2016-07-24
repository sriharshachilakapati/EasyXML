package com.shc.easyxml;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>An XML tag is the root concept of the XML data specification. It is a container of text, can have some attributes,
 * and is also a recursive container of XML tags, that is it can have children. The attributes are a map and the
 * children are in a list.</p>
 *
 * @author Sri Harsha Chilakapati
 */
public class XmlTag
{
    /**
     * The name of the XmlTag.
     */
    public String name;

    /**
     * The text of the XmlTag. Also contains any CData sections.
     */
    public String text;

    /**
     * A set of XmlAttributes that are present on this tag.
     */
    public final Set<XmlAttribute> attributes = new LinkedHashSet<>();

    /**
     * A list of children XmlTags of this tag.
     */
    public final List<XmlTag> children = new ArrayList<>();

    /**
     * Constructs a new XmlTag with a specified name. The value is initialized to an empty string and not {@code null}.
     *
     * @param name The name of the tag.
     */
    public XmlTag(String name)
    {
        this.name = name;
        this.text = "";
    }

    /**
     * Returns a new list of child tags which have the specified name. The hierarchy is not modified.
     *
     * @param name The name of the tag to retrieve.
     *
     * @return A list of child tags with the specified name.
     */
    public List<XmlTag> getTagsByName(String name)
    {
        List<XmlTag> tags = new ArrayList<>();

        for (XmlTag tag : children)
            if (tag.name.equals(name))
                tags.add(tag);

        return tags;
    }

    /**
     * Returns an attribute with the specified name. If such attribute does not exist, we return null.
     *
     * @param name The name of the attribute.
     *
     * @return An XmlAttribute with the specified name.
     */
    public XmlAttribute getAttribute(String name)
    {
        for (XmlAttribute attribute : attributes)
            if (attribute.name.equals(name))
                return attribute;

        return null;
    }
}
