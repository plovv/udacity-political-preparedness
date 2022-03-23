package com.example.android.politicalpreparedness.election.adapter

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.Election

@BindingAdapter("elections")
fun bindRecyclerViewElections(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("stateDataValue")
fun bindTextRelatedData(textView: TextView, dataValue: String?) {
    if (dataValue != null && dataValue.isNotEmpty()) {
        textView.visibility = View.VISIBLE
        textView.text = dataValue
    } else {
        textView.visibility = View.GONE
    }
}

@BindingAdapter("setSavedTitle")
fun bindFollowButtonTitle(button: Button, isSaved: Boolean = false) {
    button.text = if (isSaved) button.context.getText(R.string.btn_election_unfollow_title) else button.context.getText(R.string.btn_election_follow_title)
}