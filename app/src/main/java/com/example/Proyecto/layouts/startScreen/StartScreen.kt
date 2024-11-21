package com.example.Proyecto.layouts.startScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.Proyecto.R
import com.example.Proyecto.util.db.LDItemDb
import com.example.Proyecto.util.type.FolderItem
import com.example.Proyecto.util.type.FormItem
import com.example.Proyecto.util.type.LDItemType
import com.example.Proyecto.util.type.StartDropdownItem
import androidx.compose.material3.CircularProgressIndicator



private val ldItemDb = LDItemDb()

@Composable
fun StartScreenRoute(
    onBack: () -> Unit,
    onFolderClick: (Int) -> Unit,
    onFillFormClick: (Int) -> Unit,
    onEditFormClick: (Int?) -> Unit,
    viewModel: StartScreenViewModel = StartScreenViewModel()
) {
    val state by viewModel.startState.collectAsStateWithLifecycle()
    StartScreen(
        state = state,
        onBack = onBack,
        onFolderClick = onFolderClick,
        onFillFormClick = onFillFormClick,
        onEditFormClick = onEditFormClick,
        onRetry = { viewModel.retryLoading() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    state: StartScreenState,
    onBack: () -> Unit,
    onFolderClick: (Int) -> Unit,
    onFillFormClick: (Int) -> Unit,
    onEditFormClick: (Int?) -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Inicio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "backstack button"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Acción de crear nuevo formulario o carpeta */ },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        when {
            state.isLoading -> LoadingScreen()
            state.error != null -> ErrorScreen(
                message = state.error!!,
                onRetry = onRetry
            )
            else -> ContentScreen(
                folders = state.folders,
                forms = state.forms,
                onFolderClick = onFolderClick,
                onFillFormClick = onFillFormClick,
                onEditFormClick = onEditFormClick,
                paddingValues = paddingValues
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
fun ContentScreen(
    folders: List<FolderItem>,
    forms: List<FormItem>,
    onFolderClick: (Int) -> Unit,
    onFillFormClick: (Int) -> Unit,
    onEditFormClick: (Int?) -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp, top = 20.dp),
            text = "Tus Archivos",
            style = MaterialTheme.typography.headlineLarge
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyVerticalGrid(
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                folders.forEach { folder: FolderItem ->
                    item { ldFolder(onFolderClick = onFolderClick, id = folder.id, name = folder.title) }
                }
                forms.forEach { form: FormItem ->
                    item {
                        ldForms(
                            onFillFormClick = onFillFormClick,
                            onEditFormClick = onEditFormClick,
                            id = form.id,
                            name = form.question
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ldForms(onFillFormClick: (Int) -> Unit,
            onEditFormClick: (Int?) -> Unit,
            id: Int,
            name: String)
{
    val dropdownItems: List<StartDropdownItem> = listOf(
        StartDropdownItem("Editar"),
        StartDropdownItem("Llenar")
    )

    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

    var position by remember {
        mutableStateOf(Offset(0F, 0F))
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, )
    {
        Image(painter = painterResource(id = R.drawable.ldform), contentDescription = "Form",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(100.dp)
                .onGloballyPositioned { coordinates ->
                    position = coordinates.positionInWindow()
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        isContextMenuVisible = true
                        pressOffset = DpOffset(position.x.toDp(), position.y.toDp())
                        itemHeight = offset.y.toDp()
                    }
                }

        )
        Text(text = name, style = MaterialTheme.typography.labelLarge)
    }
    DropdownMenu(
        expanded = isContextMenuVisible,
        onDismissRequest = {
            isContextMenuVisible = false
        },
        offset = pressOffset.copy(
            y = pressOffset.y - itemHeight
        )
    ) {
        dropdownItems.forEach {
            DropdownMenuItem(onClick = {
                when(it.Option){
                    "Editar" -> onEditFormClick(id)
                    "Llenar" -> onFillFormClick(id)
                }
                isContextMenuVisible = false
            }, text = {Text(text = it.Option)})
        }
    }
}

@Composable
fun ldFolder(onFolderClick: (Int) -> Unit, id: Int, name: String) {
    Column(modifier = Modifier
        ,horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(painter = painterResource(id = R.drawable.ldfolder), contentDescription = "Folder",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(100.dp)
                .clickable { onFolderClick(id) }
        )
        Text(text = name, style = MaterialTheme.typography.labelLarge)
    }
}

