import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Proyecto.layouts.startScreen.StartRepositoryImpl
import com.example.Proyecto.util.type.FolderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FolderScreenViewModel : ViewModel() {
    private val startRepository = StartRepositoryImpl()
    private val _folderItems = MutableStateFlow<List<FolderItem>>(emptyList())
    val folderItems: StateFlow<List<FolderItem>> = _folderItems

    private val _selectedItems = MutableStateFlow<Set<String>>(emptySet())
    val selectedItems: StateFlow<Set<String>> = _selectedItems

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _showNoResultsMessage = MutableStateFlow(false)
    val showNoResultsMessage: StateFlow<Boolean> = _showNoResultsMessage

    init {
        loadFolderItems()
    }

    private fun loadFolderItems() {
        viewModelScope.launch {
            _folderItems.value = startRepository.getFolders()
        }
    }

    fun toggleSelectAll(selectAll: Boolean) {
        _selectedItems.value = if (selectAll) {
            _folderItems.value.map { it.id }.toSet()
        } else {
            emptySet()
        }
    }

    fun deleteSelectedItems() {
        _folderItems.value = _folderItems.value.filterNot { _selectedItems.value.contains(it.id) }
        _selectedItems.value = emptySet()
    }

    fun sortItemsByName() {
        _folderItems.value = _folderItems.value.sortedBy { it.title }
    }

    fun sortItemsByDate() {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        _folderItems.value = _folderItems.value.sortedBy {
            LocalDate.parse(it.date, formatter)
        }
    }

     fun searchItems(query: String) {
        _searchQuery.value = query
         viewModelScope.launch {
             val results = startRepository.getFolders().filter { it.title.contains(query, ignoreCase = true) }
             _folderItems.value = results
             _showNoResultsMessage.value = results.isEmpty()
         }
    }

    fun updateItemTitle(id: String, newTitle: String) {
        _folderItems.value = _folderItems.value.map {
            if (it.id == id) it.copy(title = newTitle) else it
        }
    }

    fun deleteItem(id: String) {
        _folderItems.value = _folderItems.value.filterNot { it.id == id }
    }
}