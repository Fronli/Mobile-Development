package com.example.compose_test

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose_test.ui.theme.ComposetestTheme

// Data class mahasiswa dengan atribut nama, nim, ipk, dan jurusan
data class Mahasiswa(
    val nama: String,
    val nim: String,
    val ipk: Double,
    val jurusan: String = "Informatika"
)

// Daftar 22 data mahasiswa (10 awal + 12 mahasiswa tambahan)
val daftarMahasiswaLengkap = listOf(
    // 10 Mahasiswa Awal dari Starter
    Mahasiswa("Ali Rahman", "22001", 3.85, "Informatika"),
    Mahasiswa("Budi Santoso", "22002", 3.40, "Sistem Informasi"),
    Mahasiswa("Cici Wulandari", "22003", 3.92, "Informatika"),
    Mahasiswa("Dian Pratama", "22004", 2.95, "Teknik Elektro"),
    Mahasiswa("Eka Fitriani", "22005", 3.75, "Sistem Informasi"),
    Mahasiswa("Fandi Ahmad", "22006", 3.50, "Informatika"),
    Mahasiswa("Gita Permata", "22007", 3.88, "Informatika"),
    Mahasiswa("Hendra Kusuma", "22008", 2.80, "Teknik Elektro"),
    Mahasiswa("Indah Lestari", "22009", 3.65, "Sistem Informasi"),
    Mahasiswa("Joko Pratama", "22010", 3.20, "Informatika"),

    // 12 Mahasiswa Tambahan (Tugas Pengembangan #2)
    Mahasiswa("Kevin Wijaya", "22011", 3.90, "Informatika"),
    Mahasiswa("Lestari Ayu", "22012", 3.45, "Sistem Informasi"),
    Mahasiswa("Muhammad Rizky", "22013", 3.78, "Informatika"),
    Mahasiswa("Nadia Putri", "22014", 3.82, "Teknik Elektro"),
    Mahasiswa("Oscar Jonathan", "22015", 3.10, "Sistem Informasi"),
    Mahasiswa("Putri Anggraini", "22016", 3.95, "Informatika"),
    Mahasiswa("Qori Ramadhan", "22017", 2.88, "Teknik Elektro"),
    Mahasiswa("Rian Hidayat", "22018", 3.60, "Informatika"),
    Mahasiswa("Siti Nurhaliza", "22019", 3.70, "Sistem Informasi"),
    Mahasiswa("Taufik Ismail", "22020", 3.35, "Teknik Elektro"),
    Mahasiswa("Vina Amalia", "22021", 3.98, "Informatika"),
    Mahasiswa("Wahyu Saputra", "22022", 3.52, "Informatika")
)

@Composable
fun StudentListScreen(modifier: Modifier = Modifier) {
    var selectedCategory by remember { mutableStateOf("Semua") }

    // Kategori filter untuk tantangan LazyRow
    val kategoriList = listOf(
        "Semua",
        "IPK ≥ 3.5",
        "Informatika",
        "Sistem Informasi",
        "Teknik Elektro"
    )

    // Logika penyaringan data berdasarkan kategori yang dipilih
    val filteredMahasiswa = remember(selectedCategory) {
        when (selectedCategory) {
            "Semua" -> daftarMahasiswaLengkap
            "IPK ≥ 3.5" -> daftarMahasiswaLengkap.filter { it.ipk >= 3.5 }
            else -> daftarMahasiswaLengkap.filter { it.jurusan == selectedCategory }
        }
    }

    DaftarMahasiswa(
        mahasiswaList = filteredMahasiswa,
        totalMahasiswa = daftarMahasiswaLengkap.size,
        kategoriList = kategoriList,
        selectedCategory = selectedCategory,
        onSelectCategory = { selectedCategory = it },
        modifier = modifier
    )
}

@Composable
fun DaftarMahasiswa(
    mahasiswaList: List<Mahasiswa>,
    totalMahasiswa: Int,
    kategoriList: List<String>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // -------------------------------------------------------------
        // HEADER 1: Ringkasan Statistik (Tugas Pengembangan #3: Header item { })
        // -------------------------------------------------------------
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$totalMahasiswa",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Total Mahasiswa",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val avgIpk = daftarMahasiswaLengkap.map { it.ipk }.average()
                        Text(
                            text = "%.2f".format(avgIpk),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Rata-rata IPK",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val cumlaudeCount = daftarMahasiswaLengkap.count { it.ipk >= 3.5 }
                        Text(
                            text = "$cumlaudeCount",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = "IPK ≥ 3.5",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // HEADER 2: Tantangan LazyRow Kategori (Tugas Pengembangan #5)
        // -------------------------------------------------------------
        item {
            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Kategori & Program Studi",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(kategoriList) { kategori ->
                        val isSelected = (kategori == selectedCategory)
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectCategory(kategori) },
                            label = { Text(kategori) },
                            leadingIcon = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Terpilih",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // ITEMS: Daftar Mahasiswa dalam Card (Tugas Pengembangan #2)
        // -------------------------------------------------------------
        items(
            items = mahasiswaList,
            key = { it.nim }
        ) { mahasiswa ->
            MahasiswaCard(mahasiswa)
        }

        // -------------------------------------------------------------
        // FOOTER: Informasi Akhir Daftar (Tugas Pengembangan #3: Footer item { })
        // -------------------------------------------------------------
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Menampilkan ${mahasiswaList.size} dari $totalMahasiswa mahasiswa\nPraktikum 3 Jetpack Compose",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun MahasiswaCard(mahasiswa: Mahasiswa) {
    val isHighIpk = mahasiswa.ipk >= 3.5

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Avatar inisial nama mahasiswa
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (isHighIpk) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.secondaryContainer
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = mahasiswa.nama
                        .split(" ")
                        .take(2)
                        .mapNotNull { it.firstOrNull()?.toString() }
                        .joinToString("")
                    Text(
                        text = initials,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isHighIpk) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = mahasiswa.nama,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = mahasiswa.nim,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = mahasiswa.jurusan,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Badge IPK
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isHighIpk) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isHighIpk) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Cumlaude",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = "IPK %.2f".format(mahasiswa.ipk),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isHighIpk) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(name = "Student List Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun StudentListScreenLightPreview() {
    ComposetestTheme(darkTheme = false) {
        Surface {
            StudentListScreen()
        }
    }
}

@Preview(
    name = "Student List Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun StudentListScreenDarkPreview() {
    ComposetestTheme(darkTheme = true) {
        Surface {
            StudentListScreen()
        }
    }
}
