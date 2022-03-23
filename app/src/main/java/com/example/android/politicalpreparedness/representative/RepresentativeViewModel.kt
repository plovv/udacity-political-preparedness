package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.Repository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repo: Repository): ViewModel() {

    val line1 = MutableLiveData<String>()
    val line2 = MutableLiveData<String?>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String?>()
    val zip = MutableLiveData<String?>()

    private var _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _showError = MutableLiveData<String?>(null)
    val showError: LiveData<String?>
        get() = _showError
    fun errorShown() { _showError.value = null }

    fun setAddressFromMyLocation(address: Address) {
        line1.value = address.line1
        line2.value = address.line2
        city.value = address.city
        state.value = address.state
        zip.value = address.zip
    }

    fun retrieveRepresentatives() {
        if (line1.value != null && city.value != null && state.value != null && zip.value != null) {
            val address = Address(
                line1.value!!,
                line2.value,
                city.value!!,
                state.value!!,
                zip.value!!
            )

            viewModelScope.launch {
                try {
                    val (offices, officials) = repo.retrieveRepresentatives(address.toFormattedString())
                    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
                } catch (e: Exception) {
                    _showError.value = "Failed to retrieve the representatives. Please check your network conneciton."
                }
            }
        }
    }

}
