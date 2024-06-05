/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audio;

//import com.sun.speech.freetts.Voice;
//import com.sun.speech.freetts.VoiceManager;
import static java.lang.System.setProperty;

/**
 *
 * @author Zahoor Ali
 */
public class Speech {

//    private String VOICENAME = "mbrola_us1";
//    private Voice voice;

    public static boolean running = false;

    public Speech(String v) {
        if (v != null) {
//            VOICENAME = v;
        }
        setProperty("mbrola.base", "E:\\Study\\University\\FYP\\Code\\mbrola");
//        VoiceManager vm = VoiceManager.getInstance();
//        voice = vm.getVoice(VOICENAME);
//        voice.allocate();

        //voice.speak(s);
    }

    public void speak(String speech) {
//        voice.speak(speech);
    }

    public void speak(String speech, float delay) {
//        voice.setDurationStretch(delay);
//        voice.speak(speech);
    }
}
