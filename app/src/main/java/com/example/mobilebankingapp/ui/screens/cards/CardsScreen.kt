package com.example.mobilebankingapp.ui.screens.cards

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilebankingapp.R
import com.example.mobilebankingapp.ViewModelProvider
import com.example.mobilebankingapp.model.CreditCard
import com.example.mobilebankingapp.utils.getSecretKey

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardsScreen(
    activity: Activity,
    cards: List<Pair<String, CreditCard>>,
    onCardAdded: (CreditCard, String) -> Unit,
    onCardDeleted: (String) -> Unit,
    onCardSetDefault: (String, String?) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val pagerState = rememberPagerState {
            cards.size
        }
        val key = stringResource(id = R.string.key_alias)

        if (cards.isNotEmpty()) {
            HorizontalPager(state = pagerState, key = { it }) {
                val cardViewModel: CreditCardViewModel =
                    viewModel(key = cards[it].first, factory = ViewModelProvider.Factory)
                cardViewModel.creditCard.value = cards[it].second.decrypt(key)

                CreditCardForm(
                    viewModel = cardViewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        var showDialog by remember { mutableStateOf(false) }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(onClick = { showDialog = true }) {
                    Text(text = "Add new card")
                }
                Button(
                    onClick = {
                        val crtDefaultId = cards.find { it.second.default }?.first
                        onCardSetDefault(cards[pagerState.currentPage].first, crtDefaultId)
                    },
                    enabled = pagerState.currentPage < cards.size && !cards[pagerState.currentPage].second.default
                ) {
                    Text(text = "Set as default")
                }
                Button(
                    onClick = { onCardDeleted(cards[pagerState.currentPage].first) },
                    enabled = pagerState.currentPage < cards.size
                ) {
                    Text(text = "Delete")
                }
            }
        }

        if (showDialog) {
            val newCardViewModel: CreditCardViewModel =
                viewModel(factory = ViewModelProvider.Factory)
            if (cards.isEmpty()) {
                newCardViewModel.updateDefault(true)
            }
            Dialog(onDismissRequest = {
                showDialog = false
                newCardViewModel.resetState()
            }) {
                Card(
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CreditCardForm(
                            viewModel = newCardViewModel,
                            readOnly = false,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                    newCardViewModel.resetState()
                                },
                                modifier = Modifier.padding(4.dp),
                            ) {
                                Text("Cancel")
                            }

                            val ctx = LocalContext.current;
                            if (ctx.packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
                                var showScanError by remember { mutableStateOf(false) }
                                TextButton(onClick = {
                                    CardReaderCallback(
                                        activity,
                                        newCardViewModel::updateCreditCard
                                    ) { showScanError = true }
                                }) {
                                    Text("Scan")
                                }
                                if (showScanError) {
                                    Toast.makeText(
                                        ctx,
                                        "Card scan failed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            TextButton(
                                onClick = {
                                    onCardAdded(newCardViewModel.creditCard.value, key)
                                    showDialog = false
                                    newCardViewModel.resetState()
                                },
                                enabled = newCardViewModel.isCreditCardValid(),
                                modifier = Modifier.padding(4.dp),
                            ) {
                                Text("Confirm")
                            }
                        }
                    }
                }
            }
        }
    }
}
