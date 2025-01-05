package xyz.volartrix.OGLG.window;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.glfw.GLFWVidMode;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private WindowInfo info;
    private long handle;

    public Window(WindowInfo initInfo) {
        this.info = initInfo;
    }

    public void setup() {
        GLFWErrorCallback.createPrint(info.errorStream()).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        for (var entry : info.hints().entrySet()) {
            glfwWindowHint(entry.getKey(), entry.getValue());
        }

        handle = glfwCreateWindow(info.width(), info.height(), info.title(), NULL, NULL);
        if (handle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(handle, info.keyboardHandler());

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(handle, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            assert vidmode != null;
            glfwSetWindowPos(
                    handle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(handle);
        glfwSwapInterval(info.vsync());
        glfwShowWindow(handle);
    }

    public long getHandle() {
        return handle;
    }

    // set new keyboard handler
    public void setKeyCallback(KeyboardHandler keyboardHandler) {
        info = new WindowInfo(info.width(), info.height(), info.vsync(), keyboardHandler, info.errorStream(), info.title(), info.hints());
    }

    // enable / disable vsync
    public void setVsync(int vsync) {
        glfwSwapInterval(vsync);
    }

    // update window size
    public void setSize(int width, int height) {
        info = new WindowInfo(width, height, info.vsync(), info.keyboardHandler(), info.errorStream(), info.title(), info.hints());
    }

    // update window title
    public void setTitle(String title) {
        glfwSetWindowTitle(handle, title);
    }

    public long getWindowHandle() {
        return handle;
    }

    // destroy the window
    public void destroy() {
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
    }
}
