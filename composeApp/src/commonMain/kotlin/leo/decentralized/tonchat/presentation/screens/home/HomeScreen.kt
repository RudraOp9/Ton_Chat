package leo.decentralized.tonchat.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.coroutineScope
import leo.decentralized.tonchat.presentation.navigation.Screen
import leo.decentralized.tonchat.presentation.navigation.Screens
import leo.decentralized.tonchat.presentation.screens.DefaultScreen
import leo.decentralized.tonchat.presentation.screens.LoadingScreen
import leo.decentralized.tonchat.presentation.uiComponents.shimmerBackground
import leo.decentralized.tonchat.presentation.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(navController: NavController,vm: HomeViewModel = koinViewModel()) {

    val snackBarHost = remember{
        SnackbarHostState()
    }

    var crrPage by rememberSaveable{
        mutableStateOf(0)
    }

    val pagerState = rememberPagerState { 2 }

    LaunchedEffect(pagerState.currentPage){
        crrPage = pagerState.currentPage
    }

    LaunchedEffect(Unit){
        vm.checkPassword(navController)
    }

    LaunchedEffect(vm.snackBarText.value) {
        if (vm.snackBarText.value.isNotEmpty()) {
            snackBarHost.showSnackbar(vm.snackBarText.value)
            vm.snackBarText.value = ""
        }
    }

    LoadingScreen(
        isLoading = vm.isLoading.value,
        supportingText = vm.isLoadingText.value,
        snackbarHostState = snackBarHost
    ) {
        DefaultScreen(
            modifier = Modifier.fillMaxSize(),
            screenName = "Decentralized ton chat",
            onPrimaryClick = {},
            primaryButtonIcon = Icons.Sharp.Search,
            secondaryButton = {
                Text(
                    text = "Settings",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(/*vertical = 8.dp,*/ horizontal = 4.dp)
                        .clickable(
                            interactionSource = null,
                            indication = ripple(color = MaterialTheme.colorScheme.primary),
                            role = Role.Button
                        ) {
                            navController.navigate(Screens.Settings.screen)
                        }
                        .padding(vertical = 2.dp, horizontal = 8.dp)
                )
            }, postLazyContent = {
                Box(modifier = Modifier.fillMaxSize()) {
                    HorizontalPager(
                        modifier = Modifier.padding(top = 4.dp)
                            .navigationBarsPadding(),
                        state = pagerState,
                        beyondViewportPageCount = 1,
                        userScrollEnabled = true,
                        //key = {it},
                        pageContent = { page ->
                            LazyColumn(
                                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerLow)
                            ) {
                                if (vm.isContactsLoading.value) {
                                    items(4) {
                                        ChatItemShimmer()
                                    }
                                }
                                if (!vm.isContactsLoading.value && if (page==0) vm.contacts.value.isEmpty() else vm.spamContact.value.isEmpty()) {
                                    item {

                                        Column(
                                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            if (page == 0) {
                                                Text(
                                                    text = "No contacts yet",
                                                    style = MaterialTheme.typography.headlineSmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Spacer(modifier = Modifier.padding(8.dp))
                                                Text(
                                                    text = "Click the button below to add a new contact.",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    textAlign = TextAlign.Center
                                                )
                                                Spacer(modifier = Modifier.padding(16.dp))
                                                val infiniteTransition =
                                                    rememberInfiniteTransition(label = "arrowTransition")
                                                val arrowOffsetY = infiniteTransition.animateFloat(
                                                    initialValue = 0f,
                                                    targetValue = 20f,
                                                    animationSpec = infiniteRepeatable(
                                                        animation = tween(1000),
                                                        repeatMode = RepeatMode.Reverse
                                                    ), label = "arrowOffsetY"
                                                )
                                                Icon(
                                                    Icons.Default.ArrowDownward,
                                                    contentDescription = "Add new contact hint",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier
                                                        .rotate(-20f)
                                                        .size(48.dp)
                                                        .graphicsLayer {
                                                            translationY = arrowOffsetY.value
                                                        }
                                                )
                                            }else{
                                                Text(
                                                    text = "Spam is empty",
                                                    style = MaterialTheme.typography.headlineSmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Spacer(modifier = Modifier.padding(8.dp))
                                                Text(
                                                    text = "This will be updated once someone tries to contact you.",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                }

                                items(if (page==0)vm.contacts.value else vm.spamContact.value) {
                                    ChatItem(
                                        it.address,
                                        ChatStatus.UNKNOWN,
                                        unreadCount = 0
                                    ) { //TODO ?
                                        navController.navigate(Screen.ChatScreen(it.address,it.publicKey))
                                    }
                                }
                            }
                        }
                    )

                    TabRow(
                        selectedTabIndex = crrPage,
                        modifier = Modifier.align { s, i, l ->
                                Alignment.BottomEnd.align(s, i, l)
                            }.navigationBarsPadding(),
                        indicator = {}
                    ) {
                        Tab(
                            selected = crrPage == 0,
                            onClick = {
                                pagerState.requestScrollToPage(0)
                            },
                            text = { Text("Chats") },
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Tab(
                            selected = crrPage == 1,
                            onClick = {
                                pagerState.requestScrollToPage(1)
                            },
                            text = { Text("Spam") },
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    SmallFloatingActionButton(
                        onClick = {
                            vm.newContactScreen.value = true
                        },
                        modifier = Modifier.align { s, i, l ->
                            Alignment.BottomEnd.align(s, i, l)
                        }.padding(end = 8.dp).navigationBarsPadding(),
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.Add, "New contact")
                    }
                }

            },
            horizontalPadding = 0.dp
        )
        AnimatedVisibility(vm.newContactScreen.value){
            NewContactSearchScreen({
                vm.newContactScreen.value = false
            },{
                vm.searchAndAddNewContact(it)
            })
        }
    }
}

@Composable
fun ChatItem(
    address: String,
    status: ChatStatus,
    unreadCount: Int,
    onClick: ()->Unit
) {
    val width = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
    Column(
        modifier = Modifier
            .widthIn(max = if (width > 800.dp) 400.dp else Dp.Unspecified)
            .fillMaxWidth()
            .clickable(onClick = onClick)

    ) {
        Row(
            modifier = Modifier .background(MaterialTheme.colorScheme.surface) .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth(if (unreadCount>0){0.9f} else { 1f})) {
                Text(
                    text = address,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.MiddleEllipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1
                )
                Spacer(Modifier.padding(top = 4.dp))
                val annotedString = remember { //todo, will you ever fix typos :angry_emoji
                    AnnotatedString.Builder().apply {
                        append("status : ")
                        when(status){
                            ChatStatus.SECURE -> {
                                withStyle(style = SpanStyle(color = Color.Green.copy(alpha = 0.8f))) {
                                    this.append(status.name.lowercase())
                            }}
                            ChatStatus.UNSECURE -> {
                                withStyle(style = SpanStyle(color = Color.Red.copy(alpha = 0.8f))) {
                                    this.append(status.name.lowercase())
                                }}
                            ChatStatus.COMPROMISED -> {
                                withStyle(style = SpanStyle(color = Color.Red.copy(alpha = 0.8f))) { // Orange color
                                    this.append(status.name.lowercase())
                                }}
                            ChatStatus.VULNERABLE -> {
                                withStyle(style = SpanStyle(color = Color(0xFFFFA500).copy(alpha = 0.8f))) {
                                    this.append(status.name.lowercase())
                                }}
                            ChatStatus.UNKNOWN -> {
                                withStyle(style = SpanStyle(color = Color.Gray.copy(alpha = 0.8f))) {
                                    this.append(status.name.lowercase())
                                }}
                        }

                        // todo if(secure) else if(breached) else if(unprotected)
                    }.toAnnotatedString()
                }
                Text(
                    annotedString,
                    style = MaterialTheme.typography.labelMedium,
                    overflow = TextOverflow.MiddleEllipsis,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    maxLines = 1,
                )
            }
            Spacer(modifier = Modifier.weight(1f).padding(start = 4.dp))
            if (unreadCount > 0) {
                Column(
                    modifier = Modifier.size(24.dp).background(
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        CircleShape
                    ).fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        unreadCount.toString(),
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(top = 2.dp))
    }
}


@Composable
fun ChatItemShimmer() {
    val width = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
    Column(
        modifier = Modifier
            .widthIn(max = if (width > 800.dp) 400.dp else Dp.Unspecified)
            .fillMaxWidth()
            .clickable(onClick = {})

    ) {
        Row(
            modifier = Modifier .background(MaterialTheme.colorScheme.surface) .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth(1f)) {
                Box{
                    Text(
                        text = "EQBg3rlccFWUaG1fHt_WLhFxsVvUn_136dIznbQfVm8k79dh",
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.MiddleEllipsis,
                           color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1
                    )
                    Box(
                        modifier = Modifier.matchParentSize().shimmerBackground()
                    )
                }
                Spacer(Modifier.padding(top = 4.dp))
                val annotedString = remember {
                    AnnotatedString.Builder().apply {
                        append("status : ")
                        withStyle(style = SpanStyle(color = Color.Green.copy(alpha = 0.8f))) {
                            this.append("Secure")
                        }
                    }.toAnnotatedString()
                }
                Box {
                    Text(
                        annotedString,
                        style = MaterialTheme.typography.labelMedium,
                        overflow = TextOverflow.MiddleEllipsis,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        maxLines = 1,
                    )
                    Box(
                        modifier = Modifier.matchParentSize().shimmerBackground()
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f).padding(start = 4.dp))
        }
        Spacer(modifier = Modifier.padding(top = 2.dp))
    }
}

enum class ChatStatus {
    SECURE, UNSECURE, COMPROMISED, VULNERABLE, UNKNOWN
}


//todo checking the secure status of the user using password/pin : server sends an iv to user which will be encrypted using AES with the pin, and encrypted message will be sent to server, on each instance ( app open ) user has to encrypt the same iv using pass and send to server to allow sending the data present in cloud, in case of wrong three pin account will be marked unsecure.
