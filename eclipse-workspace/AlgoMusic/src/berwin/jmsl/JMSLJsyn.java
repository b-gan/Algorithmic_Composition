package berwin.jmsl;

import com.softsynth.jmsl.JMSLMixerContainer;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;

public class JMSLJsyn {

	JMSLMixerContainer mixer;
	void initJysnMusicDevice() {
		JSynMusicDevice dev =JSynMusicDevice.instance();
		dev.open();
	}

	void initMixer() {
		 mixer = new JMSLMixerContainer();
		 mixer.start();
	}
	public static void main(String[] args) {
		JMSLJSyn deni - bew JMSLSyn
	}
}
