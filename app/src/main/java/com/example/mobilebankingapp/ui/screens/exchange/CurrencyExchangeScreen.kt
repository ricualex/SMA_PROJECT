package com.example.mobilebankingapp.ui.screens.exchange

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.mobilebankingapp.components.RoundGreyButton
import com.example.mobilebankingapp.model.UserData
import com.example.mobilebankingapp.ui.screens.home.ApiViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyExchangeScreen (exchangeDataViewModel: ApiViewModel, dataModel: UserData) {
    var selectedFrom by remember { mutableStateOf("") }
    var selectedTo by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(0.0) }
    var resultAmount by remember { mutableStateOf(0.0) }
    var items = mutableListOf<String>()
    val ctx = LocalContext.current;
    exchangeDataViewModel.exchangeRateDataState.value.rates.keys.forEach { items.add(it) }
    Column {
        SearchableDropdown(
            items = items.distinct(),
            onItemSelected = { selectedFrom = it },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        SearchableDropdown(
            items = items.filter { it != selectedFrom }.distinct(),
            onItemSelected = { selectedTo = it },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        if (selectedFrom.isNotEmpty() && selectedTo.isNotEmpty()) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Exchange money from: $selectedFrom",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp),
                    color = Color.White
                )
                Text(text = "1 $selectedFrom equals ${String.format("%.4f", exchangeDataViewModel.getConversionAmount(selectedFrom, selectedTo))} $selectedTo",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp),
                    color = Color.White
                )
                OutlinedTextField(
                    value = formatTextFieldValue(quantity),
                    onValueChange = {
                        try {
                            if (it != "") {
                                quantity = it.toDouble()
                            } else {
                                quantity = 0.0
                            }
                        } catch (e: NumberFormatException) {
                            quantity = 0.0
                        }
                    },
                    label = { Text(selectedFrom) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusTarget()
                        .clickable {
                        }
                )
                OutlinedTextField(
                    value = exchangeDataViewModel.getConversionAmount(quantity, selectedFrom, selectedTo).toString(),
                    onValueChange = {resultAmount = it.toDouble()},
                    readOnly = true,
                    label = { Text(selectedTo) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusTarget()
                )
                Spacer(modifier = Modifier.padding(top = 16.dp))
                RoundGreyButton(value = "Confirm", onButtonClick = {
                    val result = exchangeDataViewModel.submitExchange(dataModel, quantity, selectedFrom, selectedTo)
                    if (result == 1) {
                        Toast.makeText(ctx, "Transaction successful!", LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(ctx, "Transaction failed, insufficient funds!", LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchableDropdown(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var isDropdownVisible by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier) {
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                isDropdownVisible = true
            },
            label = { Text("Search") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        isDropdownVisible = !isDropdownVisible
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusTarget()
                .clickable {
                    isDropdownVisible = true
                    focusManager.clearFocus()
                }
        )

        if (isDropdownVisible) {
            DropdownList(
                items = items.filter { it.contains(searchText, ignoreCase = true) },
                onItemSelected = {
                    selectedText = it
                    onItemSelected(it)
                    isDropdownVisible = false
                    searchText = it
                    keyboardController?.hide()
                }
            )
        }
    }
}

@Composable
private fun DropdownList(
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(items.size) {
            val item = items[it]
            DropdownItem(text = item, onItemClick = { onItemSelected(item) })
        }
    }
}

@Composable
private fun DropdownItem(text: String, onItemClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(16.dp)
    ) {
        Text(text = text, fontSize = 16.sp)
    }
}

private fun formatTextFieldValue(quantity: Double) : String {
    return if (quantity == 0.0) {
        ""
    }
    else quantity.toString()
}