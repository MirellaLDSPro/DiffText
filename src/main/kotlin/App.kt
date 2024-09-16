import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.json.JSONException
import org.skyscreamer.jsonassert.JSONCompare
import org.skyscreamer.jsonassert.JSONCompareMode

@Composable
@Preview
fun App() {
    MaterialTheme {
        var result by remember { mutableStateOf("") }
        var json1 by remember { mutableStateOf("") }
        var json2 by remember { mutableStateOf("") }

        MaterialTheme {
            Column(
                Modifier.padding(16.dp)
            ) {
                Row {
                    OutlinedTextField(
                        value = json1,
                        onValueChange = { json1 = it },
                        label = { Text("JSON 1") }
                    )
                    OutlinedTextField(
                        value = json2,
                        onValueChange = { json2 = it },
                        label = { Text("JSON 2") }
                    )
                }
                Row {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                        result = compareJSON(json1, json2)
                    }) {
                        Text("Compare")
                    }
                }
                if (result.isNotBlank()) {
                    Row {
                        OutlinedTextField(
                            value = result,
                            readOnly = true,
                            onValueChange = {},
                            label = { Text("Result") }
                        )
                    }
                }
            }
        }
    }
}

fun compareJSON(json1: String, json2: String): String {
    return try {
        val comparison = JSONCompare.compareJSON(json1, json2, JSONCompareMode.STRICT)
        if (comparison.passed())
            "✅ Same JSON" else
            "❗ Differences :\n ${comparison.message}"
    } catch (e: JSONException) {
        "❌ Format Error"
    }
}