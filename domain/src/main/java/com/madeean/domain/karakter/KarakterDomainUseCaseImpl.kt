package com.madeean.domain.karakter

import android.app.Application
import androidx.paging.PagingData
import com.madeean.domain.karakter.model.local.KarakterItemFavoriteModelRoom
import com.madeean.domain.karakter.model.local.KarakterItemModelRoom
import com.madeean.domain.karakter.model.network.KarakterModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KarakterDomainUseCaseImpl  @Inject constructor(private val repository: KarakterDomainRepository) :
    KarakterDomainUseCase {

    override suspend fun getAllKarakter(
        scope: CoroutineScope,
        application: Application,
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    ): Flow<PagingData<KarakterModelItemModel>> {
        return repository.getAllKarakter(scope,application,name,status,species,type,gender)
    }

    override suspend fun getKarakterById(
        scope: CoroutineScope,
        id: String
    ): Flow<PagingData<KarakterModelItemModel>> {
        return repository.getKarakterById(scope,id)
    }

    override suspend fun getKarakterRoom(application: Application): List<KarakterItemModelRoom> {
        return repository.getKarakterRoom(application)
    }

    override suspend fun insertFavoriteKarakter(application: Application, id: Int) {
        return repository.insertFavoriteKarakter(application,id)
    }

    override suspend fun deleteFavoriteKarakter(application: Application, id: Int) {
        return repository.deleteFavoriteKarakter(application,id)

    }

    override suspend fun getFavoriteKarakter(application: Application): List<KarakterItemFavoriteModelRoom> {
        return repository.getFavoriteKarakter(application)
    }
}