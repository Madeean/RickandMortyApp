package com.example.rickandmortyapp.domain.model.karakter

import android.os.Parcelable
import com.example.rickandmortyapp.data.repository.network.karakter.model.KarakterLocation
import com.example.rickandmortyapp.data.repository.network.karakter.model.KarakterOrigin
import kotlinx.parcelize.Parcelize

@Parcelize
data class KarakterModelItemModel(
    val id:Int?,
    val name:String?,
    val status:String?,
    val species:String?,
    val type:String?,
    val gender:String?,
    val origin: KarakterModelOriginItemModel?,
    val location: KarakterModelLocationItemModel?,
    val image:String?,
    val episode:List<String>?,
    val created:String?
): Parcelable
