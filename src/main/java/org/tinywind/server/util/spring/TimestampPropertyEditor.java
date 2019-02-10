package org.tinywind.server.util.spring;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Property editor for java.sql.Timestamp, supporting SimpleDateFormat.
 * <p>
 * Using default Constructor uses the pattern yyyy-MM-dd
 * Using the constructor with String, you can use your own pattern.
 * <p>
 * ref: http://adfinmunich.blogspot.kr/2011/04/how-to-write-sqltimestamppropertyeditor.html
 */
public class TimestampPropertyEditor extends PropertyEditorSupport {

    public static final String DEFAULT_BATCH_PATTERN = "yyyy-MM-dd";

    private final SimpleDateFormat sdf;

    /**
     * uses default pattern yyyy-MM-dd for date parsing.
     */
    public TimestampPropertyEditor() {
        this.sdf = new SimpleDateFormat(TimestampPropertyEditor.DEFAULT_BATCH_PATTERN);
    }

    /**
     * Uses the given pattern for dateparsing, see {@link SimpleDateFormat} for allowed patterns.
     *
     * @param pattern the pattern describing the date and time format
     * @see SimpleDateFormat#SimpleDateFormat(String)
     */
    public TimestampPropertyEditor(String pattern) {
        this.sdf = new SimpleDateFormat(pattern);
    }

    /**
     * Format the Timestamp as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        Timestamp value = (Timestamp) getValue();
        return (value != null ? this.sdf.format(value) : "");
    }

    /**
     * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        try {
            setValue(new Timestamp(this.sdf.parse(text).getTime()));
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
        }
    }
}