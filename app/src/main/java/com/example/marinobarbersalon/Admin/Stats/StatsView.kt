package com.example.marinobarbersalon.Admin.Stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_icon
import com.example.marinobarbersalon.ui.theme.my_yellow


//--------------------------------------------------------------------------------------------------
//PAGINA SCELTA STATISTICHE
//--------------------------------------------------------------------------------------------------
@Composable
fun VisualizzaStatistiche(
    onNavigateToVisualizzaStatisticheAppuntamenti: () -> Unit,
    onNavigateToVisualizzaStatisticheClienti: () -> Unit,
    onNavigateToVisualizzaServiziPiuRichiesti: () -> Unit,
    onNavigateToVisualizzaEntrateMensili: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Seleziona una statistica:",
            fontFamily = myFont,
            fontSize = 27.sp,
            color = my_white,
            modifier = Modifier
                .padding(top = 100.dp)
                .padding(bottom = 32.dp)
        )


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp), //Spaziatura tra le card
            contentPadding = PaddingValues(top = 25.dp)
        ) {
            item {
                //Riga per le prime due statistiche
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    //Statistica 1: Appuntamenti
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .border(width = 2.dp, color = my_gold, shape = RoundedCornerShape(25.dp))
                                .background(color = my_white, shape = RoundedCornerShape(25.dp))
                                .aspectRatio(1f)
                                .clickable {
                                    onNavigateToVisualizzaStatisticheAppuntamenti()
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarMonth,
                                contentDescription = "Icona Appuntamenti",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                tint = my_icon
                            )
                        }

                        Text(
                            text = "APPUNTAMENTI",
                            fontSize = 20.sp,
                            fontFamily = myFont,
                            color = my_white
                        )

                    }

                    //Statistica 2: Entrate
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .border(width = 2.dp, color = my_gold, shape = RoundedCornerShape(25.dp))
                                .background(color = my_white, shape = RoundedCornerShape(25.dp))
                                .aspectRatio(1f)
                                .clickable {
                                    onNavigateToVisualizzaEntrateMensili()
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.QueryStats, //oppure Icons.Filled.TrendingUp chiedere quale è meglio
                                contentDescription = "Icona Entrare",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                tint = my_icon
                            )
                        }

                        Text(
                            text = "ENTRATE",
                            fontSize = 20.sp,
                            fontFamily = myFont,
                            color = my_white
                        )
                    }
                }
            }

            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    //Statistica 3: Clienti attivi/inattivi
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .border(width = 2.dp, color = my_gold, shape = RoundedCornerShape(25.dp))
                                .background(color = my_white, shape = RoundedCornerShape(25.dp))
                                .aspectRatio(1f)
                                .clickable {
                                    onNavigateToVisualizzaStatisticheClienti()
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Group,
                                contentDescription = "Icona clienti",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                tint = my_icon
                            )
                        }

                        Text(
                            text = "CLIENTI",
                            fontSize = 20.sp,
                            fontFamily = myFont,
                            color = my_white
                        )
                    }

                    //Statistica 4: Servizi
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .border(width = 2.dp, color = my_gold, shape = RoundedCornerShape(25.dp))
                                .background(color = my_white, shape = RoundedCornerShape(25.dp))
                                .aspectRatio(1f)
                                .clickable {
                                    onNavigateToVisualizzaServiziPiuRichiesti()
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ContentCut,
                                contentDescription = "Icona servizi",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                tint = my_icon

                            )
                        }

                        Text(
                            text = "SERVIZI",
                            fontSize = 20.sp,
                            fontFamily = myFont,
                            color = my_white
                        )
                    }
                }
            }
        }
    }
}
//--------------------------------------------------------------------------------------------------



