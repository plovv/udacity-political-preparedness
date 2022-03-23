package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment: Fragment() {

    private val electionsViewModel: ElectionsViewModel by viewModel()
    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = electionsViewModel

        binding.electionList.adapter = ElectionListAdapter(ElectionListener {
            electionsViewModel.onElectionSelect(it)
        })
        binding.savedElectionList.adapter = ElectionListAdapter(ElectionListener {
            electionsViewModel.onElectionSelect(it)
        })

        electionsViewModel.navigateToVoterInfo.observe(this.viewLifecycleOwner) { election ->
            if (election != null) {
                findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division))
                electionsViewModel.completeNavigation()
            }
        }

        electionsViewModel.showError.observe(this.viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                electionsViewModel.errorShown()
            }
        }

        return binding.root
    }

}