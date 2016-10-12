//
// Created by Eddy on 10/10/16.
//

#include <jni.h>
#include <stdlib.h>


#include <SuperpoweredRecorder.h>
#include <SuperpoweredAudioBuffers.h>
#include "SuperpoweredDecoder.h"
#include "SuperpoweredAnalyzer.h"
#include "SuperpoweredSimple.h"
#include <android/log.h>
#include "../../../../SuperpoweredSDK/Superpowered/SuperpoweredSimple.h"
#include "../../../../SuperpoweredSDK/Superpowered/SuperpoweredDecoder.h"
#include "../../../../SuperpoweredSDK/Superpowered/SuperpoweredAnalyzer.h"


#define  LOG_TAG    "testjni"
#define  ALOG(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
extern "C" JNIEXPORT double Java_me_sentimize_sentimize_Utils_LocalMusicAnalysis_SuperpoweredAnalyzer(
        JNIEnv *__unused javaEnvironment, jobject __unused obj, jfloat samplerate, jstring pathstr) {
    // Open the input file.
    SuperpoweredDecoder *decoder = new SuperpoweredDecoder();

    const char *path = javaEnvironment->GetStringUTFChars(pathstr, false);
    const char *openError = decoder->open(path, false, 0, 0);
    if (openError) {
        ALOG(path);
        ALOG(openError);
        //NSLog(@"open error: %s", openError);
        delete decoder;
        return -1;
    };

    // Create the analyzer.
    SuperpoweredOfflineAnalyzer *analyzer = new SuperpoweredOfflineAnalyzer(decoder->samplerate, 0, decoder->durationSeconds);

    // Create a buffer for the 16-bit integer samples coming from the decoder.
    short int *intBuffer = (short int *) malloc(
            decoder->samplesPerFrame * 2 * sizeof(short int) + 16384);
    // Create a buffer for the 32-bit floating point samples required by the effect.
    float *floatBuffer = (float *) malloc(decoder->samplesPerFrame * 2 * sizeof(float) + 1024);

    double progress;
    // Processing.
    while (1) {
        // Decode one frame. samplesDecoded will be overwritten with the actual decoded number of samples.
        unsigned int samplesDecoded = decoder->samplesPerFrame;
        if (decoder->decode(intBuffer, &samplesDecoded) == SUPERPOWEREDDECODER_ERROR) break;
        if (samplesDecoded < 1) break;

        // Convert the decoded PCM samples from 16-bit integer to 32-bit floating point.
        SuperpoweredShortIntToFloat(intBuffer, floatBuffer, samplesDecoded);

        // Submit samples to the analyzer.
        analyzer->process(floatBuffer, samplesDecoded);

        // Update the progress indicator.
        progress = (double) decoder->samplePosition / (double) decoder->durationSamples;
    };

    // Get the result.
    unsigned char *averageWaveform = NULL, *lowWaveform = NULL, *midWaveform = NULL, *highWaveform = NULL, *peakWaveform = NULL, *notes = NULL;
    int waveformSize, overviewSize, keyIndex;
    char *overviewWaveform = NULL;
    float loudpartsAverageDecibel, peakDecibel, bpm, averageDecibel, beatgridStartMs = 0;
    analyzer->getresults(&averageWaveform, &peakWaveform, &lowWaveform, &midWaveform, &highWaveform,
                         &notes, &waveformSize, &overviewWaveform, &overviewSize, &averageDecibel,
                         &loudpartsAverageDecibel, &peakDecibel, &bpm, &beatgridStartMs, &keyIndex);

    ALOG(musicalChordNames[keyIndex]);
    // Cleanup.
    delete decoder;
    delete analyzer;
    free(intBuffer);
    free(floatBuffer);

    // Do something with the result.
    //@"Bpm is %f, average loudness is %f db, peak volume is %f db.", bpm, loudpartsAverageDecibel, peakDecibel);

    // Done with the result, free memory.
    if (averageWaveform) free(averageWaveform);
    if (lowWaveform) free(lowWaveform);
    if (midWaveform) free(midWaveform);
    if (highWaveform) free(highWaveform);
    if (peakWaveform) free(peakWaveform);
    if (notes) free(notes);
    if (overviewWaveform) free(overviewWaveform);

    return bpm;

}