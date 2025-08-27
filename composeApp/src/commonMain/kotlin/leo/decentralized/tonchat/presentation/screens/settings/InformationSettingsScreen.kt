package leo.decentralized.tonchat.presentation.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import leo.decentralized.tonchat.presentation.screens.DefaultScreen

@Composable
fun InformationSettingsScreen(onBackClick: () -> Unit) {
    DefaultScreen(
        screenName = "Information",
        primaryButtonIcon = Icons.AutoMirrored.Default.ArrowBack,
        onPrimaryClick = {
            onBackClick()
        }) {
        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = "Device info"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Secure enclave, key store"
                    )
                },
                trailingContent = {},
                //  colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    role = Role.Button
                ) {
                    //todo
                }.clip(RoundedCornerShape(12))
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = "App availability"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Download for Mac, Web, Ios, Android, Linux, Windows"
                    )
                },
                trailingContent = {},
                //  colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    role = Role.Button
                ) {
                    //todo
                }.clip(RoundedCornerShape(12))
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = "Logged data"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Your stored data"
                    )
                },
                trailingContent = {},
                //  colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    role = Role.Button
                ) {
                    //todo
                }.clip(RoundedCornerShape(12))
            )
        }
    }
}
