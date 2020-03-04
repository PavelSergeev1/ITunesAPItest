package app.pavel.itunesapitest

import java.util.*

public class Album {
    lateinit var albumTitle: String
    lateinit var artistName: String
    lateinit var artwork: String
    lateinit var genre: String
    lateinit var date: String

    constructor(albumTitle: String,
                artistName: String,
                artwork: String,
                genre: String,
                date: String) {
        this.albumTitle = albumTitle
        this.artistName = artistName
        this.artwork = artwork
        this.genre = genre
        this.date = date
    }

    constructor()

    /*

    fun getAlbumTitle() : String {
        return this.albumTitle
    }

    fun setAlbumTitle(title: String) {
        this.albumTitle = title
    }

    fun getArtistName() : String {
        return this.artistName
    }

    fun setArtistName(name: String) {
        this.artistName = name
    }

    fun getArtwork() : String {
        return this.artwork
    }

    fun getGenre() : String {
        return this.genre
    }

    fun getDate() : String {
        return this.date
    }

     */
}