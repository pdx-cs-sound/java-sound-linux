# Using javax.sound with Debian
Bart Massey 2020-04-07

* Install the Debian packages `libtritonus-java` and
  `libpulse-java`:

        apt install libtritonus-java libpulse-java

* Set your `CLASSPATH`:

        TRITONUS=/usr/share/java/tritonus_share.jar
        PULSEJAVA=/usr/share/java/pulse-java.jar
        export CLASSPATH=$TRITONUS:$PULSEJAVA:.

  (You could also set your classpath for compilation with
  the `-cp` option for `javac`.)

  *WARNING:* If you compile without your classpath set, your
  code will compile but you will get a version of
  `javax.sound` that doesn't actually use PulseAudio but
  uses a null stream instead. Don't waste your life
  debugging this as I did.

* Make sure the native libraries can be loaded.

    * Oracle Java: set your `LD_LIBRARY_PATH`

            export LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu/jni

      or whatever is appropriate for your architecture. This
      is needed because Oracle's `java.library.path` doesn't
      correspond to anything on a Debian box, at least as of
      Java 8 which is the latest I have to test.

      (You could also fix this, as I did, by becoming root and
      
            mkdir -p /usr/java/packages/lib/amd64
            cd /usr/java/packages/lib/amd64
            ln -s /usr/lib/x86_64-linux-gnu/jni/*.so .

      but this is kind of fragile.

      There are other possible fixes: see
      [this thread](https://stackoverflow.com/questions/6736235/set-java-system-properties-with-a-configuration-file)
      on StackOverflow for a detailed discussion.)

    * OpenJDK: Should just work — no magic necessary.

* Compile and run your java code. A sine wave player demo is
  here.

        javac playsine.java
        java playsine


  A sine wave file writer demo is also here.

        javac writesine.java
        java writesine sine.wav

  These examples have been run with Oracle Java 8 and
  OpenJDK 11.

## Acknowledgments

Thanks to the authors of Tritonus and the Java PulseAudio
bindings. Thanks to WolinLabs for the sine wave demo.
Thanks to ProgramCreek for the WAVE writer demo.
