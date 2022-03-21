package com.izamha.shakisha.ui.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.izamha.shakisha.model.Filter
import com.izamha.shakisha.model.FoundItemCollection
import com.izamha.shakisha.model.FoundItemRepo
import com.izamha.shakisha.ui.components.FilterBar
import com.izamha.shakisha.ui.components.ShakishaDivider
import com.izamha.shakisha.ui.components.ShakishaSurface
import com.izamha.shakisha.ui.components.FoundItemCollection
import com.izamha.shakisha.ui.theme.ShakishaTheme
import com.google.accompanist.insets.statusBarsHeight

@Composable
fun Feed(
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val foundItemCollections = remember { FoundItemRepo.getSnacks() }
    val filters = remember { FoundItemRepo.getFilters() }
    Feed(
        foundItemCollections,
        filters,
        onItemClick,
        modifier
    )
}

@Composable
private fun Feed(
    foundItemCollections: List<FoundItemCollection>,
    filters: List<Filter>,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    ShakishaSurface(modifier = modifier.fillMaxSize()) {
        Box {
            ShakishaCollectionList(foundItemCollections, filters, onItemClick)
            // DestinationBar()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ShakishaCollectionList(
    foundItemCollections: List<FoundItemCollection>,
    filters: List<Filter>,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var filtersVisible by rememberSaveable { mutableStateOf(false) }
    Box(modifier) {
        LazyColumn {

            item {
                Spacer(Modifier.statusBarsHeight(additional = 15.dp))
                FilterBar(filters, onShowFilters = { filtersVisible = true })
            }
            itemsIndexed(foundItemCollections) { index, foundItemCollection ->
                if (index > 0) {
                    ShakishaDivider(thickness = 2.dp)
                }

                FoundItemCollection(
                    foundItemCollection = foundItemCollection,
                    onFoundItemClick = onItemClick,
                    index = index
                )
            }
        }
    }
    AnimatedVisibility(
        visible = filtersVisible,
        enter = slideInVertically() + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        FilterScreen(
            onDismiss = { filtersVisible = false }
        )
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun HomePreview() {
    ShakishaTheme {
        Feed(onItemClick = { })
    }
}
