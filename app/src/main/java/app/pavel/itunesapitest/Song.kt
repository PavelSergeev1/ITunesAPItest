package app.pavel.itunesapitest

class Song {
    lateinit var trackName: String
    lateinit var discNumber: String
    lateinit var trackNumber: String
    lateinit var trackTimeMillis: String
    lateinit var trackExplicitness: String
    lateinit var isStreamable: String

    constructor(trackName: String,
                trackPrice: String,
                discNumber: String,
                trackNumber: String,
                trackTimeMillis: String,
                trackExplicitness: String,
                isStreamable: String)

    constructor()
}