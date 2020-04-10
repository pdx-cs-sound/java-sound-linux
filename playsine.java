// Output a fixed-frequency sine wave.
// Demo by WolinLabs.
// http://www.wolinlabs.com/blog/java.sine.wave.html

import java.nio.ByteBuffer;
import javax.sound.sampled.*;

public class playsine {

    //This is just an example -
    // you would want to handle LineUnavailable properly...
    public static void main(String[] args)
        throws InterruptedException, LineUnavailableException 
    {
        final int SAMPLING_RATE = 44100;  // Audio sampling rate.
        final int SAMPLE_SIZE = 2; // Audio sample size in bytes.

        SourceDataLine line;
        double fFreq = 440; // Frequency of sine wave in hz.

        // Open up audio output, using 44100hz sampling rate,
        // 16 bit samples, mono, and big endian byte ordering.
        AudioFormat format = new AudioFormat(SAMPLING_RATE, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)){
            System.out.println("Line matching " + info + " is not supported.");
            throw new LineUnavailableException();
        }

        line = (SourceDataLine)AudioSystem.getLine(info);
        line.open(format);  
        line.start();

        // Output for roughly 5 seconds.
        int ctBytesTotal = 2 * SAMPLING_RATE * 5;

        // On each pass main loop blocks in write if the
        // audio buffer is at least half full.  Main loop
        // creates audio samples for sine wave, runs until
        // we tell the thread to exit.  Each sample is
        // spaced 1 / SAMPLING_RATE apart in time.
        int ctBufSiz = line.getBufferSize() / 4;
        // Fraction of cycle between samples
        double fCycleInc = fFreq / SAMPLING_RATE;
        // Position through the sine wave as a percentage
        // (i.e. 0 to 1 is 0 to 2*PI).
        double fCyclePosition = 0;
        while (ctBytesTotal>0) {
            // Set up the samples to write.
            ByteBuffer cBuf = ByteBuffer.allocate(2 * ctBufSiz);
            for (int i=0; i < ctBufSiz; i++) {
                double t = 2.0 * Math.PI * fCyclePosition;
                short s = (short) (Short.MAX_VALUE * Math.sin(t));
                cBuf.putShort(s);
                fCyclePosition += fCycleInc;
            }

            // Write sine samples to the line buffer.  If
            // the audio buffer is full, this will block
            // until there is room.
            int nwrite = Math.min(2 * ctBufSiz, ctBytesTotal);
            byte[] bytes = cBuf.array();
            int nwritten = line.write(bytes, 0, nwrite);

            // Update total number of samples written.
            ctBytesTotal -= nwritten;
        }

        // Done playing the whole waveform, now wait until the
        // queued samples finish playing, then clean up and
        // exit.
        line.drain();                                         
        line.close();
    }
}
