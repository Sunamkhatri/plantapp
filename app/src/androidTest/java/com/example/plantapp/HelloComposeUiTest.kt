import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test

class HelloComposeUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHelloWorldDisplayed() {
        composeTestRule.setContent {
            // Your composable function here
        }

        onNodeWithText("Hello World").assertIsDisplayed()
    }
}