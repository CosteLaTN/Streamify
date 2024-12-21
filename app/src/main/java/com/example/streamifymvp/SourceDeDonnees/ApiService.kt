package com.example.streamifymvp.SourceDeDonnees

import com.example.streamifymvp.Domaine.entitees.*
import retrofit2.http.*

interface ApiService {


    @GET("artistes")
    suspend fun getAllArtistes(): List<Artiste>

    @GET("artistes/{id}")
    suspend fun getArtisteById(@Path("id") id: Int): Artiste

    @GET("artistes/recherche")
    suspend fun searchArtistes(@Query("pseudoArtiste") pseudoArtiste: String): List<Artiste>


    @GET("chansons")
    suspend fun getAllChansons(): List<Chanson>

    @GET("chansons/{id}")
    suspend fun getChansonById(@Path("id") id: Int): Chanson

    @GET("chansons/recherche")
    suspend fun searchChansons(@Query("nom") nom: String): List<Chanson>


    @GET("listesLecture")
    suspend fun getAllPlaylists(): List<ListeDeLecture>

    @GET("listesLecture/{id}")
    suspend fun getPlaylistById(@Path("id") id: Int): ListeDeLecture

    @POST("listesLecture")
    suspend fun createPlaylist(@Body playlist: ListeDeLecture): ListeDeLecture

    @PUT("listesLecture/{id}/chansons/{chansonId}")
    suspend fun addSongToPlaylist(
        @Path("id") playlistId: Int,
        @Path("chansonId") chansonId: Int
    )
    @GET("images/{filename}")
    suspend fun getImage(@Path("filename") filename: String): String

    @GET("audio/{filename}")
    suspend fun getAudio(@Path("filename") filename: String): String

    @DELETE("listesLecture/{id}/chansons/{chansonId}")
    suspend fun removeSongFromPlaylist(
        @Path("id") playlistId: Int,
        @Path("chansonId") chansonId: Int
    )
}
