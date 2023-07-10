package com.example.contactcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.contactcompose.ui.theme.ContactComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactListScreen()
                }
            }
        }
    }
}

data class Contact(val name: String, val email: String, val phoneNumber: String)

@Composable
fun ContactListScreen() {
    val contacts = remember { mutableStateListOf<Contact>() }
    val selectedContact = remember { mutableStateOf<Contact?>(null) }

    Column {
        ContactForm(onAddContact = { name, email, phoneNumber ->
            contacts.add(Contact(name, email, phoneNumber))
        },
//            onEditContact = { contact ->
//            selectedContact.value = contact
//        }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ContactList(
            contacts = contacts,
            onEditContact = { contact -> selectedContact.value = contact },
            onDeleteContact = { contact -> contacts.remove(contact) }
        )

        selectedContact.value?.let { contact ->
            ContactDialog(
                contact = contact,
                onDismissRequest = { selectedContact.value = null },
                onContactUpdated = { name, email, phoneNumber ->
                    contacts[contacts.indexOf(contact)] = contact.copy(
                        name = name,
                        email = email,
                        phoneNumber = phoneNumber
                    )
                    selectedContact.value = null
                }
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactForm(onAddContact: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf(value = "") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Phone Number") }
        )
        Spacer(modifier = Modifier.height(16.dp))

//
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(onClick = { onAddContact(name, email, phoneNumber) }) {
                Text("Add Contact")
            }
        }
    }
}

@Composable
fun ContactList(contacts: List<Contact>, onEditContact: (Contact) -> Unit, onDeleteContact: (Contact) -> Unit) {
    LazyColumn {
        contacts.reversed().forEach { contact ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = contact.name, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = contact.email)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = contact.phoneNumber)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = { onEditContact(contact) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Edit Contact")
                            }
                            IconButton(onClick = { onDeleteContact(contact) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete Contact")
                            }
                        }
                    }
                }
            }

//            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDialog(
    contact: Contact,
    onDismissRequest: () -> Unit,
    onContactUpdated: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(contact.name) }
    var email by remember { mutableStateOf(contact.email) }
    var phoneNumber by remember { mutableStateOf(contact.phoneNumber) }

    Dialog(onDismissRequest) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
//            backgroundColor = Color.White
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Phone Number") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onContactUpdated(name, email, phoneNumber) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContactComposeTheme {
        ContactListScreen()
    }
}