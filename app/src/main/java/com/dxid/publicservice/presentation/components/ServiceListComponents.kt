package com.dxid.publicservice.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dxid.publicservice.domain.model.Service
import com.dxid.publicservice.presentation.service_list.ServiceListState
import kotlinx.coroutines.launch


// Component Filter Category

@Composable
fun FilterChipRow(
    state: ServiceListState,
    onCategorySelected: (String?) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LazyRow(
        state = listState,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        itemsIndexed(state.categories) { index, category ->
            val selected = state.selectedCategory == category
            val backgroudColor by animateColorAsState(
                targetValue = if(selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface,
                label = "chipBackgroundAnimation"
            )
            FilterChip(
                selected = selected,
                onClick = {
                    val newSelected = if (selected) null else category
                    onCategorySelected(newSelected)

                    if(!selected) {
                        scope.launch {
                            listState.animateScrollToItem(index)
                        }
                    }
                },
                label = {
                    Text(
                    category, color = if (selected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface
                ) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = backgroudColor,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

//Component service card
@Composable
fun ServiceCard(
    service: Service,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Gambar penuh seluruh card
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(service.imageUrl)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient + teks di bagian bawah
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        )
                    )
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = service.description,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Rp ${service.price}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary)
                    )
                }
            }
        }
    }
}

// For component row in Detail Screen

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

//reuseable shimmer placeholder
@Composable
fun Modifier.shimmerPlaceholder(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "ShimmerAnimation")
    val shimmerX = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerXPosition"
    )
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.Gray.copy(alpha = 0.3f),
            Color.Gray.copy(alpha = 0.6f),
            Color.Gray.copy(alpha = 0.3f)
        ),
        start = Offset(x = shimmerX.value - 200f, y = 0f),
        end = Offset(x = shimmerX.value, y = 0f)
    )
    this.background(shimmerBrush)
}

// shimmer for service list screen
@Composable
fun ShimmerServiceListScreen() {
    Column(modifier = Modifier.padding(16.dp)) {

        // 🔍 Search Bar Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 15.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerPlaceholder()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🎯 Filter Chips Placeholder
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(4) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(50))
                        .shimmerPlaceholder()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📋 List of Service Cards Placeholder
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(5) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerPlaceholder()
                ) {}
            }
        }
    }
}