package leo.decentralized.tonchat.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.presentation.screens.DefaultScreen
import leo.decentralized.tonchat.utils.getText

@Composable
fun NewContactSearchScreen(onBack:()->Unit,onSearch:(address:String)->Unit){
    DefaultScreen(screenName = "New contact", onPrimaryClick = onBack, primaryButtonIcon = Icons.AutoMirrored.Default.ArrowBack){
        item {
            var address by rememberSaveable{
                mutableStateOf("")
            }
            val clipBoard = LocalClipboard.current
            val coroutine = rememberCoroutineScope()
            OutlinedTextField(
                value = address,
                onValueChange = {address = it},
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .heightIn(max = 150.dp),
                placeholder = {
                    Text("Enter V4R2 Ton Address")
                },
                suffix = {
                    if (address.isEmpty()){
                        Text("Paste",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W400) ,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .clickable {
                                    coroutine.launch(Dispatchers.IO){
                                        address = clipBoard.getClipEntry()?.getText()?:""
                                    }

                                })
                    }else{
                        Icon(
                            Icons.Default.Search,
                            "",
                            modifier = Modifier
                                .padding(start = 4.dp)
                        )
                    }
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W400),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.outline,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    if (address.isNotEmpty()) {
                        onSearch(address)
                    }
                },
                enabled = address.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(40)
            ) {
                Text(
                    text = "Search and add",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W400)
                )
            }
        }
    }
}