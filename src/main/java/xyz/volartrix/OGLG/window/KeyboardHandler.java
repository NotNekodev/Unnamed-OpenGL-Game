package xyz.volartrix.OGLG.window;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

// simple keyboard handler class
public class KeyboardHandler implements GLFWKeyCallbackI {

    public KeyboardHandler() {
    } // cool public constructor :cool:

    // maps for handlers (pressed) for handler (holding) and key states (pressed or not)
    private final Map<Integer, Runnable> handlers = new HashMap<>();
    private final Map<Integer, Runnable> holdingHandlers = new HashMap<>();
    private final Map<Integer, Boolean> keyStates = new HashMap<>();

    // adds a normal handler
    public void addHandler(Runnable handler, int key) {
        handlers.put(key, handler);
    }

    // removes a normal handler
    public void removeHandler(int key) {
        handlers.remove(key);
    }

    // adds a holding handler
    public void addHoldingHandler(Runnable handler, int key) {
        holdingHandlers.put(key, handler);
    }

    // removes a holding handler
    public void removeHoldingHandler(int key) {
        holdingHandlers.remove(key);
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) { // function provided by GLFWKeyCallbackI
        // Update key state
        if (action == GLFW_PRESS) {
            keyStates.put(key, true);
        } else if (action == GLFW_RELEASE) {
            keyStates.put(key, false);
            if (handlers.containsKey(key)) {
                handlers.get(key).run();
            }
        }
    }

    // handling hold keys
    public void handleHeldKeys() {
        for (Map.Entry<Integer, Runnable> entry : holdingHandlers.entrySet()) {
            if (keyStates.getOrDefault(entry.getKey(), false)) {
                entry.getValue().run();
            }
        }
    }
}
