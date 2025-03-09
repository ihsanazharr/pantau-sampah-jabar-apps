package com.example.opendatajabar.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.opendatajabar.data.local.DataEntity

@Composable
fun DataItemCard(
    item: DataEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)), // Outline
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onTertiary) // Ganti dari onTertiary jika terlalu terang
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
//            Text(
//                text = "Provinsi: ${item.namaProvinsi}",
//                style = MaterialTheme.typography.labelLarge,
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp
//            )
            Text(
                text = "${item.namaKabupatenKota}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(
                text = "\nJumlah Produksi Sampah:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(
                text = "${item.rataRataLamaSekolah} ${item.satuan}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
            )
            Text(
                text = "\nTahun:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(
                text = "${item.tahun}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onError),
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                    onClick = { onEditClick() }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically, // Sejajarkan vertikal
                        horizontalArrangement = Arrangement.Center // Pusatkan isi
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp) // Sesuaikan ukuran ikon
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Edit",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                    onClick = { onDeleteClick() }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically, // Sejajarkan vertikal
                        horizontalArrangement = Arrangement.Center // Pusatkan isi
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp) // Sesuaikan ukuran ikon
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Hapus",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
        }
    }
}
