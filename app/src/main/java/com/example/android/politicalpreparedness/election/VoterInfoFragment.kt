package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
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
        binding = FragmentVoterInfoBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = voteInfoViewModel

        voteInfoViewModel.openUrl.observe(this.viewLifecycleOwner) { uri ->
            if (uri != null) {
                openUrl(uri)
                voteInfoViewModel.clearOpenUrl()
            }
        }

        voteInfoViewModel.showError.observe(this.viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                voteInfoViewModel.errorShown()
            }
        }

        return binding.root
    }
    
    private fun openUrl(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        requireContext().startActivity(intent)
    }

}