//--------------------------------------------------------------------------------------------------
//PAGINA 1: STATISTICHE APPUNTAMENTI
//--------------------------------------------------------------------------------------------------
@Composable
fun VisualizzaStatisticheAppuntamenti(
    statsViewModel: StatsVM = viewModel()
) {
    val selectedInterval = remember { mutableStateOf("giorno") }
    val appuntamentiPerIntervallo = statsViewModel.appuntamentiPerIntervallo.collectAsState().value
    val isLoading = statsViewModel.isLoading.collectAsState().value // Stato di caricamento

    LaunchedEffect(selectedInterval.value) {
        statsViewModel.getAppuntamentiPerIntervallo(selectedInterval.value)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Statistiche Appuntamenti",
            fontFamily = myFont,
            fontSize = 28.sp,
            color = my_white,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { selectedInterval.value = "giorno" },
                modifier = Modifier.align(Alignment.CenterVertically),
                colors = ButtonDefaults.buttonColors(
                    containerColor = my_bordeaux,
                    disabledContainerColor = my_bordeaux
                )
            ) {
                Text("Giorno", color = my_white)
            }

            Button(
                onClick = { selectedInterval.value = "mese" },
                modifier = Modifier.align(Alignment.CenterVertically),
                colors = ButtonDefaults.buttonColors(
                    containerColor = my_bordeaux,
                    disabledContainerColor = my_bordeaux
                )
            ) {
                Text("Mese", color = my_white)
            }

            Button(
                onClick = { selectedInterval.value = "anno" },
                modifier = Modifier.align(Alignment.CenterVertically),
                colors = ButtonDefaults.buttonColors(
                    containerColor = my_bordeaux,
                    disabledContainerColor = my_bordeaux
                )
            ) {
                Text("Anno", color = my_white)
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                color = my_gold,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(50.dp)
            )
        } else {
            // Mostra il grafico o il messaggio di nessun dato
            if (appuntamentiPerIntervallo.isNotEmpty()) {
                BarChart(appuntamentiPerIntervallo = appuntamentiPerIntervallo)
            } else {
                Text(
                    text = "Nessun dato disponibile per l'intervallo selezionato.",
                    color = my_white,
                    fontFamily = myFont
                )
            }
        }
    }
}

@Composable
fun BarChart(appuntamentiPerIntervallo: List<Pair<String, Int>>) {
    val maxValue = appuntamentiPerIntervallo.maxOfOrNull { it.second }?.toFloat() ?: 1f
    val barWidth = 90.dp //Larghezza fissa delle barre
    val labelTextStyle = androidx.compose.ui.text.TextStyle(
        color = Color.White,
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    )


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp) //Altezza totale del grafico
            .background(Color.Transparent)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp), //Altezza specifica per le barre
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(appuntamentiPerIntervallo) { pair ->
                val barHeightFactor = 200.dp / maxValue //Scala altezza dinamicamente

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    //Valore numerico sopra la barra
                    Text(
                        text = pair.second.toString(),
                        style = labelTextStyle,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Box(
                        modifier = Modifier
                            .width(barWidth)
                            .height((pair.second * barHeightFactor.value).dp)
                            .background(my_bordeaux, RoundedCornerShape(4.dp))
                    )

                    //Etichetta sotto la barra
                    Text(
                        text = pair.first,
                        style = labelTextStyle,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .width(barWidth),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = my_white
                    )
                }
            }
        }
    }
}
//--------------------------------------------------------------------------------------------------



