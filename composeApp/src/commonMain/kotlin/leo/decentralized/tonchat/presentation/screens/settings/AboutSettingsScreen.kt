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
import leo.decentralized.tonchat.presentation.viewmodel.settings.AboutSettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AboutSettingsScreen(onBackClick: () -> Unit,vm : AboutSettingsViewModel = koinViewModel()) {
    DefaultScreen(
        screenName = "About",
        primaryButtonIcon = Icons.AutoMirrored.Default.ArrowBack,
        onPrimaryClick = {
            onBackClick()
        }) {
        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = "get Help"
                    )
                },
                supportingContent = {
                    Text(
                        text = "E-mail us the problem."
                    )
                },
                trailingContent = {},
                //  colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    role = Role.Button
                ) {
                    vm.getHelpFromEmail()
                }.clip(RoundedCornerShape(12))
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = "FAQ"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Read commonly asked  questions"
                    )
                },
                trailingContent = {},
                //  colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    role = Role.Button
                ) {
                    vm.openFAQUrl()
                }.clip(RoundedCornerShape(12))
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = "Github"
                    )
                },
                supportingContent = {
                    Text(
                        text = "See the source code of the application"
                    )
                },
                trailingContent = {},
                //  colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    role = Role.Button
                ) {
                    vm.openGithubUrl()
                }.clip(RoundedCornerShape(12))
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = "Support us"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Keep this project fueling"
                    )
                },
                trailingContent = {},
                //  colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    role = Role.Button
                ) {
                    vm.openSupportUsUrl()
                }.clip(RoundedCornerShape(12))
            )
        }
    }
}