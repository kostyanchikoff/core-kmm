package com.kostyanchikoff.core.android.core.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.kostyanchikoff.core.presentation.viewModel.CoreContract
import com.kostyanchikoff.core.presentation.viewModel.CoreViewModel
import com.kostyanchikoff.core.presentation.viewModel.CoreViewSideEffect
import com.kostyanchikoff.core.presentation.viewModel.CoreViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Слушатель работы viewModel(MVI)
 * [viewModel] - viewModel
 * [effectBuilder] - билдер UI для работы с эффектами
 * [primaryErrorBuilder] - билдер UI при первичной ошибке сервера
 * [stateBuilder] -билдер UI для работы со стейтами
 * [retryHttpRequestBloc] - в случае ошибки повторно выполнить запрос
 * [scaffoldState] - состояние Scaffold
 */
@Composable
fun CoreViewModelConsumer(
    viewModel: CoreViewModel,
    effectBuilder: ((CoreViewSideEffect) -> Unit)? = null,
    primaryErrorBuilder: @Composable ((CoreViewState) -> Unit)? = null,
    stateBuilder: @Composable (CoreViewState) -> Unit,
    retryHttpRequestBloc: (() -> Unit)? = null,
    scaffoldState: ScaffoldState
) {
    LaunchedEffect(LocalContext.current) {
        launch {
            viewModel.effect.collect {
                if (it is CoreContract.CoreErrorEffect.ShowErrorMessage) {
                    scaffoldState.snackbarHostState.showSnackbar(it.errorMessage)
                }
                effectBuilder?.invoke(it)
            }
        }
    }
    val state = viewModel.viewState.collectAsState().value
    Box(content = {
        if (state is CoreContract.CoreErrorState) {
            primaryErrorBuilder?.invoke(state)
                ?: TextButton(onClick = { retryHttpRequestBloc?.invoke() }) {
                    Text("Повторить запрос")
                }
        }
        stateBuilder.invoke(state)

    })
}
