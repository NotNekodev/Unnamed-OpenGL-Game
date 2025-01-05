package xyz.volartrix.OGLG.audio;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.stb.STBVorbisInfo;
import xyz.volartrix.OGLG.util.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static xyz.volartrix.OGLG.Main.LOGGER;

public class OggPlayer {

    private long device;
    private long context;

    public int source;
    private int buffer;

    public void init() {
        String defaultDeviceName = alcGetString(NULL, ALC_DEFAULT_DEVICE_SPECIFIER);
        LOGGER.info("Using default OpenAL device: {}", defaultDeviceName);

        device = alcOpenDevice(defaultDeviceName);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }

        context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        }

        alcMakeContextCurrent(context);
        AL.createCapabilities(ALC.createCapabilities(device));

        // Create a source
        source = alGenSources();
        alSourcef(source, AL_GAIN, 1.0f);
        alSourcef(source, AL_PITCH, 1.0f);
        alSource3f(source, AL_POSITION, 0.0f, 0.0f, 0.0f);
    }

    public void loadOgg(String filePath) throws IOException {
        // Decode OGG file
        ByteBuffer vorbis = Utils.ioResourceToByteBuffer(filePath, 256 * 1024);

        IntBuffer errorBuffer = BufferUtils.createIntBuffer(1); // Allocate error buffer on the heap
        long decoder = stb_vorbis_open_memory(vorbis, errorBuffer, null);
        if (decoder == NULL) {
            throw new RuntimeException("Failed to open OGG file. Error: " + errorBuffer.get(0));
        }

        STBVorbisInfo info = STBVorbisInfo.create(); // Allocate STBVorbisInfo on the heap
        stb_vorbis_get_info(decoder, info);

        int channels = info.channels();
        int sampleRate = info.sample_rate();

        // Determine the number of samples and allocate PCM buffer
        int numSamples = stb_vorbis_stream_length_in_samples(decoder);
        ShortBuffer pcm = BufferUtils.createShortBuffer(numSamples * channels);

        // Decode the OGG file into the PCM buffer
        stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm);
        stb_vorbis_close(decoder);

        // Create OpenAL buffer and load PCM data
        buffer = alGenBuffers();
        alBufferData(buffer,
                channels == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16,
                pcm,
                sampleRate);

        // Attach the buffer to the source
        alSourcei(source, AL_BUFFER, buffer);
    }

    public void play() {
        alSourcePlay(source);
    }

    public void cleanup() {
        alDeleteSources(source);
        alDeleteBuffers(buffer);

        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}
