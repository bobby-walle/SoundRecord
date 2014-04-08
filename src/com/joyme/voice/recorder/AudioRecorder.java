package com.joyme.voice.recorder;

import android.media.AudioRecord;

public class AudioRecorder implements Runnable {

    String LOG = "Recorder ";

    /**
     * 8000hz / s = 1s为8000个采样
     * 160 / 20  = 默认20ms为一个采样周期单位
     * <p/>
     * AudioFormat.ENCODING_PCM_16BIT = 1采样为16bit
     * 1个采样 = 16bit = 2byte
     * 160个采样 = 2560bit = 320byte
     * <p/>
     * 每隔采样实际读取的长度,
     *
     * @see(audioRecord.read(samples, 0, bufferSize);)
     */
    public static final int BUFFER_FRAM_SIZE = 320;
    // 音频采样
    private byte[] samples;
    // 采样读取长度(sample size)
    private int bufferSize = 0;
    // 录音长度(the size of audio read from recorder)
    private int bufferRead = 0;
    // 采用缓存区最小长度，通过AudioRecord获取
    private int audioBufSize = 0;

    // 录音状态
    private boolean isRecording = false;
    // 录音
    private AudioRecord audioRecord;

    public void startRecording() {

    }

    @Override
    public void run() {

    }
}
