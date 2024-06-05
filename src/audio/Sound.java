/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audio;

//import csnd6.Csound;
//import csnd6.CsoundArgVList;
//import csnd6.CsoundMYFLTArray;
//import csnd6.SWIGTYPE_p_CSOUND_;
//import csnd6.SWIGTYPE_p_p_double;
//import csnd6.SWIGTYPE_p_void;
//import csnd6.csnd6;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

/**
 *
 * @author Zahoor Ali
 */
public class Sound implements Runnable {

//    SWIGTYPE_p_void myvoid;
//    SWIGTYPE_p_CSOUND_ mycsound = csnd6.csoundCreate(myvoid);
//    Csound csound = new Csound();
//    CsoundArgVList args = new CsoundArgVList();
//    CsoundMYFLTArray myfltarray = new CsoundMYFLTArray();

    boolean on = false;
    boolean pause = false;

    double freq = 600;
    int depth = 1;

    String src, format;
    int delay = 0;

//    public static final int p1 = 1;
//    public static final double p3 = 0.05;
//    public static final double p4 = 0.5;
    public double result1;
    public double result2;
    public static boolean running = false;

    public Sound(String src, String format) {
        this.src = src;
        this.format = format;
    }

    public Sound(String src, String format, int delay) {
        this.src = src;
        this.format = format;
        this.delay = delay;
    }

    public Sound(String src, String format, int delay, int depth) {
        this.src = src;
        this.format = format;
        this.delay = delay;
        this.depth = depth;
    }

    public void run() {
        while (Sound.running || Speech.running) {
        }
        running = true;
        if (delay != 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (format.contains("wav")) {
            if (src != null) {
                try {
                    this.playWav(src);
                } catch (IOException ex) {
                    Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    this.playWav();
                } catch (IOException ex) {
                    Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (format.contains("csound")) {
            this.playSound();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
        running = false;
    }

    public void playSound() {
//        csnd6.csoundInitialize(
//                csnd6.CSOUNDINIT_NO_ATEXIT | csnd6.CSOUNDINIT_NO_SIGNAL_HANDLER);

        // Defining our Csound ORC code within a String
        String orc = "sr=44100\n"
                + "ksmps=32\n"
                + "nchnls=2\n"
                + "0dbfs=1\n"
                + "\n"
                + "instr 1 \n"
                + "ipch = cps2pch(p5, 12)\n"
                + "kenv linsegr 0, .05, 1, .05, .7, .4, 0\n"
                + "aout vco2 p4 * kenv, ipch \n"
                + "aout moogladder aout, 2000, 0.25\n"
                + "outs aout, aout\n"
                + "endin\n";

        String sco = getParameters(depth);

//        Csound c = new Csound();

//        c.SetOption("-odac");
//        c.CompileOrc(orc);
//
//        // Compile the Csound SCO String
//        c.ReadScore(sco);
//        c.Start();
//        while (c.PerformKsmps() == 0) {
//            // pass for now
//        }
//        c.Stop();
//        c.Cleanup();

//        args.Append("csound");
//        args.Append("-s");
//        args.Append("-d");
//        args.Append("-odevaudio");
//        args.Append("-b4096");
//        args.Append("-B4096");
//        //args.Append("-+rtaudio=ALSA");
//        args.Append("-+rtaudio=portaudio");
//
//        src = "src/resources/sound/EightEightyHz.csd";
//        args.Append(src);
//
//        try {
//            int result = csound.Compile(args.argc(), args.argv());
//            //System.out.println("Resultttttttttttttttttttttttt " + result);
//            //int myinputch = csnd6Constants.CSOUND_EXITJMP_SUCCESS;
//            //int mycontch = csnd6Constants.CSOUND_CONTROL_CHANNEL;
//
//            SWIGTYPE_p_p_double myptr = myfltarray.GetPtr();
//
//            if (result == 0) {
//                while (csound.PerformKsmps() == 0) {
//                    if (csnd6.csoundGetChannelPtr(mycsound, myptr,
//                            "pitch", 0) == 0) {
//                        csound.SetChannel("pitch", freq);
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //java.lang.System.err.println("Could not Perform...\n");
//        //csound.Stop();
//        //csound.Cleanup();
//        csound.Reset();
//        // java.lang.System.exit(1);
    }

    public String getParameters(Integer depth) {
        result1 = depth * 0.25;
//       result2 = depth * 0.01;
        StringBuilder sco2 = new StringBuilder();

        sco2.append(String.format("i1 %g .25 0.5 8.%02d\n", result1, depth));

        return sco2.toString();
    }

    public void playWav() throws FileNotFoundException, IOException {
        playWav(null);
    }

    public void playWav(String src) throws FileNotFoundException, IOException {
        String source;
        Pattern p = Pattern.compile("src/resources/sound");

        if (src == null) {
            //String gongFile = "D:\\Semester 8 Courses\\FYP\\Code (TRIDENT)\\FinalProject\\src\\file\\resources\\Audio\\rootNodeCreatedSpeech.wav";
            source = "src/resources/sound/rootNodeSound.wav";
        } else if (!p.matcher(src).find()) {
            source = "src/resources/sound/" + src;
        } else {
            source = src;
        }
        InputStream in = new FileInputStream(source);

//        // create an audiostream from the inputstream
//        AudioStream audioStream = new AudioStream(in);
//
//        // play the audio clip with the audioplayer class
//        AudioPlayer.player.start(audioStream);

    }
}