//--------------------------------------------------------------------------------------------------
//PAGINA 2: STATISTICHE APPUNTAMENTI RECENTI VS APPUNTAMENTI VECCHI
//--------------------------------------------------------------------------------------------------
@Composable
fun VisualizzaStatisticheClienti(
    statsViewModel: StatsVM = viewModel()
) {
    val isLoadingClienti = statsViewModel.isLoadingClienti.collectAsState().value
    val clientiStats = statsViewModel.clientiStats.collectAsState().value

    LaunchedEffect(Unit) {
        statsViewModel.calcolaClientiAttiviInattivi()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Titolo
        Text(
            text = "Statistiche Clienti",
            fontFamily = myFont,
            fontSize = 28.sp,
            color = my_white,
            modifier = Modifier.padding(bottom = 0.dp)
        )

        //Contenitore per centrare il grafico
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (isLoadingClienti) {
                CircularProgressIndicator(
                    color = my_gold,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(100.dp)
                )
            } else {
                if (clientiStats.first + clientiStats.second > 0) {
                    GraficoTortaClienti(attivi = clientiStats.first, inattivi = clientiStats.second)
                } else {
                    Text(
                        text = "Nessun dato disponibile.",
                        color = my_white,
                        fontFamily = myFont,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * E' posibile utilizzare sia BarChart sia il grafico a torta
 * (basta chiamare una o l'altra funzione CON GLI STESSI PARAMETRI!!)
 * */
@Composable
fun BarChartClienti(attivi: Int, inattivi: Int) {
    val clientiData = listOf("Attivi" to attivi, "Inattivi" to inattivi)
    val maxValue = clientiData.maxOfOrNull { it.second }?.toFloat() ?: 1f
    val barWidth = 90.dp //Larghezza fissa delle barre
    val labelTextStyle = androidx.compose.ui.text.TextStyle(
        color = my_white,
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp) //Altezza tot grafico
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp), //Altezza barre
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(clientiData) { pair ->
                val barHeightFactor = 200.dp / maxValue //Scala altezza dinamicamente

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    //Valore numerico sopra la barra
                    Text(
                        text = pair.second.toString(),
                        style = labelTextStyle,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Box(
                        modifier = Modifier
                            .width(barWidth)
                            .height((pair.second * barHeightFactor.value).dp)
                            .background(
                                color = if (pair.first == "Attivi") my_gold else my_bordeaux,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )

                    //Etichetta sotto la barra
                    Text(
                        text = pair.first,
                        style = labelTextStyle,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .width(barWidth),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = my_white
                    )
                }
            }
        }
    }
}

@Composable
fun GraficoTortaClienti(attivi: Int, inattivi: Int) {
    val totale = attivi + inattivi
    val percentualeAttivi = if (totale > 0) attivi.toFloat() / totale else 0f
    val sweepAngleAttivi = percentualeAttivi * 360f


    val density = LocalDensity.current
    val radius = with(density) { 155.dp.toPx() } //Raggio del cerchio per posizionare le etichette

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp), //Altezza totale del contenitore
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(250.dp) //Dimensione del grafico
        ) {
            val circleSize = Size(size.minDimension, size.minDimension)
            val center = Offset(size.width / 2, size.height / 2)

            //Fetta per i clienti attivi
            drawArc(
                color = my_gold,
                startAngle = 0f,
                sweepAngle = sweepAngleAttivi,
                useCenter = true,
                size = circleSize,
                topLeft = Offset(
                    (size.width - circleSize.width) / 2f,
                    (size.height - circleSize.height) / 2f
                )
            )

            //Fetta per i clienti inattivi
            drawArc(
                color = my_bordeaux,
                startAngle = sweepAngleAttivi,
                sweepAngle = 360f - sweepAngleAttivi,
                useCenter = true,
                size = circleSize,
                topLeft = Offset(
                    (size.width - circleSize.width) / 2f,
                    (size.height - circleSize.height) / 2f
                )
            )

            //Posizionamento dinamico dell'etichetta per i clienti attivi
            val angleAttivi = sweepAngleAttivi / 2
            val attiviX =
                center.x + radius * kotlin.math.cos(Math.toRadians(angleAttivi.toDouble()))
                    .toFloat()
            val attiviY =
                center.y + radius * kotlin.math.sin(Math.toRadians(angleAttivi.toDouble()))
                    .toFloat()
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "Attivi",
                    attiviX,
                    attiviY - 20f, //Posizione sopra la percentuale
                    android.graphics.Paint().apply {
                        color = my_gold.toArgb()
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 20.dp.toPx()
                    }
                )
                drawText(
                    "${((percentualeAttivi) * 100).toInt()}%",
                    attiviX,
                    attiviY + 50f, //Posizione sotto "Attivi"
                    android.graphics.Paint().apply {
                        color = my_gold.toArgb()
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 20.dp.toPx()
                    }
                )
            }

            //Posizionamento dinamico dell'etichetta per i clienti inattivi
            val angleInattivi =
                sweepAngleAttivi + (360f - sweepAngleAttivi) / 2 //Angolo medio della fetta
            /**
            * una volta che sto a metà angolo della getta inattivi allora:
             *
             * cos: Calcola l'offset orizzontale (X) relativo all'angolo dato.
             * sin: Calcola l'offset verticale (Y) relativo all'angolo dato.
            * */


            val inattiviX =
                center.x + radius * kotlin.math.cos(Math.toRadians(angleInattivi.toDouble()))
                    .toFloat()
            val inattiviY =
                center.y + radius * kotlin.math.sin(Math.toRadians(angleInattivi.toDouble()))
                    .toFloat()
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "Inattivi",
                    inattiviX,
                    inattiviY - 20f, //Posizione sopra la percentuale
                    android.graphics.Paint().apply {
                        color = my_bordeaux.toArgb()
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 20.dp.toPx()
                    }
                )
                drawText(
                    "${((1 - percentualeAttivi) * 100).toInt()}%",
                    inattiviX,
                    inattiviY + 50f, //Posizione sotto "Inattivi"
                    android.graphics.Paint().apply {
                        color = my_bordeaux.toArgb()
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 20.dp.toPx()
                    }
                )
            }
        }
    }
}
//--------------------------------------------------------------------------------------------------




