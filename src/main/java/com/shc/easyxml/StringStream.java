package com.shc.easyxml;

/**
 * A helper class used to keep track of the line numbers and column numbers of the input.
 *
 * @author Sri Harsha Chilakapati
 */
class StringStream
{
    final static char EOF = '\0';

    int line = 1;
    int column;
    int index;

    private String input;

    StringStream(String input)
    {
        this.input = input;
    }

    char getCurrentChar()
    {
        if (index >= input.length())
            return EOF;

        return input.charAt(index);
    }

    char getNextChar()
    {
        if (index + 1 >= input.length())
            return EOF;

        column++;
        index++;

        char ch = input.charAt(index);

        if (ch == '\n')
        {
            column = 0;
            line++;
        }

        return ch;
    }

    char peekNextChar()
    {
        if (index + 1 >= input.length())
            return EOF;

        return input.charAt(index + 1);
    }
}
