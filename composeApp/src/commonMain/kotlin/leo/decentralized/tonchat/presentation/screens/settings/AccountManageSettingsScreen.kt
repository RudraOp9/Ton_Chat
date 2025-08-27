package leo.decentralized.tonchat.presentation.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import leo.decentralized.tonchat.presentation.screens.DefaultScreen

@Composable
fun AccountManageSettingsScreen(onBackClick: () -> Unit) {
    DefaultScreen(
        screenName = "Account",
        primaryButtonIcon = Icons.AutoMirrored.Default.ArrowBack,
        onPrimaryClick = {
            onBackClick()
        }) {
        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = "Wipe my chats"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Clear all chats from server and device"
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
                        text = "Change passcode"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Logout to change passcode"
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
                        text = "Logout"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Remove all data from device"
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
                        text = "Delete Account",
                        color = MaterialTheme.colorScheme.error
                    )
                },
                supportingContent = {
                    Text(
                        text = "Delete account permanently"
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
