package com.mdubovikov.data.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mdubovikov.common.ResponseOption
import com.mdubovikov.data.network.api.ApiService
import com.mdubovikov.data.network.dto.TrackDto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TracksPageSource @Inject constructor(
    private val apiService: ApiService,
    private val responseOption: ResponseOption,
    private val query: String? = null
) : PagingSource<Int, TrackDto>() {

    override fun getRefreshKey(state: PagingState<Int, TrackDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TrackDto> {
        val index = params.key ?: 0

        return when (responseOption) {
            ResponseOption.CHART_TRACKS -> {
                try {
                    val response = apiService.getTracks()
                    val tracks = response.tracks.data

                    LoadResult.Page(
                        data = tracks,
                        prevKey = null,
                        nextKey = null
                    )
                } catch (e: IOException) {
                    LoadResult.Error(e)
                } catch (e: HttpException) {
                    LoadResult.Error(e)
                }
            }

            ResponseOption.SEARCH_TRACKS -> {
                try {
                    val response = apiService.searchTracks(query.toString(), index)
                    val tracks = response.tracks

                    val nextKey = if (tracks.isEmpty()) null else index + params.loadSize
                    val prevKey = if (index == 0) null else index - params.loadSize

                    LoadResult.Page(
                        data = tracks,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                } catch (e: IOException) {
                    LoadResult.Error(e)
                } catch (e: HttpException) {
                    LoadResult.Error(e)
                }
            }
        }
    }
}