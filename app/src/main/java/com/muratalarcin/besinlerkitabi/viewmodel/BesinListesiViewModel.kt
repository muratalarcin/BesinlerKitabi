package com.muratalarcin.besinlerkitabi.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.widget.Toast
import com.muratalarcin.besinlerkitabi.model.Besin
import com.muratalarcin.besinlerkitabi.service.BesinAPIService
import com.muratalarcin.besinlerkitabi.service.BesinDatabase
import com.muratalarcin.besinlerkitabi.util.OzelSharedPreferences
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class BesinListesiViewModel(application: Application) : BaseViewModel(application) {
    val besinler = MutableLiveData<List<Besin>>()
    val besinHataMesaji = MutableLiveData<Boolean>()
    val besinYukleniyor = MutableLiveData<Boolean>()
    private val guncellemeZamani = 10 * 60 * 1000 * 1000 * 1000L //10 dakikayı nanosaniyede gösterdik

    private val besiApiService = BesinAPIService()
    private val disposable = CompositeDisposable() // hafıza yönetimi için rxjavanın bize sağladığı kullan at istek gibi bişi yapıyo
    private val OzelSharedPreferences = OzelSharedPreferences(getApplication())


    fun refreshData() {

        val kaydedilmeZamani = OzelSharedPreferences.ZamaniAl()

        if (kaydedilmeZamani != null && kaydedilmeZamani != 0L && System.nanoTime() - kaydedilmeZamani < guncellemeZamani){

            verileriSQLitedanAL()

        }else {
            verileriInternettenAL()
        }
    }

    fun refleshFromInternet(){

        verileriInternettenAL()

    }


    private fun verileriSQLitedanAL(){
        besinYukleniyor.value = true

        launch {

            val besinListesi = BesinDatabase(getApplication()).besinDao().getAllBesin()
            besinlerGoster(besinListesi)
            Toast.makeText(getApplication(), "Besinleri ROOM dan aldık", Toast.LENGTH_LONG).show()

        }

    }

    private fun verileriInternettenAL(){
        besinYukleniyor.value = true

        disposable.add(
            besiApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                    object : DisposableSingleObserver<List<Besin>>() {
                        override fun onSuccess(t: List<Besin>) {
                            sqliteSakla(t)
                            Toast.makeText(getApplication(), "Besinleri Internet'ten aldık", Toast.LENGTH_LONG).show()

                        }

                        override fun onError(e: Throwable) {
                            besinHataMesaji.value = true
                            besinYukleniyor.value = false
                            e.printStackTrace() // eğer hata alırsak hatayı logcat de görmemizi sağlar
                        }

                    }

                    )
        )

    }
    private fun besinlerGoster (besinlerListesi : List<Besin>){

        besinler.value = besinlerListesi
        besinYukleniyor.value = false
        besinHataMesaji.value = false

    }

    private fun sqliteSakla(besinlerListesi : List<Besin>) {

        launch {

            val dao = BesinDatabase(getApplication()).besinDao()
            dao.deleteAllBesin()
            val uuidListesi = dao.insertAll(*besinlerListesi.toTypedArray())
            var i = 0
            while (i < besinlerListesi.size){
                besinlerListesi[i].uuid = uuidListesi[i].toInt()
                i += 1
            }
            besinlerGoster(besinlerListesi)
        }

        OzelSharedPreferences.zamaniKaydet(System.nanoTime())

    }

}