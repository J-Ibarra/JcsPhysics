package engine;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.nglfwGetFramebufferSize;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;
import static org.lwjgl.system.MemoryUtil.memAddress;

public class Font {
    private static ByteBuffer charBuffer;
    private static int quads;

    public static void render(String text, long window, int x, int y, Vector3f color) {
        charBuffer = BufferUtils.createByteBuffer(text.length() * 270);
        quads = stb_easy_font_print(0, 0, text, null, charBuffer);

        IntBuffer framebufferSize = BufferUtils.createIntBuffer(2);
        nglfwGetFramebufferSize(window, memAddress(framebufferSize), memAddress(framebufferSize) + 4);
        int width = framebufferSize.get(0);
        int height = framebufferSize.get(1);

        glDisable(GL_CULL_FACE);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, width, height, 0.0, -1.0, 1.0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 16, charBuffer);

        glColor3f(color.x, color.y, color.z);
        glPushMatrix();
        glTranslated(x, y, 0.0f);
        glDrawArrays(GL_QUADS, 0, quads * 4);
        glPopMatrix();

        charBuffer.clear();
        quads = -1;
        glDisableClientState(GL_VERTEX_ARRAY);
        glEnable(GL_CULL_FACE);
    }
}
