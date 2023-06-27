package com.muratalarcin.besinlerkitabi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.muratalarcin.besinlerkitabi.R
import com.muratalarcin.besinlerkitabi.databinding.FragmentBesinDetayiBinding
import com.muratalarcin.besinlerkitabi.viewmodel.BesinDetayiViewModel


class BesinDetayiFragment : Fragment() {
    private lateinit var viewModel: BesinDetayiViewModel
    private lateinit var dataBinding: FragmentBesinDetayiBinding
    private var besinID = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_besin_detayi,container, false)
        return dataBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated (view, savedInstanceState)

        arguments?. let {
            besinID = BesinDetayiFragmentArgs.fromBundle(it).besinId
        }
       // viewModel = ViewModelProvider(this).get(BesinDetayiViewModel::class.java)
        viewModel.roomVerisiniAl(besinID)


        observeLiveData()

    }

    fun observeLiveData(){

        viewModel.besinLiveData.observe(viewLifecycleOwner) { besin ->
            besin?.let {
                dataBinding.secilenBesin = it
            }

        }

    }

}