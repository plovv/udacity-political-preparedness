package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class VoterInfoFragment : Fragment() {

    private val voteInfoViewModel: VoterInfoViewModel by viewModel {
        parametersOf(
            VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId,
            VoterInfoFragmentArgs.fromBundle(requireArguments()).argDivision
        )
    }
    private lateinit var binding: FragmentVoterInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

        binding.lifecycleOwner = this
        binding.viewModel = voteInfoViewModel

        return binding.root
    }

    //TODO: Create method to load URL intents

}