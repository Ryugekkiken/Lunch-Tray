package com.example.lunchtray.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.lunchtray.data.DataSource
import java.text.NumberFormat

class OrderViewModel : ViewModel()
{
    // Map of menu items
    val menuItems = DataSource.menuItems

    // Default values for item prices
    private var previousEntreePrice = 0.0
    private var previousSidePrice = 0.0
    private var previousAccompanimentPrice = 0.0

    // Default tax rate
    private val taxRate = 0.08

    // Entree for the order
    private val _entree = MutableLiveData<MenuItem?>()
    val entree: LiveData<MenuItem?> = _entree

    // Side for the order
    private val _side = MutableLiveData<MenuItem?>()
    val side: LiveData<MenuItem?> = _side

    // Accompaniment for the order.
    private val _accompaniment = MutableLiveData<MenuItem?>()
    val accompaniment: LiveData<MenuItem?> = _accompaniment

    // Subtotal for the order
    private val _subtotal = MutableLiveData(0.0)
    val subtotal: LiveData<String> = Transformations.map(_subtotal) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    // Total cost of the order
    private val _total = MutableLiveData(0.0)
    val total: LiveData<String> = Transformations.map(_total) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    // Tax for the order
    private val _tax = MutableLiveData(0.0)
    val tax: LiveData<String> = Transformations.map(_tax) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    /**
     * Set the entree for the order.
     */
    fun setEntree(entree: String)
    {
        if(_entree.value != null)
            previousEntreePrice = _entree.value!!.price

        if(_subtotal.value != null)
            _subtotal.value = _subtotal.value!! - previousEntreePrice

        //set the current entree value to the menu item corresponding to the passed in string
        _entree.value = menuItems.getValue(entree)
        //update the subtotal to reflect the price of the selected entree.
        updateSubtotal(_entree.value!!.price)
    }

    /**
     * Set the side for the order.
     */
    fun setSide(side: String) {
        // ODO: if _side.value is not null, set the previous side price to the current side price.
        if(_side.value != null)
            previousSidePrice = _side.value!!.price

        // ODO: if _subtotal.value is not null subtract the previous side price from the current
        //  subtotal value. This ensures that we only charge for the currently selected side.
        if(_subtotal.value != null)
            _subtotal.value = _subtotal.value!! - previousSidePrice

        // ODO: set the current side value to the menu item corresponding to the passed in string
        _side.value = menuItems.getValue(side)
        // ODO: update the subtotal to reflect the price of the selected side.
        updateSubtotal(_side.value!!.price)
    }

    /**
     * Set the accompaniment for the order.
     */
    fun setAccompaniment(accompaniment: String) {
        // ODO: if _accompaniment.value is not null, set the previous accompaniment price to the
        //  current accompaniment price.
        if(_accompaniment.value != null)
            previousAccompanimentPrice = _accompaniment.value!!.price

        // ODO: if _accompaniment.value is not null subtract the previous accompaniment price from
        //  the current subtotal value. This ensures that we only charge for the currently selected
        //  accompaniment.
        if(_accompaniment.value != null)
            _subtotal.value = _subtotal.value!! - previousAccompanimentPrice

        // ODO: set the current accompaniment value to the menu item corresponding to the passed in
        //  string
        _accompaniment.value = menuItems.getValue(accompaniment)
        // ODO: update the subtotal to reflect the price of the selected accompaniment.
        updateSubtotal(_accompaniment.value!!.price)
    }

    /**
     * Update subtotal value.
     */
    private fun updateSubtotal(itemPrice: Double) {
        //  ODO: if _subtotal.value is not null, update it to reflect the price of the recently
        //  added item.
        //  Otherwise, set _subtotal.value to equal the price of the item.
        if(_subtotal.value != null)
            _subtotal.value = _subtotal.value!! + itemPrice
        else
            _subtotal.value = itemPrice

        // calculate the tax and resulting total

    }

    /**
     * Calculate tax and update total.
     */
    fun calculateTaxAndTotal() {
        // ODO: set _tax.value based on the subtotal and the tax rate.
        val tax = _subtotal.value!!.plus(_subtotal.value!! * taxRate)

        _tax.value = _subtotal.value!!.plus(tax)
        // ODO: set the total based on the subtotal and _tax.value.
        _total.value = _subtotal.value!!.plus(_tax.value!!)
    }

    /**
     * Reset all values pertaining to the order.
     */
    fun resetOrder() {
        // ODO: Reset all values associated with an order
        previousEntreePrice = 0.0
        previousSidePrice = 0.0
        previousAccompanimentPrice = 0.0

        _entree.value = MenuItem("", "", 0.0, 0)
        _side.value = MenuItem("", "", 0.0, 0)
        _accompaniment.value = MenuItem("", "", 0.0, 0)

        _subtotal.value = 0.0
        _tax.value = 0.0
        _total.value = 0.0
    }
}
