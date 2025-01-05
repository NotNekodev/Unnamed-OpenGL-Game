package xyz.volartrix.OGLG.components;

import static xyz.volartrix.OGLG.Main.LOGGER;

public class Text {

    private final byte[] text;

    private Text(byte[] text) {
        this.text = text;
    }

    public static Text literal(String text) {
        return new Text(text.getBytes());
    }

    public static Text translated(String text) {
        LOGGER.warn("Text translation is not implemented yet!");
        return new Text(text.getBytes());
    }

    public byte[] getText() {
        return text;
    }

    public String getTextString() {
        return new String(getText());
    }

}
