package org.mule.module.datapack;

/**
 * Defines the supported delimiter chars.
 *
 * Passing around chars in XML is challenging, this enum wraps readable names with the char values
 */
public enum Delimiter
{
    LF, CR, QUOTE, COMMA, SPACE, TAB, POUND, BACKSLASH, PIPE, NULL, BACKSPACE, FORMFEED, ESCAPE, VERTICAL_TAB, ALERT;

    static char getChar(Delimiter d)
    {
        switch (d)
        {
            case LF:
                return '\n';
            case CR:
                return '\r';
            case QUOTE:
                return '"';
            case COMMA:
                return ',';
            case SPACE:
                return ' ';
            case TAB:
                return '\t';
            case POUND:
                return '#';
            case BACKSLASH:
                return '\\';
            case PIPE:
                return '|';
            case NULL:
                return '\0';
            case BACKSPACE:
                return '\b';
            case FORMFEED:
                return '\f';
            case ESCAPE:
                return '\u001B';
            case VERTICAL_TAB:
                return '\u000B';
            case ALERT:
                return '\u0007';
            default:
                return ',';
        }
    }
}
