package app.pavel.itunesapitest

/**
 * Singleton object with String constants
 */

object Constants {
    const val spaceChar = ' '
    const val plusChar = '+'
    const val minusChar = '-'
    const val dotChar = '.'

    // URL string for Album search by title
    const val albumQueryURLPart1 = "https://itunes.apple.com/search?term="
    const val albumQueryURLPart2 = "&entity=album&attribute=albumTerm"

    // URL string for Album's information search by collectionId
    const val albumSongQueryURLPart1 = "https://itunes.apple.com/lookup?id="
    const val albumSongQueryURLPart2 = "&entity=song"

    // keys for JSON strings (Album search)
    const val results = "results"
    const val resultCount = "resultCount"
    const val resultCountText = "Result count: "
    const val collectionName = "collectionName"
    const val artistName = "artistName"
    const val releaseDate = "releaseDate"
    const val primaryGenreName = "primaryGenreName"
    const val artworkUrl100 = "artworkUrl100"
    const val collectionId = "collectionId"
    // keys for JSON strings (Album's songs search)
    const val price = "Price:"
    const val released = "Released:"
    const val collectionPrice = "collectionPrice"
    const val copyright = "copyright"
    const val country = "country"
    const val currency = "currency"
    const val trackName = "trackName"
    const val trackNumber = "trackNumber"
    const val trackTimeMillis = "trackTimeMillis"
    const val trackExplicitness = "trackExplicitness"
    const val explicit = "explicit"
    const val letterE = "E"
    const val zero = "0"

    // text for Toast
    const val failToLoadText = "Fail to load. Please, check your connection"
}