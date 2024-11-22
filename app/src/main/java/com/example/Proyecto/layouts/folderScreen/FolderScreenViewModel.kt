import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Proyecto.layouts.startScreen.StartRepositoryImpl
import com.example.Proyecto.util.type.FolderItem
import com.example.Proyecto.util.type.FormDisplayItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FormDisplayViewModel : ViewModel() {
    private val startRepository = StartRepositoryImpl()

    private val _formItems = MutableStateFlow<List<FormDisplayItem>>(emptyList())
    val formItems: StateFlow<List<FormDisplayItem>> = _formItems

    private val _selectedItems = MutableStateFlow<Set<String>>(emptySet())
    val selectedItems: StateFlow<Set<String>> = _selectedItems

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _showNoResultsMessage = MutableStateFlow(false)
    val showNoResultsMessage: StateFlow<Boolean> = _showNoResultsMessage

    private val _folderTitle = MutableStateFlow("Forms")
    val folderTitle: StateFlow<String> = _folderTitle

    init {
        loadFormItems()
    }

    private fun loadFormItems() {
        viewModelScope.launch {
            // Assuming startRepository.getForms() returns List<FormDisplayItem>
            _formItems.value = startRepository.getForms()
        }
    }

    fun toggleItemSelection(id: String, isSelected: Boolean) {
        _selectedItems.value = if (isSelected) {
            _selectedItems.value + id
        } else {
            _selectedItems.value - id
        }
    }

    fun deleteSelectedItems() {
        _formItems.value = _formItems.value.filterNot { _selectedItems.value.contains(it.id) }
        _selectedItems.value = emptySet()
    }

    fun sortItemsByName() {
        _formItems.value = _formItems.value.sortedBy { it.title }
    }

    fun searchItems(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            val results = startRepository.getForms().filter {
                it.title.contains(query, ignoreCase = true)
            }
            _formItems.value = results
            _showNoResultsMessage.value = results.isEmpty()
        }
    }

    fun updateItemTitle(id: String, newTitle: String) {
        _formItems.value = _formItems.value.map {
            if (it.id == id) it.copy(title = newTitle) else it
        }
    }

    fun deleteItem(id: String) {
        _formItems.value = _formItems.value.filterNot { it.id == id }
    }

    fun setFolderTitle(title: String) {
        _folderTitle.value = title
    }
}