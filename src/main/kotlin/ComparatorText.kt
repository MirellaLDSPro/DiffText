import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch

@Composable
fun Comparator() {
    var text1 by remember { mutableStateOf(TextFieldValue()) }
    var text2 by remember { mutableStateOf(TextFieldValue()) }
    var diffOriginalAnnotatedString by remember { mutableStateOf(AnnotatedString("")) }
    var diffNewAnnotatedString by remember { mutableStateOf(AnnotatedString("")) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        BasicTextField(
            value = text1,
            onValueChange = { text1 = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        BasicTextField(
            value = text2,
            onValueChange = { text2 = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val (originalDiff, newDiff) = getDiffAnnotatedStrings(text1.text, text2.text)
            diffOriginalAnnotatedString = originalDiff
            diffNewAnnotatedString = newDiff
        }) {
            Text("Compare")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Original Text with Differences:")
        Text(diffOriginalAnnotatedString)
        Spacer(modifier = Modifier.height(16.dp))
        Text("New Text with Differences:")
        Text(diffNewAnnotatedString)
    }
}

private fun getDiffAnnotatedStrings(text1: String, text2: String): Pair<AnnotatedString, AnnotatedString> {
    val dmp = DiffMatchPatch()
    val diffs = dmp.diffMain(text1, text2)
    dmp.diffCleanupSemantic(diffs)

    val originalText = buildAnnotatedString {
        for (diff in diffs) {
            val color = when (diff.operation) {
                DiffMatchPatch.Operation.DELETE -> Color.Red
                else -> Color.Black
            }
            withStyle(style = androidx.compose.ui.text.SpanStyle(color = color)) {
                if (diff.operation != DiffMatchPatch.Operation.INSERT) {
                    append(diff.text)
                }
            }
        }
    }

    val newText = buildAnnotatedString {
        for (diff in diffs) {
            val color = when (diff.operation) {
                DiffMatchPatch.Operation.INSERT -> Color.Green
                else -> Color.Black
            }
            withStyle(style = androidx.compose.ui.text.SpanStyle(color = color)) {
                if (diff.operation != DiffMatchPatch.Operation.DELETE) {
                    append(diff.text)
                }
            }
        }
    }

    return Pair(originalText, newText)
}