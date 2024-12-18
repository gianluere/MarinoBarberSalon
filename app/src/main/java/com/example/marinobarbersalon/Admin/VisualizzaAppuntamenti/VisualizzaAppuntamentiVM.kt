package com.example.marinobarbersalon.Admin.VisualizzaAppuntamenti

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marinobarbersalon.Admin.Admin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColor
import com.example.marinobarbersalon.Cliente.Account.Appuntamento
import com.example.marinobarbersalon.Cliente.Home.User
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_gold
import com.example.marinobarbersalon.ui.theme.my_grey
import com.example.marinobarbersalon.ui.theme.my_white
import com.example.marinobarbersalon.ui.theme.my_yellow
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.LocalDate




class VisualizzaAppuntamentiVM: ViewModel() {
    
    //per la prima pagina
    private val _calendarState = MutableStateFlow(CalendarState())
    val calendarState : StateFlow<CalendarState> = _calendarState.asStateFlow()
    //per la seconda pagina
    private val firestore = FirebaseFirestore.getInstance()
    private val _appuntamentiState = MutableStateFlow<List<Appuntamento>>(emptyList())
    val appuntamentiState: StateFlow<List<Appuntamento>> = _appuntamentiState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun selectDate(date: String) {
        _calendarState.value = CalendarState(selectedDate = date)
    }

    fun onConfirm(onNavigate: () -> Unit) {
        viewModelScope.launch{
            onNavigate()
        }
    }

    suspend fun getAppuntamentiByDate(date: String) {
        _isLoading.value = true
        try {
            val appuntamenti = fetchAppuntamentiFromFirestore(date)
            _appuntamentiState.value = appuntamenti
        } catch (e: Exception) {
            _appuntamentiState.value = emptyList()
        } finally {
            _isLoading.value = false
        }
    }

     private suspend fun fetchAppuntamentiFromFirestore(date: String): List<Appuntamento> {
        val db = FirebaseFirestore.getInstance()
        val appuntamentiRef = db.collection("appuntamenti").document(date).collection("app")
        val querySnapshot = appuntamentiRef.get().await()
        return querySnapshot.documents.map { document ->
            val clienteRef = document.getDocumentReference("cliente")
            val servizio = document.getString("servizio") ?: ""
            val descrizione = document.getString("descrizione") ?: ""
            val orarioInizio = document.getString("orarioInizio") ?: ""
            val orarioFine = document.getString("orarioFine") ?: ""
            val prezzo = document.getDouble("prezzo") ?: 0.0

            Appuntamento(
                cliente = clienteRef,
                servizio = servizio,
                descrizione = descrizione,
                orarioInizio = orarioInizio,
                orarioFine = orarioFine,
                prezzo = prezzo,
                data = date
            )
        }
    }
}

@Composable
fun Calendar(
    onDateSelected: (String) -> Unit
) {
    val currentDate = remember { LocalDate.now() }
    var selectedMonth by remember { mutableStateOf(currentDate.month) }
    var selectedYear by remember { mutableStateOf(currentDate.year) }

    val daysInMonth = remember(selectedMonth, selectedYear) {
        YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
    }

    val firstDayOfWeek = remember(selectedMonth, selectedYear) {
        YearMonth.of(selectedYear, selectedMonth)
            .atDay(1)
            .dayOfWeek
            .value % 7
    }

    val dates = remember(selectedMonth, selectedYear) {
        buildList {
            repeat(firstDayOfWeek) { add(null) }
            addAll((1..daysInMonth).map { it })
        }
    }


    val italianMonthNames = listOf(
        "Gen", "Feb", "Mar", "Apr", "Mag", "Giu", "Lug", "Ago", "Set", "Ott", "Nov", "Dic"
    )

    val italianWeekDays = listOf("Dom", "Lun", "Mar", "Mer", "Gio", "Ven", "Sab")

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                if (selectedMonth == Month.JANUARY) {
                    selectedMonth = Month.DECEMBER
                    selectedYear -= 1
                } else {
                    selectedMonth = selectedMonth.minus(1)
                }
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Mese Precedente", tint = my_white)
            }

            Text(
                text = "${italianMonthNames[selectedMonth.value - 1]} $selectedYear",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontFamily = myFont,
                color = my_white,
                fontSize = 20.sp
            )

            IconButton(onClick = {
                if (selectedMonth == Month.DECEMBER) {
                    selectedMonth = Month.JANUARY
                    selectedYear += 1
                } else {
                    selectedMonth = selectedMonth.plus(1)
                }
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Mese prossimo", tint = my_white)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            italianWeekDays.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                    color = my_white,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(dates) { day ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable(enabled = day != null) {
                            if (day != null) {
                                val selectedDate = LocalDate.of(selectedYear, selectedMonth, day)
                                val formattedDate = selectedDate.format(formatter)
                                onDateSelected(formattedDate)
                            }
                        }
//                        .background(
//                            if (day == currentDate.dayOfMonth &&
//                                selectedMonth == currentDate.month &&
//                                selectedYear == currentDate.year
//                            ) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
//                            else MaterialTheme.colorScheme.surface,
//                            shape = RoundedCornerShape(4.dp)
//                        )
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day?.toString() ?: "",
                        //style = MaterialTheme.typography.bodySmall,
                        color = my_white,
                        fontFamily = myFont,
                        fontSize = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}

@Composable
fun AppointmentItem(appuntamento: Appuntamento) {
    val db = FirebaseFirestore.getInstance()
    val clienteDocument = remember(appuntamento.cliente) { appuntamento.cliente }

    var nomeCliente by remember { mutableStateOf("") }

    LaunchedEffect(clienteDocument) {
        if (clienteDocument != null) {
            // Carica i dettagli del cliente dal Firestore
            val clienteSnapshot = clienteDocument.get().await()
            val user = clienteSnapshot.toObject(User::class.java)
            nomeCliente = user?.nome ?: "Cliente non trovato"
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .border(2.dp, my_gold, RoundedCornerShape(17.dp)),
        shape = RoundedCornerShape(17.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = my_yellow
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Orario: ${appuntamento.orarioInizio} - ${appuntamento.orarioFine}",
                fontFamily = myFont,
                fontSize = 18.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Cliente: $nomeCliente",
                fontFamily = myFont,
                fontSize = 18.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Servizio: ${appuntamento.servizio}",
                fontFamily = myFont,
                fontSize = 18.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Prezzo: €${"%.2f".format(appuntamento.prezzo)}",
                fontFamily = myFont,
                fontSize = 18.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

