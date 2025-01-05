package xyz.volartrix.OGLG.window;

import java.io.PrintStream;
import java.util.Map;

public record WindowInfo(int width, int height, int vsync,
                         KeyboardHandler keyboardHandler,
                         PrintStream errorStream, String title,
                         Map<Integer, Integer> hints) {
}
