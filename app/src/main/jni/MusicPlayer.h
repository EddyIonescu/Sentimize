//
// Created by Eddy on 10/23/16.
//

#include "../../../../../SuperpoweredSDK/Superpowered/SuperpoweredAdvancedAudioPlayer.h"
#include "../../../../../SuperpoweredSDK/Superpowered/AndroidIO/SuperpoweredAndroidAudioIO.h"

#ifndef SENTIMIZE_MUSICPLAYER_H
#define SENTIMIZE_MUSICPLAYER_H

#endif //SENTIMIZE_MUSICPLAYER_H

class MusicPlayer {
public:

    MusicPlayer(unsigned int samplerate, unsigned int buffersize, const char *path);
    ~MusicPlayer();
    void onPlayPause(bool play);
    void setPosition(int position);
    bool process(short int *output, unsigned int numberOfSamples);

private:
    SuperpoweredAdvancedAudioPlayer *player;
    SuperpoweredAndroidAudioIO *audioSystem;
    float *stereoBuffer;
};