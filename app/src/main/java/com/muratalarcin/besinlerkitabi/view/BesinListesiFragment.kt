package com.muratalarcin.besinlerkitabi.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.muratalarcin.besinlerkitabi.adapter.BesinRecyclerAdapter
import com.muratalarcin.besinlerkitabi.databinding.FragmentBesinListesiBinding
import com.muratalarcin.besinlerkitabi.viewmodel.BesinListesiViewModel


@Suppress("UNREACHABLE_CODE")
class BesinListesiFragment : Fragment() {
    private lateinit var viewModel : BesinListesiViewModel
    private lateinit var binding: FragmentBesinListesiBinding
    private val recyclerBesinAdapter = BesinRecyclerAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBesinListesiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ViewModelProvider(this@QuizActivity).get(QuizViewModel::class.java)
      //  viewModel = ViewModelProviders.of(this).get(BesinListesiViewModel::class.java)
        viewModel.refreshData()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = recyclerBesinAdapter

        binding.swipeRefleshLayout.setOnRefreshListener {
            binding.besinListesiYukleniyor.visibility = View.VISIBLE
            binding.besinListesiHataMesaji.visibility = View. GONE
            binding.recyclerView.visibility = View. GONE
            viewModel.refleshFromInternet()
            binding.swipeRefleshLayout.isRefreshing = false
        }
        observeLiveData()

    }

    fun observeLiveData(){

        viewModel.besinler.observe(viewLifecycleOwner) { besinler ->
            besinler?.let {

                binding.recyclerView.visibility = View.VISIBLE
                recyclerBesinAdapter.besinListesiniGuncelle(besinler)

            }

        }

        viewModel.besinHataMesaji.observe(viewLifecycleOwner) { besinHataMesaji ->
            besinHataMesaji?.let {
                if (it){
                    binding.besinListesiHataMesaji.visibility = View.VISIBLE
                } else {
                    binding.besinListesiHataMesaji.visibility = View.GONE
                }
            }

        }

        viewModel.besinYukleniyor.observe(viewLifecycleOwner) { besinYukleniyor ->
            besinYukleniyor?.let {
                if (it) {
                    binding.recyclerView.visibility = View.GONE
                    binding.besinListesiHataMesaji.visibility = View.GONE
                    binding.besinListesiYukleniyor.visibility = View.VISIBLE
                } else {
                    binding.besinListesiYukleniyor.visibility = View.GONE
                }
            }

        }

    }


}

private fun <T> MutableLiveData<T>.observe(viewLifecycleOwner: LifecycleOwner, observer: Observer<T>) {

}


