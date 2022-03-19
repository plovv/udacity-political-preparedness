package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.w3c.dom.Element
import java.util.*

class ElectionsFragment: Fragment() {

    private val electionsViewModel: ElectionsViewModel by viewModel()
    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = electionsViewModel

        binding.electionList.adapter = ElectionListAdapter(ElectionListener {
            electionsViewModel.onElectionSelect(it)
        })
        binding.savedElectionList.adapter = ElectionListAdapter(ElectionListener {
            electionsViewModel.onElectionSelect(it)
        })

        electionsViewModel.navigateToVoterInfo.observe(this.viewLifecycleOwner, Observer { election ->
            if (election != null) {
                findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division))
                electionsViewModel.completeNavigation()
            }
        })

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}