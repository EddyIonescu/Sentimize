//
// Created by Eddy on 10/23/16.
//

#include "MusicPlayer.h"
#include "../../../../../SuperpoweredSDK/Superpowered/SuperpoweredSimple.h"
#include "../../../../../SuperpoweredSDK/Superpowered/SuperpoweredAdvancedAudioPlayer.h"
#include "../../../../../SuperpoweredSDK/Superpowered/AndroidIO/SuperpoweredAndroidAudioIO.h"
#include <jni.h>
#include <stdio.h>
#include <android/log.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_AndroidConfiguration.h>
#include <malloc.h>
#include <thread>

#define  LOG_TAG    "MusicPlayer"
#define  ALOG(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

void playerEventCallback(void *clientData, SuperpoweredAdvancedAudioPlayerEvent event, void * __unused value) {
    if (event == SuperpoweredAdvancedAudioPlayerEvent_LoadSuccess) {
        ALOG("load success");
        // todo set volume using SuperpoweredSimple to ensure consistent volume between songs
    }
    else if(event == SuperpoweredAdvancedAudioPlayerEvent_DurationChanged){
        // todo update seekbar
        ALOG("duration changed");
    }
    else if(event == SuperpoweredAdvancedAudioPlayerEvent_LoopEnd){
        // todo go back - ask for next song
        ALOG("loop end");
    }
}

void MusicPlayer::setPosition(int position){
    player->setPosition(position, false, false);
}


bool MusicPlayer::process(short int *output, unsigned int numberOfSamples) {
    //ALOG("loc 7 processing");
    bool thereIsSound = player->process(stereoBuffer, false, numberOfSamples);
    //if(thereIsSound) ALOG("loc 6 process there is sound");
    // The stereoBuffer is ready now, let's put the finished audio into the requested buffers.
    audioWorking = thereIsSound;
    if (thereIsSound) SuperpoweredFloatToShortInt(stereoBuffer, output, numberOfSamples);
   // ALOG("loc 8 end processing");
    return thereIsSound;
}


static bool audioProcessing(void *clientdata, short int *audioIO, int numberOfSamples, int __unused samplerate) {
    return ((MusicPlayer *)clientdata)->process(audioIO, (unsigned int)numberOfSamples);
}

MusicPlayer::MusicPlayer(unsigned int samplerate, unsigned int buffersize, const char *path) {
    audioWorking = false;
    stereoBuffer = (float *)memalign(16, (buffersize + 16) * sizeof(float) * 2);
    ALOG("loc2 ctor");
    player = new SuperpoweredAdvancedAudioPlayer(&player , playerEventCallback, samplerate, 0);
    player->open(path);
    ALOG("loc3 ctor");
    audioSystem = new SuperpoweredAndroidAudioIO(samplerate, buffersize, false, true, audioProcessing, this, -1, SL_ANDROID_STREAM_MEDIA, buffersize * 2);
}

MusicPlayer::~MusicPlayer() {
    ALOG("loc 5 dtor");
    delete player;
    delete audioSystem;
    free(stereoBuffer);
}

void MusicPlayer::onPlayPause(bool play) {
    ALOG("loc4 - on play pause");
    if (!play) {
        player->pause();
    } else {
        player->play(false);
    };
}

bool MusicPlayer::getAudioWorking() {
    return audioWorking;
}

static MusicPlayer *sentiPlayer = NULL;

// plays the song - returns whether it is playing it (in case of AUDIO_OUTPUT_FLAG_FAST denied by client - unsupported sample rate)
extern "C" JNIEXPORT jboolean Java_me_sentimize_sentimize_Utils_Superpowered_MusicPlayer(JNIEnv *javaEnvironment, jobject __unused obj, jstring songstr, jint samplerate, jint buffersize ) {
    ALOG("loc1 entry");
    const char *path = javaEnvironment->GetStringUTFChars(songstr, false);
    ALOG(path);
    if(sentiPlayer != NULL) delete sentiPlayer; // if an existing song is already loaded
    sentiPlayer = new MusicPlayer((unsigned int)samplerate, (unsigned int)buffersize, path);
    return sentiPlayer->getAudioWorking();
}

extern "C" JNIEXPORT void Java_me_sentimize_sentimize_Utils_Superpowered_onPlayPause(JNIEnv * __unused javaEnvironment, jobject __unused obj, jboolean play) {
    if(sentiPlayer) sentiPlayer->onPlayPause(play);
}

extern "C" JNIEXPORT void Java_me_sentimize_sentimize_Utils_Superpowered_setProgress(JNIEnv * __unused javaEnvironment, jobject __unused obj, jint progress) {
    if(sentiPlayer) sentiPlayer->setPosition(progress);
}
