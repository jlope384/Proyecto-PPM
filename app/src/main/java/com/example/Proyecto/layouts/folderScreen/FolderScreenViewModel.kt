import androidx.lifecycle.ViewModel
import com.example.Proyecto.util.db.FolderItemDb
import com.example.Proyecto.util.type.FolderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FolderScreenViewModel : ViewModel() {
    private val _folderItems = MutableStateFlow<List<FolderItem>>(emptyList())
    val folderItems: StateFlow<List<FolderItem>> = _folderItems

    private val _selectedItems = MutableStateFlow<Set<Int>>(emptySet())
    val selectedItems: StateFlow<Set<Int>> = _selectedItems

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _showNoResultsMessage = MutableStateFlow(false)
    val showNoResultsMessage: StateFlow<Boolean> = _showNoResultsMessage

    private val folderDb = FolderItemDb()

    init {
        loadFolderItems()
    }

    private fun loadFolderItems() {
        _folderItems.value = folderDb.generateRandomFolderItems(10)
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
        val results = folderDb.generateRandomFolderItems(10).filter { it.title.contains(query, ignoreCase = true) }
        _folderItems.value = results
        _showNoResultsMessage.value = results.isEmpty()
    }

    fun updateItemTitle(id: Int, newTitle: String) {
        _folderItems.value = _folderItems.value.map {
            if (it.id == id) it.copy(title = newTitle) else it
        }
    }

    fun deleteItem(id: Int) {
        _folderItems.value = _folderItems.value.filterNot { it.id == id }
    }
}