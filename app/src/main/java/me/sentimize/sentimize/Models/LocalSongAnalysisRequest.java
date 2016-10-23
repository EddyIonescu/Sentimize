package me.sentimize.sentimize.Models;

import me.sentimize.sentimize.Models.LocalSong;

public class LocalSongAnalysisRequest{
    private LocalSong song;
    private LocalSongAnalysisRequest nextRequest;
    public LocalSongAnalysisRequest(LocalSong song, LocalSongAnalysisRequest nextRequest){
        this.song = song;
        this.nextRequest = nextRequest;
    }
    public LocalSong getSong(){
        return song;
    }
    public LocalSongAnalysisRequest getNextRequest(){
        return nextRequest;
    }
    public void setNextRequest(LocalSongAnalysisRequest request){
        nextRequest = request;
    }
}