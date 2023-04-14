package com.example.rickandmortyapp.data.repository.domain_repository

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyapp.data.repository.di.LocalModule
import com.example.rickandmortyapp.data.repository.local.karakter.KarakterFavoriteModelRoom
import com.example.rickandmortyapp.data.repository.local.karakter.KarakterModelRoom
import com.example.rickandmortyapp.data.repository.network.karakter.KarakterApiService
import com.example.rickandmortyapp.data.repository.network.karakter.KarakterListPagingSource
import com.example.rickandmortyapp.data.repository.network.karakter.MultipleKarakterPagingSource
import com.example.rickandmortyapp.domain.karakter.KarakterDomainRepository
import com.example.rickandmortyapp.domain.karakter.model.local.KarakterItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.karakter.model.local.KarakterItemModelRoom
import com.example.rickandmortyapp.domain.karakter.model.network.KarakterModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KarakterDomainRepositoryImpl  @Inject constructor(
    private val karakterApiService: KarakterApiService,
) : KarakterDomainRepository {
    override suspend fun getAllKarakter(
        scope: CoroutineScope,
        application: Application,
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    ): Flow<PagingData<KarakterModelItemModel>> {

        return Pager(config = PagingConfig(20)) {
            KarakterListPagingSource(
                apiService = karakterApiService,
                application = application,
                name = name,
                status = status,
                species = species,
                type = type,
                gender = gender
            )
        }.flow.cachedIn(scope)
    }

    override suspend fun getKarakterById(
        scope: CoroutineScope, id: String
    ): Flow<PagingData<KarakterModelItemModel>> {
        return Pager(config = PagingConfig(1)) {
            MultipleKarakterPagingSource( apiService = karakterApiService, id = id
            )
        }.flow.cachedIn(scope)
    }

    override suspend fun getKarakterRoom(application: Application): List<KarakterItemModelRoom> {
        val karakterDao = LocalModule.getDatabase(application).karakterDao()
        val data = karakterDao.getAllKarakterRoom()
        return KarakterModelRoom.transfromsFroomRoomToDomain(data)
    }


    override suspend fun insertFavoriteKarakter(application: Application, id: Int) {
        val karakterDao = LocalModule.getDatabase(application).karakterDao()
        karakterDao.insertKarakterFavoriteRoom(KarakterFavoriteModelRoom(id))

    }

    override suspend fun deleteFavoriteKarakter(application: Application, id: Int) {
        val karakterDao = LocalModule.getDatabase(application).karakterDao()
        karakterDao.deleteKarakterFavoriteRoom(KarakterFavoriteModelRoom(id))

    }

    override suspend fun getFavoriteKarakter(application: Application): List<KarakterItemFavoriteModelRoom> {
        val karakterDao = LocalModule.getDatabase(application).karakterDao()
        val data = karakterDao.getAllKarakterFavoriteRoom()
        return KarakterFavoriteModelRoom.transforms(data)

    }
}