package xyz.volartrix.OGLG;

import io.github.chiraagchakravarthy.lwjgl_vectorized_text.VectorFont;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.volartrix.OGLG.components.Text;
import xyz.volartrix.OGLG.ui.TextRenderer;
import xyz.volartrix.OGLG.util.CrashHandler;
import xyz.volartrix.OGLG.window.KeyboardHandler;
import xyz.volartrix.OGLG.window.Window;
import xyz.volartrix.OGLG.window.WindowInfo;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

    public static String[] args;

    public static Window WINDOW;
    public static final Logger LOGGER = LoggerFactory.getLogger("Unnamed OpenGL Game");

    public void run() {

        LOGGER.info("Using LWJGL version {}", Version.getVersion());
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArgs = runtimeMxBean.getInputArguments();
        LOGGER.info("JVM Args: ");
        for (String jvmArg : jvmArgs) {
            System.out.println("\t" + jvmArg);
        }

        LOGGER.info("OS: {}", System.getProperty("os.name"));
        LOGGER.info("Java Version: {}", System.getProperty("java.version"));
        LOGGER.info("Architecture: {}", System.getProperty("os.arch"));


        HashMap<Integer, Integer> hints = new HashMap<>();

        hints.put(GLFW_CONTEXT_VERSION_MAJOR, 3);
        hints.put(GLFW_CONTEXT_VERSION_MINOR, 3);
        hints.put(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        hints.put(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        hints.put(GLFW_VISIBLE, GLFW_FALSE);
        hints.put(GLFW_RESIZABLE, GLFW_TRUE);

        KeyboardHandler keyboardHandler = new KeyboardHandler();

        WINDOW = new Window(new WindowInfo(1280, 720,
                1, keyboardHandler,
                System.err, "Hello World",
                hints));


        WINDOW.setup();

        renderLoop();

        WINDOW.destroy();

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void renderLoop() {
        GLCapabilities caps = GL.createCapabilities();
        if (caps == null) {
            LOGGER.error("Error getting GL caps");
            return;
        }
        if (!caps.GL_ARB_shader_objects) {
            LOGGER.error("ARB_shader_objects is not supported!");
            return;
        }

        GL.setCapabilities(caps);

        LOGGER.info("Initialized OpenGL ({})", glGetString(GL_VERSION));

        VectorFont font = new VectorFont("/assets/font.ttf");
        TextRenderer textRenderer = new TextRenderer(font);

        double lastTime = glfwGetTime();
        int frameCount = 0;
        int fps = 0;

        glClearColor(0, 0, 0, 1.0f);

        while (!glfwWindowShouldClose(WINDOW.getHandle())) {
            frameCount++;

            // Clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            double currentTime = glfwGetTime();
            frameCount++;
            if (currentTime - lastTime >= 1.0) {
                fps = frameCount;
                frameCount = 0;
                lastTime = currentTime;
            }

            textRenderer.drawText(Text.literal("The quick brown fox jumps over the lazy dog"), 10, 10, 0xFFFFFFFF, 12);

            // Swap the color buffers
            glfwSwapBuffers(WINDOW.getHandle());

            // Poll for window events
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        LOGGER.info("Unnamed OpenGL Game Version alpha-0.1");
        Thread currentThread = Thread.currentThread();
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler::crashHandler);
        LOGGER.info("Set crash to xyz.volartrix.OGLG.util.CrashHandler#crashHandler");

        Main.args = args;

        new Main().run();
    }
}
