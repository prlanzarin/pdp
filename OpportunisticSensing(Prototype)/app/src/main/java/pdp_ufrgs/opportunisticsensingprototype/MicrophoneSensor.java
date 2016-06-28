package pdp_ufrgs.opportunisticsensingprototype;

import android.media.MediaRecorder;

import java.io.IOException;

public class MicrophoneSensor {
        private MediaRecorder mRecorder = null;

        public MicrophoneSensor() {
        }

        public void start() {
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
                try {
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mRecorder.setOutputFile("/dev/null");
                    mRecorder.prepare();
                    mRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop() {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
        }

        public double getAmplitude() {
            if (mRecorder != null)
                return  mRecorder.getMaxAmplitude();
            else
                return 0;

        }
}
