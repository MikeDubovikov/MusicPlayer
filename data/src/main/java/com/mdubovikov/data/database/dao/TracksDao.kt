package com.mdubovikov.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mdubovikov.data.database.entity.TrackDb
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksDao {

    @Query("SELECT * FROM downloads")
    fun getDownloads(): Flow<List<TrackDb>>

    @Query("SELECT id FROM downloads")
    fun trackIdsFromDownloads(): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToDownloads(trackDb: TrackDb)

    @Query("DELETE FROM downloads WHERE id=:trackId")
    suspend fun removeFromDownloads(trackId: Long)

    @Query(
        """
    SELECT * FROM downloads 
    WHERE title LIKE '%' || :query || '%' 
    OR artist LIKE '%' || :query || '%'"""
    )
    fun searchTracksFromDownloads(query: String): Flow<List<TrackDb>>
}