package leo.decentralized.tonchat.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import leo.decentralized.tonchat.presentation.navigation.Screens
import leo.decentralized.tonchat.presentation.screens.DefaultScreen
import leo.decentralized.tonchat.presentation.theme.TonDecentralizedChatTheme
import leo.decentralized.tonchat.presentation.viewmodel.settings.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


enum class ThemeOption {
    SYSTEM,
    LIGHT,
    DARK
}

private enum class ActiveSettingsScreen {
    MAIN,
    ACCOUNT,
    PRIVACY_SECURITY,
    INFORMATION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, vm : SettingsViewModel = koinViewModel()) {
    DefaultScreen(
        screenName = "Settings",
        primaryButtonIcon = Icons.AutoMirrored.Default.ArrowBack,
        onPrimaryClick = {
            navController.popBackStack()
        }) {


        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
        //Appearance
        item {
            var showThemeInfo by rememberSaveable { mutableStateOf(false) }
            var currentTheme by rememberSaveable { mutableStateOf(ThemeOption.SYSTEM) }
            ListItem(
                headlineContent = {
                    Text(
                        text = "Theme"
                    )
                },
                supportingContent = {
                    Text(
                        text = currentTheme.name.lowercase().replaceFirstChar { it.uppercase() }
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.WbSunny,
                        contentDescription = "Settings"
                    )

                },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null, // Using AlertDialog for selection, so no ripple needed here
                    role = Role.Button
                ) {
                    showThemeInfo = !showThemeInfo
                }.clip(RoundedCornerShape(12))
            )
            if (showThemeInfo) {
                Dialog(
                    onDismissRequest = { showThemeInfo = false },
                    content = {
                        Column(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    RoundedCornerShape(12)
                                )
                                .padding(12.dp)

                        ) {
                            ThemeDialogOption(
                                text = "System default",
                                isSelected = currentTheme == ThemeOption.SYSTEM,
                                onClick = {
                                    vm.changeTheme(1)
                                    currentTheme = ThemeOption.SYSTEM
                                    showThemeInfo = false
                                }
                            )
                            HorizontalDivider()
                            ThemeDialogOption(
                                text = "Light",
                                isSelected = currentTheme == ThemeOption.LIGHT,
                                onClick = {
                                    vm.changeTheme(2)
                                    currentTheme = ThemeOption.LIGHT
                                    showThemeInfo = false
                                }
                            )
                            HorizontalDivider()
                            ThemeDialogOption(
                                text = "Dark",
                                isSelected = currentTheme == ThemeOption.DARK,
                                onClick = {
                                    vm.changeTheme(0)
                                    currentTheme = ThemeOption.DARK
                                    showThemeInfo = false
                                }
                            )
                        }
                    }
                )

            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        //Account
        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = "Account"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Manage data, passcode, status"
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Account"
                    )

                },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    role = Role.Button
                ) {
                    navController.navigate(Screens.Account.screen)
                }.clip(RoundedCornerShape(12))
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        //Security and Privacy
        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = "Security & privacy"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Private key, token"
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.PrivacyTip,
                        contentDescription = "Account"
                    )

                },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    role = Role.Button
                ) {
                    navController.navigate(Screens.PrivacyNSecurity.screen)
                }.clip(RoundedCornerShape(12))
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        //Information
        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = "Info"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Device info, application"
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.QuestionMark,
                        contentDescription = "Info"
                    )

                },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    role = Role.Button
                ) {
                    navController.navigate(Screens.Information.screen)
                }.clip(RoundedCornerShape(12))
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        //About
        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = "About"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Developers, github, support"
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Account"
                    )

                },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    role = Role.Button
                ) {
                    navController.navigate(Screens.About.screen)
                }.clip(RoundedCornerShape(12))
            )
        }

    }
}



@Composable
fun ThemeDialogOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = null,
                indication = null, // Dialog items typically don't have individual ripples
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp), // Adjusted padding for dialog items
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            style = MaterialTheme.typography.titleSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }


}


@Preview
@Composable
fun SettingScPreview() {
    val navController: NavHostController = rememberNavController()
    TonDecentralizedChatTheme {
        SettingsScreen(navController)
    }

}




