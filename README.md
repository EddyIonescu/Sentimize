# Sentimize

Sentimize your day by listening to your songs such that you'll feel the way you want to feel.

Adjusting the positivity and energy level will generate a list of your songs (locally stored on the device) that are best suited.

<a href="https://play.google.com/store/apps/details?id=me.sentimize.sentimize&hl=en">Get it on Google Play</a>


Steps:
- Made List showing the songs (implementing SongFragment and a ListAdapter)
- Added the <a href="http://superpowered.com/">SuperPowered SDK</a> using the experimental version of Gradle (supports NDK)
- Interfaced with the SDK through C++ functions implemented using the JNI
- Made music-analysis an asyncronous task so that it runs in the background
- Interfaced with the Superpowered SDK to provide music-playback and also put that on its own thread running on a service
- Added media-player controls
- Added animated floating action buttons
- Added SQLite database for song caching
- Added deep-linking intent-filter to interface with chatbot

Next Steps:
- Facebook Messenger Chatbot Interface (Publish Sentima Chatbot)
- Smoothly-animated seek-bar
- Spotify Integration
- More advanced local music analysis

<iframe src="https://docs.google.com/document/d/1mEn01a33teceP42IE8Z2EYEMu-zLrcKgEfKIAbaYasM/pub?embedded=true"></iframe>
