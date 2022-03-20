package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.Repository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repo: Repository): ViewModel() {

    //TODO: Establish live data for representatives and address

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields
    val line1 = MutableLiveData<String>()
    val line2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<Address>()

    private var _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    fun setAddressFromMyLocation(myAddress: Address) {
        address.value = Address(
            myAddress.line1,
            myAddress.line2,
            myAddress.city,
            myAddress.state,
            myAddress.zip
        )
    }

    fun getRepresentatives() {
        if (address.value != null) {
            viewModelScope.launch {
                val (offices, officials) = repo.retrieveRepresentatives(address.value!!.toFormattedString())
                _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
            }
        }
    }

}
