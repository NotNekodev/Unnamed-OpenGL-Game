package xyz.volartrix.OGLG.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

import static org.lwjgl.BufferUtils.createByteBuffer;

public class Utils {

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        // Get the resource as a stream
        try (InputStream source = Utils.class.getClassLoader().getResourceAsStream(resource)) {
            if (source == null) {
                throw new IOException("Resource not found: " + resource);
            }

            try (var rbc = Channels.newChannel(source)) {
                buffer = createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        ByteBuffer newBuffer = createByteBuffer(buffer.capacity() * 2);
                        buffer.flip();
                        newBuffer.put(buffer);
                        buffer = newBuffer;
                    }
                }

                buffer.flip();
            }
        }

        return buffer;
    }
}