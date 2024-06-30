// Fred Wong; 301199984

package com.fred.wong

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Second Activity; since my name, Fred, starts with a letter from A-N inclusive, use Checkboxes instead of RadioButtons

class WongActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wong)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Display a back button in ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set the title of the ActionBar for this activity to be Brain Challenge
        supportActionBar?.title = getString(R.string.actionBar_second_title_truncated)

        val imageZigZagLine: ImageView = findViewById(R.id.imageViewZigZagLine)
        val checkBoxExercise1: CheckBox = findViewById(R.id.checkBoxExercise1)
        val checkBoxExercise2: CheckBox = findViewById(R.id.checkBoxExercise2)
        val checkBoxExercise3: CheckBox = findViewById(R.id.checkBoxExercise3)
        val checkBoxExercise4: CheckBox = findViewById(R.id.checkBoxExercise4)
        val buttonDisplayExercises: Button = findViewById(R.id.buttonDisplayExercises)

        // Initialize a String array to store Toast message parts
        val checkedExercises: ArrayList<String> = ArrayList()

        // Set listeners for the checkboxes to add or remove the exercise name to the String array
        checkBoxExercise1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedExercises.add(resources.getString(R.string.text_exercise_1))
            } else {
                checkedExercises.remove(resources.getString(R.string.text_exercise_1))
            }
        }
        checkBoxExercise2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedExercises.add(resources.getString(R.string.text_exercise_2))
            } else {
                checkedExercises.remove(resources.getString(R.string.text_exercise_2))
            }
        }
        checkBoxExercise3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedExercises.add(resources.getString(R.string.text_exercise_3))
            } else {
                checkedExercises.remove(resources.getString(R.string.text_exercise_3))
            }
        }
        checkBoxExercise4.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedExercises.add(resources.getString(R.string.text_exercise_4))
            } else {
                checkedExercises.remove(resources.getString(R.string.text_exercise_4))
            }
        }

        // Set listener on button to build string from checkedExercises String array and inflate a custom multiline Toast to display the string
        buttonDisplayExercises.setOnClickListener {
            // Use a string builder to concatenate and format the output string
            val stringBuilder: StringBuilder = StringBuilder()
            for ((index, string) in checkedExercises.withIndex()) {
                stringBuilder.append(string)
                if (index != checkedExercises.lastIndex) {  // Check so it doesn't add a newline after the final string
                    stringBuilder.append("\n")
                }
            }
            val formattedString: String = stringBuilder.toString()

            // Inflate a custom Toast
            val inflater = layoutInflater
            val customLayout: View = inflater.inflate(R.layout.multiline_toast, findViewById(R.id.toastLayout))

            // Retrieve the custom Toast's textView
            val textViewToast: TextView = customLayout.findViewById(R.id.toast_text)
            // Handle if the string is empty by setting a default message, otherwise, set to formatted string
            textViewToast.text = formattedString.ifEmpty { getString(R.string.text_no_exercises_selected) }

            val toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
            // Set the Toast to use the custom layout, replacing the makeTest's set text in the process
            toast.view = customLayout  // Android API deprecated custom Toast view setter, but test requirements ask for a Toast. Custom is the only option for multiline message
            toast.show()

            // Toast alternative using modern SnackBar
            /*
            val snackBar = Snackbar.make(
                findViewById(android.R.id.content),
                formattedString.ifEmpty { getString(R.string.text_no_exercises_selected) },
                Snackbar.LENGTH_LONG
            )
            val snackBarView: View = snackBar.view
            val textViewSnackBar: TextView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text)
            textViewSnackBar.maxLines = 5
            snackBar.show()
            */
        }

        // Initialize variables to store required y-coordinates string array, and individual x- and y-values
        val yCoordinates = resources.getStringArray(R.array.y_coordinates)
        val outXValue = TypedValue()
        resources.getValue(R.dimen.image_zigzag_x_starting_coordinate, outXValue, true)
        var xValue: Float = outXValue.float
        var y: Float

        // Instantiate a paint object to hold info about geometries, text, and bitmaps
        val paint = Paint().apply {
            color = Color.WHITE
            val outValue = TypedValue()
            resources.getValue(R.dimen.image_zigzag_stroke_width, outValue, true)
            strokeWidth = outValue.float
        }

        // Set width based on size of y-coordinates array and height based on largest y-value
        val bitmapWidth = yCoordinates.size * resources.getInteger(R.integer.image_zigzag_x_increment)
        val bitmapHeight = yCoordinates.max().toInt() * 2  // Setting height to be 2X of largest y-displacement

        // Create a bitmap with the defined height and width
        val bitmap: Bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
        // Construct a canvas using the bitmap
        val canvas = Canvas(bitmap)

        // Loop through each y-coordinate of the array;
        // draw upwards (y-displacement) and then downwards (y+displacement) using drawLine;
        // x-interval is 100 pixels per point
        for (i in yCoordinates.indices) {
            y = yCoordinates[i].toFloat()
            if (i % 2 == 0) {
                // Draw angled upwards
                canvas.drawLine(
                    xValue,
                    y,
                    xValue + resources.getInteger(R.integer.image_zigzag_x_increment),
                    y - resources.getInteger(R.integer.image_zigzag_y_displacement),
                    paint
                )
                xValue += resources.getInteger(R.integer.image_zigzag_x_increment)
            } else {
                // Draw angled downwards
                canvas.drawLine(
                    xValue,
                    y,
                    xValue + resources.getInteger(R.integer.image_zigzag_x_increment),
                    y + resources.getInteger(R.integer.image_zigzag_y_displacement),
                    paint
                )
                xValue += resources.getInteger(R.integer.image_zigzag_x_increment)
            }
        }
        // Set the drawn bitmap as the content of the ImageView
        imageZigZagLine.setImageBitmap(bitmap)

    }

    // Inflating a menu/ActionBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.overall_menu, menu)
        return true
    }

    // Handling menu/ActionBar items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}