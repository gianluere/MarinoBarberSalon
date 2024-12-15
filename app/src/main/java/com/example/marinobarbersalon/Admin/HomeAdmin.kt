package com.example.marinobarbersalon.Admin

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.marinobarbersalon.Cliente.Home.AuthState
import com.example.marinobarbersalon.Cliente.Home.UserFirebase
import com.example.marinobarbersalon.ui.theme.myFont
import com.example.marinobarbersalon.ui.theme.my_bordeaux
import com.example.marinobarbersalon.ui.theme.my_grey
import com.github.javafaker.Faker
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun HomeAdmin(modifier: Modifier = Modifier, adminViewModel: AdminViewModel, onNavigateToLogin : () -> Unit) {
    val adminState by adminViewModel.adminState.collectAsState()

    LaunchedEffect(adminState.state){
        when(adminState.state){
            is AuthState.Unauthenticated -> onNavigateToLogin()
            else -> Unit
        }
    }

    Column(
        modifier = modifier
    ) { Text("Visualizza appuntamenti",
              style = MaterialTheme.typography.bodyLarge
                    )
        Button(onClick = {
            adminViewModel.logout()
        }) {
            Text(text = "Logout")
        }
        Button(
            onClick = {
                insertFakeUsers(20)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = my_bordeaux,
                disabledContainerColor = my_bordeaux
            )
        ){
            Text("FAKER UTENTI")
        }
    }
}


//generazione utenti con faker perche mi servono in "visualizza clienti"
fun generateFakeUsers(numberOfUsers: Int): List<UserFirebase> {
    val faker = Faker()
    val users = mutableListOf<UserFirebase>()

    for (i in 0 until numberOfUsers) {
        val nome = faker.name().firstName()
        val cognome = faker.name().lastName()
        val email = faker.internet().emailAddress()
        val eta = (18..80).random()
        val telefono = faker.phoneNumber().cellPhone()

        users.add(UserFirebase(nome, cognome, email, eta, telefono))
    }
    return users
}
fun addUsersToFirestore(users: List<UserFirebase>) {
    val db = FirebaseFirestore.getInstance()

    users.forEach { user ->
        db.collection("utenti").document(user.email).set(
            hashMapOf(
                "nome" to user.nome,
                "cognome" to user.cognome,
                "email" to user.email,
                "eta" to user.eta,
                "telefono" to user.telefono,
                "appuntamenti" to emptyList<String>()
            )
        ).addOnSuccessListener {
            Log.d("admin", "Utente ${user.email} aggiunto con successo!")
        }.addOnFailureListener { e ->
            Log.d("admin", "Errore durante l'aggiunta dell'utente ${user.email}", e)
        }
    }
}
fun insertFakeUsers(n: Int) {
    val fakeUsers = generateFakeUsers(n)
    addUsersToFirestore(fakeUsers)
}
