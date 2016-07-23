# EasyXML

An easy to use XML parser for Java. This parser is written for educational purposes but it worked well, so I'm using it in my [SilenceEngine](http://silenceengine.goharsha.com) to parse the Tiled maps.

## What is supported?

Note that this is not a complete XML parser, so only a basic functionality is supported. Here are a list of features that are supported:

  - Tags
  - Attributes
  - Text
  - CData

If you need any more features, please use real libraries. Three small reasons why I like my parser is that

  1. It is light-weight. Just a 11 KB JAR file.
  2. It works on desktop/android and also with GWT.
  3. I just don't need all the complete XML specification.

And this is why I'm continuing to use this library. Note, this doesn't validate your XML and tries to be forgiving, but in case you have errors, it will do proper error reporting with line numbers.

## Installation

This is not in Maven yet (will go in a few days after writing more unit tests), so until then you have to compile it yourself and add the JAR to the build classpath of your app.

## Using the library

Everything in this library that you will use really is the `XmlTag` class and the `Xml` class. You also need to do your own IO to read the file because the parser operates on Strings. Let's take an example XML.

~~~xml
<?xml version="1.0" ?>
<test/>
~~~

The above is the simplest XML document with two parts. The first one is the prologue and the second is the tag definition of `test`. You will parse the XML in this fashion.

~~~java
XmlTag tag = Xml.parse(readFileToString("test1.xml"));
System.out.println(tag.name); // Will be test
~~~

Now let us take a look at accessing the attributes of a tag. The following is an XML document which defines some attributes in the tag.

~~~xml
<?xml version="1.0"?>
<test myattrib="Hello"/>
~~~

And now in Java, you can do the following to access the attribute.

~~~java
System.out.println(tag.attributes.get("myattrib").value); // Hello
~~~

This also supports error checking. For example, let's try to parse an XML file with an error like this.

~~~xml
<?xml version="1.0" ?>
<test attribute="Testing error"
~~~

Which results in the following error which is pretty descriptive.

~~~
Exception in thread "main" com.shc.easyxml.ParseException: Expected TAG_END; got EOF() at 3:0-0
	at com.shc.easyxml.Xml.parseTag(Xml.java:63)
	at com.shc.easyxml.Xml.parse(Xml.java:17)
	at com.shc.easyxml.test.XmlTokenTest.main(XmlTokenTest.java:22)
~~~

The parser also supports tags in tags, and also CData sections in text.

## Tokenization

Another thing that this library offers other than other libraries is that you can see the tokens that are identified by using the `XmlTokenizer` class.

~~~java
XmlTokenizer tokenizer = new XmlTokenizer(xml);
XmlToken token;

while ((token = tokenizer.extract()).type != XmlToken.Type.EOF)
    System.out.println(token);
~~~

will output the following for the previous XML.

~~~
XmlToken{type=TAG_BEGIN, text='<', line=2, start=1, end=2}
XmlToken{type=NAME, text='test', line=2, start=2, end=6}
XmlToken{type=NAME, text='attribute', line=2, start=7, end=16}
XmlToken{type=EQUALS, text='=', line=2, start=16, end=17}
XmlToken{type=STRING, text='Testing error', line=2, start=17, end=-1}
~~~

Thanks for using this library!
