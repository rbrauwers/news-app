package com.rbrauwers.newsapp.source

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rbrauwers.newsapp.model.Country
import com.rbrauwers.newsapp.model.NewsSource
import com.rbrauwers.newsapp.ui.NewsAppDefaultProgressIndicator
import com.rbrauwers.newsapp.ui.Screen

internal const val sourceIdArg = "id"

val sourceScreen = Screen(
    route = "source/{$sourceIdArg}",
    title = R.string.source_details,
    icon = Icons.Filled.Person,
    isHome = false
)

@Composable
internal fun SourceRoute(
    modifier: Modifier = Modifier,
    viewModel: SourceViewModel = hiltViewModel(),
) {
    val sourceUiState: SourceUiState by viewModel.sourceUiState.collectAsStateWithLifecycle()
    val countryUiState: CountryUiState by viewModel.countryUiState.collectAsStateWithLifecycle()

    SourceScreen(
        modifier = modifier,
        sourceUiState = sourceUiState,
        countryUiState = countryUiState
    )
}


@Composable
private fun SourceScreen(
    modifier: Modifier = Modifier,
    sourceUiState: SourceUiState,
    countryUiState: CountryUiState
) {
    val source = sourceUiState.source

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Cell(title = "Name", value = source?.name)
        Cell(title = "Category", value = source?.category)
        Cell(title = "Description", value = source?.description)
        Cell(title = "Language", value = source?.language)
        Spacer(modifier = Modifier.height(80.dp))
        CountryCard(countryUiState = countryUiState)
    }
}

@Composable
private fun Cell(title: String, value: String?) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value ?: "N/A",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun CountryCard(countryUiState: CountryUiState) {
    val country = (countryUiState as? CountryUiState.Success)?.country

    country?.emoji?.let { emoji ->
        Text(text = emoji, fontSize = 40.sp)
        Spacer(modifier = Modifier.height(8.dp))
    }

    Card(
        border = BorderStroke(width = 1.dp, color = Color.Black),
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
    ) {
        when (countryUiState) {
            is CountryUiState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp, top = 12.dp)
                        .weight(0.7f)
                ) {
                    Cell(title = "Name", value = country?.name ?: "N/A")
                    Cell(title = "Continent", value = country?.continent ?: "N/A")
                    Cell(title = "Capital", value = country?.capital ?: "N/A")
                    Cell(
                        title = "Number of states",
                        value = (country?.statesCount?.toString()) ?: "N/A"
                    )
                }
            }

            is CountryUiState.Error -> {
                Text(
                    text = "Could not load country",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            }

            is CountryUiState.Loading -> {
                NewsAppDefaultProgressIndicator(placeOnCenter = true)
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview() {
    SourceScreen(
        sourceUiState = SourceUiState(
            source = NewsSource(
                id = "",
                name = "Source",
                description = null,
                url = null,
                category = null,
                language = null,
                country = null
            )
        ),
        countryUiState = CountryUiState.Success(
            country = Country(
                name = "United States",
                capital = "Washington",
                continent = "America",
                statesCount = 60,
                emoji = "\uD83C\uDDFA"
            )
        ),
        //countryUiState = CountryUiState.Error,
        //countryUiState = CountryUiState.Loading,
        modifier = Modifier.background(Color.White)
    )
}