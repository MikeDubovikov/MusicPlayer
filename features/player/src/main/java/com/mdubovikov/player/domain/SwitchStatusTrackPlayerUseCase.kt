package com.mdubovikov.player.domain

import com.mdubovikov.player.domain.repositories.PlayerRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SwitchStatusTrackPlayerUseCase @Inject constructor(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(trackId: Long): Boolean {
        return playerRepository.getTrackIdsInDownloads().map {
            return@map if (!it.contains(trackId)) {
                playerRepository.addTrackToDownloads(trackId)
                true
            } else {
                playerRepository.removeTrackFromDownloads(trackId)
                false
            }
        }.first()
    }
}