//--------------------------------------------------------------------------------------------------
//TERZA PAGINA: STATS SERVIZI
//--------------------------------------------------------------------------------------------------
@Composable
fun VisualizzaServiziPiuRichiesti(
    statsViewModel: StatsVM = viewModel()
) {
    val serviziStats = statsViewModel.serviziStats.collectAsState().value
    val isLoadingServizi = statsViewModel.isLoadingServizi.collectAsState().value

    LaunchedEffect(Unit) {
        statsViewModel.calcolaServiziPiuRichiesti()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Servizi più richiesti",
            fontFamily = myFont,
            fontSize = 28.sp,
            color = my_white,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoadingServizi) {
            CircularProgressIndicator(
                color = my_gold,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(50.dp)
            )
        } else {
            if (serviziStats.isNotEmpty()) {
                BarChartServizi(serviziStats)
            } else {
                Text(
                    text = "Nessun dato disponibile.",
                    color = my_white,
                    fontFamily = myFont
                )
            }
        }
    }
}

@Composable
fun BarChartServizi(serviziStats: Map<String, Int>) {
    val maxValue = serviziStats.values.maxOrNull()?.toFloat() ?: 1f
    val barWidth = 90.dp //Larghezza fissa delle barre
    val labelTextStyle = androidx.compose.ui.text.TextStyle(
        color = Color.White,
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp) //Altezza totale del grafico
            .background(Color.Transparent)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp), //Altezza specifica per le barre
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(serviziStats.entries.toList()) { servizio ->
                val barHeightFactor = 200.dp / maxValue //Scala altezza dinamicamente

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    //Valore numerico sopra la barra
                    Text(
                        text = servizio.value.toString(),
                        style = labelTextStyle,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )


                    Box(
                        modifier = Modifier
                            .width(barWidth)
                            .height((servizio.value * barHeightFactor.value).dp)
                            .background(my_bordeaux, RoundedCornerShape(4.dp))
                    )

                    //Etichetta sotto la barra
                    Text(
                        text = servizio.key,
                        style = labelTextStyle,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .width(barWidth),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = my_white
                    )
                }
            }
        }
    }
}
//--------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------
//QUARTA PAGINA: ENTRATE MENSILI
//--------------------------------------------------------------------------------------------------
@Composable
fun VisualizzaEntrateMensili(statsViewModel: StatsVM = viewModel()) {
    val entrateMensili = statsViewModel.entrateMensili.collectAsState().value
    val isLoadingEntrate = statsViewModel.isLoadingEntrate.collectAsState().value

    LaunchedEffect(Unit) {
        statsViewModel.calcolaEntrateMensili()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 64.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titolo
        Text(
            text = "Entrate Mensili",
            fontFamily = myFont,
            fontSize = 28.sp,
            color = my_white,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoadingEntrate) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = my_gold,
                    modifier = Modifier.size(100.dp)
                )
            }
        } else {
            if (entrateMensili.isEmpty()) {
                Text(
                    text = "Nessun dato disponibile.",
                    color = my_white,
                    fontFamily = myFont,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(entrateMensili) { (mese, entrata) ->
                        EntrateMensiliCard(
                            mese = mese,
                            entrata = entrata
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun EntrateMensiliCard(mese: String, entrata: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(2.dp, my_gold, RoundedCornerShape(17.dp)),
        shape = RoundedCornerShape(17.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = my_yellow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                //Nome mese
                Text(
                    text = mese,
                    fontFamily = myFont,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Color.Black
                )

                //Entrata mensile
                Text(
                    text = "Entrate: €${"%.2f".format(entrata)}",
                    fontFamily = myFont,
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
//--------------------------------------------------------------------------------------------------






























