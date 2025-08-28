package leo.decentralized.tonchat.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import leo.decentralized.tonchat.presentation.uiComponents.shimmerBackground
import leo.decentralized.tonchat.presentation.viewmodel.ChatViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun ChatScreen(
    navController: NavHostController,
    address: String,
    contactPublicAddress: String,
    vm: ChatViewModel = koinViewModel()
){
    LaunchedEffect(Unit){
        vm.getChats(contactAddress = address,contactPublicKey = contactPublicAddress)
    }

    val snackBarHost = SnackbarHostState()

    LaunchedEffect(vm.snackBarText.value) {
        if (vm.snackBarText.value.isNotEmpty()) {
            snackBarHost.showSnackbar(vm.snackBarText.value)
            vm.snackBarText.value = ""
        }
    }

    LoadingScreen(
        isLoading = false,
        supportingText = "",
        snackbarHostState = snackBarHost
    ) {
        DefaultScreen(screenName = "${address.take(5)}...${address.takeLast(3)}", onPrimaryClick = {
            navController.popBackStack()
        }, primaryButtonIcon = Icons.AutoMirrored.Sharp.ArrowBack, secondaryButton = {
            Icon(Icons.Default.MoreVert,"",    modifier = Modifier
                .padding(/*vertical = 8.dp*/)
                .clickable(
                    interactionSource = null,
                    indication = ripple(color = MaterialTheme.colorScheme.primary),
                    role = Role.Button
                ) {

                }
                .padding(vertical = 2.dp))

        }, postLazyContent = {
            Box(modifier = Modifier.fillMaxSize().padding(top = 4.dp)
                .navigationBarsPadding().imePadding()){
                val messageTextFieldHeight = rememberSaveable{
                    mutableStateOf(0)
                }
                val availableWidth = rememberSaveable{
                    mutableStateOf(0)
                }

                LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = with(LocalDensity.current){messageTextFieldHeight.value.toDp()}).padding(horizontal = 8.dp), reverseLayout = true){
                    if (vm.shimmer.value){
                        item {
                            ChatBubbleShimmer(
                                ChatMessage(
                                    message = "This is placeholder",
                                    isMine = true,
                                    isSending = false
                                ),
                                totalWidth = availableWidth.value,
                                hasChatAbove = false,
                                hasChatBelow = false
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            ChatBubbleShimmer(
                                ChatMessage(
                                    message = "Message",
                                    isMine = false,
                                    isSending = false
                                ),
                                totalWidth =  availableWidth.value,
                                hasChatAbove = false,
                                hasChatBelow = false
                            )
                        }

                    }
                    items(vm.chatList.value.size) { index ->
                        ChatBubble(
                            vm.chatList.value[index],
                            availableWidth.value,
                            index < vm.chatList.value.size - 1 && vm.chatList.value[index].isMine == vm.chatList.value[index + 1].isMine,
                            index > 0 && vm.chatList.value[index].isMine == vm.chatList.value[index - 1].isMine
                        )
                        if(index < vm.chatList.value.size - 1 && vm.chatList.value[index].isMine == vm.chatList.value[index + 1].isMine){
                            Spacer(modifier = Modifier.height(2.dp))
                        }else {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                val someText = remember {
                    mutableStateOf("")
                }

                TextField(
                    value = someText.value,
                    onValueChange = {someText.value = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 150.dp)
                        .align { size, shape, layoutDirection ->
                            Alignment.BottomCenter.align(size,shape,layoutDirection)
                        }.onGloballyPositioned {
                            messageTextFieldHeight.value = it.size.height
                            availableWidth.value = it.size.width
                        },
                    placeholder = {
                        Text("Type your message")
                    },
                    suffix = {
                            Icon(Icons.AutoMirrored.Default.Send,"",modifier = Modifier
                                .padding(start = 4.dp)
                                .clickable {
                                    if (someText.value.isNotBlank()) {
                                        vm.sendMessage(someText.value, address, contactPublicAddress)
                                        someText.value = ""
                                    }
                                })
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

            }
        })
    }
}

data class ChatMessage(val message: String, val isMine: Boolean, var isSending: Boolean? = false)

@Composable
fun ChatBubble(chatMessage: ChatMessage, totalWidth: Int, hasChatAbove: Boolean, hasChatBelow: Boolean){
    val singleSide = totalWidth > with(LocalDensity.current){600.dp.toPx()}
    val isMyMsg = chatMessage.isMine
    val otherMsgShape = RoundedCornerShape(
        topEnd = 16.dp,
        bottomEnd = 16.dp,
        topStart = if (hasChatAbove) {8.dp} else {16.dp},
        bottomStart = if (hasChatBelow) {8.dp} else {0.dp}
    )
    val myMsgShape =
        if (singleSide)
        {otherMsgShape
        }else {
        RoundedCornerShape(
            topEnd = if (hasChatAbove) {8.dp} else {16.dp} ,
            bottomEnd = if (hasChatBelow) {4.dp} else {0.dp},
            topStart = 16.dp,
            bottomStart = 16.dp)
        }

    val otherMsgColor = MaterialTheme.colorScheme.surfaceContainer
    val myMsgColor = MaterialTheme.colorScheme.primaryContainer

    Row(
        modifier = Modifier
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(
                    if (singleSide || !isMyMsg) {
                        Alignment.Start
                    } else {
                        Alignment.End
                    }
                ).widthIn(max = with(LocalDensity.current) { (totalWidth * 0.84f).toDp() })
                .background(
                    if (isMyMsg) {
                        myMsgColor
                    } else {
                        otherMsgColor
                    },
                    if (isMyMsg) {
                        myMsgShape
                    } else {
                        otherMsgShape
                    }
                )
        ) {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                SelectionContainer(
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = chatMessage.message,
                        modifier = Modifier.padding(start = 16.dp, top = 6.dp, bottom = 6.dp,end = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.W400
                    )
                }
                if (chatMessage.isSending == true) {
                    Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = "sending",
                            modifier = Modifier.size(20.dp).padding(end = 8.dp)
                    )
                }
            }
        }
    }

}



@Composable
fun ChatBubbleShimmer(chatMessage: ChatMessage, totalWidth: Int, hasChatAbove: Boolean, hasChatBelow: Boolean){
    val singleSide = totalWidth > with(LocalDensity.current){600.dp.toPx()}
    val isMyMsg = chatMessage.isMine
    val otherMsgShape = RoundedCornerShape(
        topEnd = 16.dp,
        bottomEnd = 16.dp,
        topStart = if (hasChatAbove) {8.dp} else {16.dp},
        bottomStart = if (hasChatBelow) {8.dp} else {0.dp}
    )
    val myMsgShape =
        if (singleSide)
        {otherMsgShape
        }else {
        RoundedCornerShape(
            topEnd = if (hasChatAbove) {8.dp} else {16.dp} ,
            bottomEnd = if (hasChatBelow) {4.dp} else {0.dp},
            topStart = 16.dp,
            bottomStart = 16.dp)
        }

    val otherMsgColor = MaterialTheme.colorScheme.surfaceContainer
    val myMsgColor = MaterialTheme.colorScheme.primaryContainer

    Box {
        Row(
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(
                        if (singleSide || !isMyMsg) {
                            Alignment.Start
                        } else {
                            Alignment.End
                        }
                    ).widthIn(max = with(LocalDensity.current) { (totalWidth * 0.84f).toDp() })
                    .background(
                        if (isMyMsg) {
                            myMsgColor
                        } else {
                            otherMsgColor
                        },
                        if (isMyMsg) {
                            myMsgShape
                        } else {
                            otherMsgShape
                        }
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    SelectionContainer(
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Text(
                            text = chatMessage.message,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 6.dp,
                                bottom = 6.dp,
                                end = 8.dp
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.W400
                        )
                    }
                    if (chatMessage.isSending == true) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = "sending",
                            modifier = Modifier.size(20.dp).padding(end = 8.dp)
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier.matchParentSize().shimmerBackground()
        )
    }

}