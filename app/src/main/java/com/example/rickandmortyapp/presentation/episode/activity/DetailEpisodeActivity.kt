package com.example.rickandmortyapp.presentation.episode.activity

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ActivityDetailEpisodeBinding
import com.example.rickandmortyapp.domain.episode.model.local.EpisodeItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.episode.model.network.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.tmdb.model.TmdbTvDomainModel
import com.example.rickandmortyapp.presentation.PresentationUtils.CODE_RESULT
import com.example.rickandmortyapp.presentation.PresentationUtils.INTENT_DATA
import com.example.rickandmortyapp.presentation.PresentationUtils.getIdFromUrl
import com.example.rickandmortyapp.presentation.PresentationUtils.loadingAlertDialog
import com.example.rickandmortyapp.presentation.PresentationUtils.setLoading
import com.example.rickandmortyapp.presentation.PresentationUtils.showError
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.factory.PresentationFactory
import com.example.rickandmortyapp.presentation.karakter.activity.DetailKarakterActivity
import com.example.rickandmortyapp.presentation.karakter.adapter.KarakterPagingAdapter
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.tmdb.TmdbViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailEpisodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEpisodeBinding
    private var data: EpisodeModelItemModel? = null
    private lateinit var adapter: KarakterPagingAdapter
    private lateinit var dialog: Dialog

    @Inject
    lateinit var presentationFactory: PresentationFactory

    private val karakterViewModel: KarakterViewModel by viewModels {
        presentationFactory
    }

    private val episodeViewModel: EpisodeViewModel by viewModels {
        presentationFactory
    }

    private val tmdbViewModel: TmdbViewModel by viewModels {
        presentationFactory
    }

    private var idKarakter = ""
    private var dataFavorite: List<EpisodeItemFavoriteModelRoom> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEpisodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setProgressBar()
        setObserver()
        setToolbar()
        setAdapter()
        getDataFromIntent()
        setFavorite()
    }

    private fun setObserver() {
        setLoading(true, dialog)
        tmdbViewModel.tmdbDetailTv.observe(this) {
            if (it.overview.isBlank()) return@observe
            setOverview(it)
        }
        tmdbViewModel.tmdbTrailetTv.observe(this) {
            if (it.isEmpty())return@observe
            binding.cvYoutubePlayerView.visibility = View.VISIBLE
            setYoutubePlayer(it[0].key)
        }

    }

    private fun setYoutubePlayer(key: String) {
        binding.youtubePlayerView.let { lifecycle.addObserver(it) }
        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(key, 0f)
            }
        })
    }

    private fun setOverview(it: TmdbTvDomainModel) {
        if (it.error.isNotBlank()) {
            binding.apply {
                tvOverviewTag.visibility = View.GONE
                tvOverview.visibility = View.GONE
                clDescriptionDetailEpisode.visibility = View.INVISIBLE
            }
            Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
        }
        binding.tvOverview.text = it.overview
    }

    private fun setFavorite() {
        getFavorite()
        insertFavorite()
    }

    private fun getFavorite() {
        lifecycleScope.launch {
            dataFavorite = episodeViewModel.getEpisodeFavoriteRoom(application)

            val isFavorite = dataFavorite.any {
                it.idEpisode == data?.id
            }

            binding.cbFavoritDetailEpisode.apply {
                isChecked = if (isFavorite) {
                    setBackgroundResource(R.drawable.favorite_full)
                    false
                } else {
                    setBackgroundResource(R.drawable.favorite_outline)
                    true
                }
            }


        }
    }

    private fun insertFavorite() {
        binding.cbFavoritDetailEpisode.apply {
            setOnClickListener {
                if (isChecked) {
                    lifecycleScope.launch {
                        episodeViewModel.deleteEpisodeFavoriteRoom(application, data?.id ?: -1)
                    }
                    Toast.makeText(
                        this@DetailEpisodeActivity,
                        getString(R.string.berhasil_menghapus_favorite, data?.name),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    lifecycleScope.launch {
                        episodeViewModel.insertEpisodeFavoriteRoom(application, data?.id ?: -1)
                    }
                    Toast.makeText(
                        this@DetailEpisodeActivity,
                        getString(R.string.berhasil_menambah_favorite, data?.name),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                getFavorite()
            }
        }

    }

    private fun setProgressBar() {
        dialog = loadingAlertDialog(this)
    }


    private fun setAdapter() {
        adapter = KarakterPagingAdapter().apply {
            setOnItemClickListener { _, data ->
                val intent = Intent(this@DetailEpisodeActivity, DetailKarakterActivity::class.java)
                intent.putExtra(INTENT_DATA, data)
                startActivity(intent)
            }
        }
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                setLoading(true, dialog)
            } else {
                setLoading(false, dialog)
            }
            if (loadState.refresh is LoadState.Error) {
                setLoading(false, dialog)
                showError(getString(R.string.karakter_tidak_ditemukan), this)

            }
        }
        binding.apply {
            rvDetailEpisode.layoutManager = GridLayoutManager(this@DetailEpisodeActivity, 2)
            rvDetailEpisode.adapter = adapter
        }


    }

    private fun getDataFromIntent() {
        data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(INTENT_DATA, EpisodeModelItemModel::class.java)
        } else {
            intent.getParcelableExtra(INTENT_DATA)
        }
        if (data != null) {
            setData(data)
            getDataFromApi()
        } else {
            showError(getString(R.string.episode_tidak_ditemukan), this)
        }
    }

    private fun getDataFromApi() {
        lifecycleScope.launch {
            karakterViewModel.getKarakterById(idKarakter).collectLatest {
                adapter.submitData(it)
            }
        }
    }


    private fun setData(data: EpisodeModelItemModel?) {
        binding.apply {
            tvEpisode.text = data?.episode
            tvName.text = data?.name
            tvAirDate.text = data?.airDate
        }
        val episode = getEpisode(data?.episode ?: "")
        val season = getSeason(data?.episode ?: "")

        if (data?.characterList?.isNotEmpty() == true) {
            idKarakter = ""
            data.characterList.forEach {
                idKarakter += "${getIdFromUrl(it)},"
            }
        }

        getDataOverview(episode, season)
        getDataTrailer(episode, season)

    }

    private fun getDataTrailer(episode: Int, season: Int) {
        tmdbViewModel.getTrailerTv(episodeNumber = episode, seasonNumber = season)
    }

    private fun getDataOverview(episode: Int, season: Int) {
        tmdbViewModel.getDetailTv(episodeNumber = episode, seasonNumber = season)
    }

    private fun getSeason(data: String): Int {
        val pattern = "(S)(\\d{2})(E)\\d+".toRegex()
        val matchResult = pattern.find(data)
        val result = matchResult?.groups?.get(2)?.value
        return result?.toInt() ?: 0
    }

    private fun getEpisode(data: String): Int {
        val pattern = "S\\d+(E)(\\d{2})".toRegex()
        val matchResult = pattern.find(data)
        val result = matchResult?.groups?.get(2)?.value
        return result?.toInt() ?: 0
    }


    private fun setToolbar() {
        binding.detailEpisodeToolbar.apply {
            tvToolbar.text = getString(R.string.detail_episode)
            ivBackToolbar.setOnClickListener {
                val intent = Intent()
                setResult(CODE_RESULT, intent)
                finish()
            }
        }

    }
}