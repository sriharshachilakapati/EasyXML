package com.shc.easyxml;

/**
 * <p>An attribute is simply a key-value pair. A tag can consist of as many attributes as possible. For example</p>
 *
 * <pre>
 *     &lt;test attrib1="value1" attrib2="value2"/&gt;
 * </pre>
 *
 * <p>In the above example, there are two attributes {@code attrib1="value1"} and {@code attrib2="value2"} which are
 * key value pairs.</p>
 *
 * @author Sri Harsha Chilakapati
 */
public class XmlAttribute
{
    /**
     * The name of the attribute.
     */
    public String name;

    /**
     * The value of the attribute.
     */
    public String value;

    /**
     * Create a new attribute by giving it a name and a value.
     *
     * @param name  The name of the attribute.
     * @param value The attribute value.
     */
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XmlAttribute attribute = (XmlAttribute) o;

        return (name != null ? name.equals(attribute.name) : attribute.name == null) &&
               (value != null ? value.equals(attribute.value) : attribute.value == null);
    }

    @Override
    public int hashCode()
    {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
