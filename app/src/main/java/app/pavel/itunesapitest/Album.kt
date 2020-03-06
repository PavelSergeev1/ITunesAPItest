package app.pavel.itunesapitest

import java.util.*

public class Album {
    lateinit var albumTitle: String
    lateinit var artistName: String
    lateinit var artwork: String
    lateinit var genre: String
    lateinit var date: String
    lateinit var collectionId: String

    constructor(albumTitle: String,
                artistName: String,
                artwork: String,
                genre: String,
                date: String,
                collectionId: String) {
        this.albumTitle = albumTitle
        this.artistName = artistName
        this.artwork = artwork
        this.genre = genre
        this.date = date
        this.collectionId = collectionId
    }

    constructor()

}