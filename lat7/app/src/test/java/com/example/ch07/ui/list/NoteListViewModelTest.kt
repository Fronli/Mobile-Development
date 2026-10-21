package com.example.ch07.ui.list

import com.example.ch07.data.NoteEntity
import com.example.ch07.testutil.FakeNoteRepository
import com.example.ch07.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

// Tes perilaku DASAR (sudah hijau sejak awal). Pakai argumen bernama saja,
// supaya tetap valid setelah kamu menambah kolom baru di NoteEntity.
// Saat mengerjakan Tugas 2 dan 4, tambahkan tes serupa untuk undo hapus dan
// filter tag (contohnya ada di praktikum/ch07).
@OptIn(ExperimentalCoroutinesApi::class)
class NoteListViewModelTest {

    @get:Rule val mainRule = MainDispatcherRule()

    private fun note(id: Int, title: String, content: String = "", pinned: Boolean = false) =
        NoteEntity(id = id, title = title, content = content, createdAt = id.toLong(), isPinned = pinned)

    // uiState memakai stateIn(WhileSubscribed): harus ada yang mengumpulkan
    private fun TestScope.start(vm: NoteListViewModel) {
        backgroundScope.launch { vm.uiState.collect { } }
        runCurrent()
    }

    @Test fun `catatan yang disematkan tampil paling atas`() = runTest {
        val vm = NoteListViewModel(FakeNoteRepository(listOf(note(1, "A"), note(2, "B", pinned = true))))
        start(vm)

        assertEquals(listOf("B", "A"), vm.uiState.value.notes.map { it.title })
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test fun `pencarian ditunda 300 ms lalu memfilter judul atau isi`() = runTest {
        val vm = NoteListViewModel(FakeNoteRepository(listOf(
            note(1, "Belanja", "susu"), note(2, "Kuliah", "bab Room"), note(3, "Ide", "aplikasi")
        )))
        start(vm)

        vm.onQueryChange("room")
        runCurrent()
        assertEquals("teks langsung tampil di TextField", "room", vm.uiState.value.query)
        advanceTimeBy(299); runCurrent()
        assertEquals("belum difilter sebelum 300 ms", 3, vm.uiState.value.notes.size)

        advanceTimeBy(2); runCurrent()
        assertEquals(listOf("Kuliah"), vm.uiState.value.notes.map { it.title })
        assertTrue(vm.uiState.value.isFiltering)

        vm.onQueryChange("")           // kosong = tanpa jeda, semua tampil lagi
        runCurrent()
        assertEquals(3, vm.uiState.value.notes.size)
    }

    @Test fun `hapus menghilangkan catatan dari daftar`() = runTest {
        val target = note(1, "A")
        val repo = FakeNoteRepository(listOf(target, note(2, "B")))
        val vm = NoteListViewModel(repo)
        start(vm)

        vm.deleteNote(target); runCurrent()

        assertEquals(listOf("B"), vm.uiState.value.notes.map { it.title })
    }

    @Test fun `togglePin menyematkan lalu melepas`() = runTest {
        val n = note(1, "A")
        val repo = FakeNoteRepository(listOf(n))
        val vm = NoteListViewModel(repo)
        start(vm)

        vm.togglePin(n); runCurrent()
        assertTrue(repo.current.single().isPinned)

        vm.togglePin(repo.current.single()); runCurrent()
        assertFalse(repo.current.single().isPinned)
    }

    @Test fun `hapus lalu batalkan mengembalikan catatan`() = runTest {
        val target = note(1, "A")
        val repo = FakeNoteRepository(listOf(target, note(2, "B")))
        val vm = NoteListViewModel(repo)
        start(vm)

        vm.deleteNote(target); runCurrent()
        assertEquals(listOf("B"), vm.uiState.value.notes.map { it.title })
        assertEquals(target, vm.uiState.value.recentlyDeleted)

        vm.undoDelete(); runCurrent()
        assertEquals(listOf("B", "A"), vm.uiState.value.notes.map { it.title })
        assertEquals(null, vm.uiState.value.recentlyDeleted)
    }

    @Test fun `onUndoDismissed mengosongkan recentlyDeleted`() = runTest {
        val target = note(1, "A")
        val repo = FakeNoteRepository(listOf(target))
        val vm = NoteListViewModel(repo)
        start(vm)

        vm.deleteNote(target); runCurrent()
        assertEquals(target, vm.uiState.value.recentlyDeleted)

        vm.onUndoDismissed(); runCurrent()
        assertEquals(null, vm.uiState.value.recentlyDeleted)
    }

    @Test fun `filter tag menyaring catatan dan klik lagi melepas filter`() = runTest {
        val n1 = NoteEntity(id = 1, title = "A", content = "", createdAt = 1L, tags = listOf("kuliah"))
        val n2 = NoteEntity(id = 2, title = "B", content = "", createdAt = 2L, tags = listOf("ide"))
        val repo = FakeNoteRepository(listOf(n1, n2))
        val vm = NoteListViewModel(repo)
        start(vm)

        assertEquals(listOf("ide", "kuliah"), vm.uiState.value.availableTags)

        vm.onTagSelected("kuliah"); runCurrent()
        assertEquals(listOf("A"), vm.uiState.value.notes.map { it.title })
        assertEquals("kuliah", vm.uiState.value.selectedTag)

        vm.onTagSelected("kuliah"); runCurrent()
        assertEquals(listOf("B", "A"), vm.uiState.value.notes.map { it.title })
        assertEquals(null, vm.uiState.value.selectedTag)
    }
}
