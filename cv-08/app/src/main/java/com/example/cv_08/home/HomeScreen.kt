package com.example.cv_08.home

import android.os.Build
import android.provider.CalendarContract
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cv_08.data.DataHolder
import com.example.cv_08.data.WeatherApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

suspend fun fetchData (client: HttpClient, query: String): WeatherApiResponse{
    val data : WeatherApiResponse = client.get("https://api.openweathermap.org/data/2.5/forecast?appid=c0bd78361293b062285d5a7ed470690d&q=$query&units=metric").body()
    println(data)
    return data
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onWeatherForecastClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var cityToSearch by remember {
        val targetCity = DataHolder.selectedCity ?: DataHolder.currentCity
        DataHolder.selectedCity = null
        DataHolder.currentCity = targetCity
        mutableStateOf(targetCity)
    }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    var data by remember { mutableStateOf<WeatherApiResponse?>(null) }

    val cornerRadius = 12.dp

    val client = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    LaunchedEffect(key1 = cityToSearch) {
        isLoading = true

        try {
            data = fetchData(client, cityToSearch)
            DataHolder.forecastData = data
        }
        catch(e: Exception) {
            isError = true
        }
        finally {
            isLoading = false
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Current weather")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }
    ) { innerPadding ->
        if(isError) {
            ErrorDialog(
                title = "Error",
                message = "An error has occured when fetching data from API.",
                onDismiss = {isError = false}
            )
        }
        Column (
            modifier = Modifier.padding(innerPadding).padding(20.dp)
        ) {
            Row (
               modifier = Modifier.height(IntrinsicSize.Min).fillMaxWidth()
            ) {
               OutlinedTextField (
                   value = searchText,
                   onValueChange = { searchText = it },
                   label = { Text("Search a place...") },
                   placeholder = { Text("Example: Ostrava") },
                   shape = RoundedCornerShape (
                       topStart = cornerRadius,
                       bottomStart = cornerRadius,
                       topEnd = 0.dp,
                       bottomEnd = 0.dp
                   ),
                   modifier = Modifier.weight(1f)
               )
               Button(
                   onClick = {
                       if(searchText.isNotBlank()) {
                           cityToSearch = searchText
                           DataHolder.currentCity = searchText
                       }
                   },
                   modifier = Modifier.fillMaxHeight().padding(top = 8.dp),
                   shape = RoundedCornerShape (
                       topStart = 0.dp,
                       bottomStart = 0.dp,
                       topEnd = cornerRadius,
                       bottomEnd = cornerRadius
                   ),
               ) {
                   if(isLoading) {
                       CircularProgressIndicator(
                           trackColor = MaterialTheme.colorScheme.onPrimary,
                           modifier = Modifier.size(24.dp),
                           strokeWidth = 2.dp
                       )
                   }
                   else {
                       Icon(
                           imageVector = Icons.Outlined.Search,
                           contentDescription = "Search"
                       )
                   }
               }
            }

            Spacer(modifier = Modifier.height(30.dp))

            if(isLoading || data == null) {
                Column (
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else {
                CurrentWeather(
                    city = data!!.city,
                    weather = data!!.list.first()
                )

                Spacer(modifier = Modifier.height(30.dp))

                ForecastChart(
                    forecast = data!!
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}