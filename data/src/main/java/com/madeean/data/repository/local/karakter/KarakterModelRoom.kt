package com.madeean.data.repository.local.karakter

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.madeean.data.repository.network.karakter.model.KarakterDetail
import com.madeean.domain.karakter.model.local.KarakterItemModelRoom
import kotlinx.parcelize.Parcelize

@Entity(tableName = "karakter")
@Parcelize
data class KarakterModelRoom(
    @PrimaryKey @ColumnInfo(name = "id_karakter") val idKarakter:Int?,
    @ColumnInfo(name = "name") val name:String?,
    @ColumnInfo(name = "status") val status:String?,
    @ColumnInfo(name = "species") val species:String?,
    @ColumnInfo(name = "type") val type:String?,
    @ColumnInfo(name = "gender") val gender:String?,
    @ColumnInfo(name = "image") val image:String?,
    @ColumnInfo(name = "created") val created:String?
):Parcelable{
    companion object{
        fun transforms(models:List<KarakterDetail>):List<KarakterModelRoom>{
            return models.map{
                transform(
                    KarakterDetail(
                        id = it.id,
                        name = it.name,
                        status = it.status,
                        species = it.species,
                        type = it.type,
                        gender = it.gender,
                        image = it.image,
                        created = it.created,
                        episode = null,
                        location = null,
                        origin = null
                    )
                )
            }
        }

        private fun transform(model: KarakterDetail):KarakterModelRoom{
            return KarakterModelRoom(
                idKarakter = model.id,
                name = model.name,
                status = model.status,
                species = model.species,
                type = model.type,
                gender = model.gender,
                image = model.image,
                created = model.created,
            )
        }

        fun transfromsFroomRoomToDomain(models:List<KarakterModelRoom>):List<KarakterItemModelRoom>{
            return models.map {
                transformFromRoomToDomain(
                    KarakterModelRoom(
                        idKarakter = it.idKarakter,
                        name = it.name,
                        status = it.status,
                        species = it.species,
                        type = it.type,
                        gender = it.gender,
                        image = it.image,
                        created = it.created,
                    )
                )
            }
        }

        private fun transformFromRoomToDomain(model: KarakterModelRoom): KarakterItemModelRoom {
            return KarakterItemModelRoom(
                id = model.idKarakter,
                name = model.name,
                status = model.status,
                species = model.species,
                type = model.type,
                gender = model.gender,
                image = model.image,
                created = model.created
            )
        }

    }
}
