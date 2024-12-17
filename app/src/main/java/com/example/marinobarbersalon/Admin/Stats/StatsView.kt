package com.example.marinobarbersalon.Admin.Stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marinobarbersalon.Admin.VisualizzaProdotti.VisualizzaProdottiVM
import com.example.marinobarbersalon.R
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_white

import androidx.compose.ui.viewinterop.AndroidView




/*
1. Numero di appuntamenti per giorno/settimana/mese:
    Descrizione: Una statistica che mostra quanti appuntamenti sono stati prenotati in un dato intervallo
     di tempo (giorno, settimana o mese).

    Tipo di Grafico: Line Chart o Bar Chart.

    Dati: Numero di appuntamenti per ciascun giorno, settimana o mese.


2. Numero di clienti attivi vs inattivi;
    Descrizione: Percentuale di clienti che hanno prenotato appuntamenti recenti rispetto a quelli
     che non hanno effettuato prenotazioni da un certo periodo di tempo.

    Tipo di Grafico: Pie Chart.

    Dati: Numero di clienti con appuntamenti negli ultimi 30 giorni vs clienti con appuntamenti passati.



3. Servizi più richiesti:
    Descrizione: Un grafico che mostra quali servizi sono stati prenotati di più in un determinato periodo.

    Tipo di Grafico: Bar Chart o Pie Chart.

    Dati: Numero di volte che ogni servizio è stato prenotato in un dato periodo.


4. Recensioni dei clienti:
    Descrizione: Un'analisi delle recensioni ricevute dai clienti, mostrando la distribuzione delle
     recensioni per rating (ad esempio, da 1 a 5 stelle).

    Tipo di Grafico: Bar Chart (o Pie Chart).

    Dati: Numero di recensioni per ciascun punteggio (1 stella, 2 stelle, ecc.).


5. Entrate mensili:
    Descrizione: Le entrate generate dal salone, divise per mese, per avere un'idea chiara delle performance finanziarie.

    Tipo di Grafico: Line Chart o Bar Chart.

    Dati: Somma delle entrate per ogni mese.

*/

//--------------------------------------------------------------------------------------------------
//PAGINA SCELTA STATISTICHE
@Composable
fun VisualizzaStatistiche(
    onNavigateToNextPage: (String) -> Unit
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

        // Avvolgiamo il contenuto delle card in un LazyColumn per renderle scrollabili
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp), // Spaziatura tra le card
            contentPadding = PaddingValues(top = 10.dp) // Aggiungiamo un po' di spazio in alto
        ) {
            item {
                // Riga per le prime due statistiche
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Statistica 1: Appuntamenti
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
                                    onNavigateToNextPage("appuntamenti")
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.barba_icona),
                                contentDescription = "Icona Appuntamenti",
                                modifier = Modifier.padding(30.dp)
                            )
                        }

                        Text(
                            text = "APPUNTAMENTI",
                            fontSize = 20.sp,
                            fontFamily = myFont,
                            color = my_white
                        )
                    }

                    // Statistica 2: Entrate
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
                                    onNavigateToNextPage("entrate")
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.barba_icona),
                                contentDescription = "Icona Entrate",
                                modifier = Modifier.padding(30.dp)
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
                // Riga per le statistiche restanti
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Statistica 4: Clienti attivi/inattivi
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
                                    onNavigateToNextPage("clienti_attivi")
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.barba_icona),
                                contentDescription = "Icona Clienti",
                                modifier = Modifier.padding(30.dp)
                            )
                        }

                        Text(
                            text = "CLIENTI",
                            fontSize = 20.sp,
                            fontFamily = myFont,
                            color = my_white
                        )
                    }

                    // Statistica 5: Recensioni
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
                                    onNavigateToNextPage("recensioni")
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.barba_icona),
                                contentDescription = "Icona Recensioni",
                                modifier = Modifier.padding(30.dp)
                            )
                        }

                        Text(
                            text = "RECENSIONI",
                            fontSize = 20.sp,
                            fontFamily = myFont,
                            color = my_white
                        )
                    }
                }
            }

            item {
                // Riga per le altre statistiche
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    // Statistica 3: Servizi più richiesti
                    Box(
                        modifier = Modifier
                            .border(width = 2.dp, color = my_gold, shape = RoundedCornerShape(25.dp))
                            .background(color = my_white, shape = RoundedCornerShape(25.dp))
                            .fillMaxWidth(0.5f)
                            .clickable {
                                onNavigateToNextPage("servizi_richiesti")
                            }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.barba_icona),
                            contentDescription = "Icona Servizi",
                            modifier = Modifier.padding(top = 30.dp, bottom = 30.dp, start = 30.dp, end = 20.dp)
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
//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------
//PAGINA 1: STATISTICHE APPUNTAMENTI
//



