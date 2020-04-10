// Output a fixed-frequency sine wave to a WAVE file.
// Adapted from demo by WolinLabs
//   http://www.wolinlabs.com/blog/java.sine.wave.html
// and demo from ProgramCreek
//   https://www.programcreek.com/java-api-examples/?
//     code=AdoptOpenJDK/openjdk-jdk10/openjdk-jdk10-master/jdk/test/
//     javax/sound/sampled/spi/AudioFileWriter/AiffSampleRate.java

import java.io.*;
import java.nio.ByteBuffer;
import javax.sound.sampled.*;

public class writesine {

    public static void main(String[] args)
        throws IOException, LineUnavailableException
    {

        final int SAMPLING_RATE = 44100;  // Audio sampling rate.
        final int SAMPLE_SIZE = 2; // Audio sample size in bytes.
        // Number of seconds as samples.
        final int NSAMPLES = 5 * SAMPLING_RATE; 

        double fFreq = 440; // Frequency of sine wave in hz.

        // Create a sample buffer.
        ByteBuffer cBuf = ByteBuffer.allocate(2 * NSAMPLES);
        for (int i=0; i < NSAMPLES; i++) {
            double t = 2 * Math.PI * fFreq * i / SAMPLING_RATE;
            cBuf.putShort((short) ((Short.MAX_VALUE - 1) * Math.sin(t)));
        }
        ByteArrayInputStream buf = new ByteArrayInputStream(cBuf.array());

        // Open up audio output, using given sampling rate,
        // 16 bit samples, mono, and big-endian byte ordering
        // (because Java's ByteBuffer is big-endian).
        AudioFormat sample_format = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            SAMPLING_RATE, 8 * SAMPLE_SIZE, 1,
            SAMPLE_SIZE, SAMPLING_RATE, true
        );
        AudioInputStream aBuf =
            new AudioInputStream(buf, sample_format, NSAMPLES);

        // Write the samples and finish.
        File wavfile = new File(args[0]);
        AudioSystem.write(aBuf, AudioFileFormat.Type.WAVE, wavfile);
    }
}
