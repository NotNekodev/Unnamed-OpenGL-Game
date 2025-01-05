package xyz.volartrix.OGLG.ui;

import io.github.chiraagchakravarthy.lwjgl_vectorized_text.VectorFont;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import xyz.volartrix.OGLG.Main;
import xyz.volartrix.OGLG.components.Text;

import java.nio.IntBuffer;

import static io.github.chiraagchakravarthy.lwjgl_vectorized_text.TextRenderer.TextBoundType.BOUNDING_BOX;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;

public class TextRenderer {


    private final io.github.chiraagchakravarthy.lwjgl_vectorized_text.TextRenderer intRenderer;

    private static Vector4f argbToVector4f(int argbColor) {
        float a = ((argbColor >> 24) & 0xFF) / 255.0f; // Alpha component
        float r = ((argbColor >> 16) & 0xFF) / 255.0f; // Red component
        float g = ((argbColor >> 8) & 0xFF) / 255.0f;  // Green component
        float b = (argbColor & 0xFF) / 255.0f;         // Blue component

        return new Vector4f(r, g, b, a);
    }

    public TextRenderer(VectorFont font) {
        intRenderer = new io.github.chiraagchakravarthy.lwjgl_vectorized_text.TextRenderer(font);
    }

    public void drawText(Text text, int x, int y, int color, int size) {

        try (MemoryStack stack = MemoryStack.stackPush()) {

            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(Main.WINDOW.getHandle(), pWidth, pHeight);

            intRenderer.drawText2D(text.getTextString(),
                    x, (y + size),
                    size,
                    new Vector2f(-1, 1),
                    BOUNDING_BOX,
                    argbToVector4f(color)
            );

            intRenderer.render();
        }


    }
}
