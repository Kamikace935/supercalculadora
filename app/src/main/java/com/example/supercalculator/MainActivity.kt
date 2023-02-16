package com.example.supercalculator

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

import android.view.View

import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    /*Con apply plugin: 'kotlin-android-extension'
    * se referenciaran directamente los TextView
    * sin que sea necesario inicializarlos*/

    private lateinit var tvResult: TextView
    private lateinit var tvShow: TextView
    private var addOperation: Boolean = false
    private var addDecimal: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvShow = findViewById(R.id.tvShow)
        tvResult = findViewById(R.id.tvResult)
    }

    /*Es necesario que compruebe que es un boton para
    * poder realizar un .text a la vista que recoge el
    * metodo al realizar una pulsacion*/

    fun numberAction(view: View) {
        /*Recoge el texto del boton y se los añade
        * al TextView por el final*/
        if (view is Button) {
            /*Si el texto recogido es una coma comprueba
            * que no ha sido añadida antes con un buleano*/
            if (view.text == ".") {
                if (tvShow.text.isNotEmpty()) {
                    val c = tvShow.text.last()

                    if (addDecimal && c.isDigit()) {
                        tvShow.append(view.text)
                        addDecimal = false
                    }
                }
            }else {
                tvShow.append(view.text)
                addOperation = true
            }
                /*Si se ha añadido un número permite añadir un operador*/
        }
    }

    fun operationAction(view: View) {
        if (view is Button && addOperation) {
            tvShow.append(view.text)
            addOperation = false
            addDecimal = true
        }
    }

    fun allClearAction(view: View) {
        tvShow.text = ""
        tvResult.text = ""
        addDecimal = true
    }

    fun eraseAction(view: View) {
        view.setOnClickListener{
            if (tvShow.length() > 0) {
                if (tvShow.text.last() == '.') {
                    addDecimal = true
                }
                tvShow.text = tvShow.text.dropLast(1)
            }
        }

        view.setOnLongClickListener{
            tvShow.text = ""
            addDecimal = true
            true
        }
    }

    fun equalsAction(view: View) {
       tvResult.text = result()
    }

    private fun result(): String {
        val operations = sortOperations()
        if (operations.isEmpty())
            return ""

        val calculations = givePriority(operations)
        if (calculations.isEmpty())
            return ""

        val result = finalCalculations(calculations)

        return result.toString()
    }

    private fun sortOperations(): ArrayList<Any> {
        val operations = ArrayList<Any>()
        var currentDigit = ""

        for (c in tvShow.text) {
            if (c.isDigit() || c == '.') {
                currentDigit += c
            }else {
                operations.add(currentDigit.toDouble())
                currentDigit = ""
                operations.add(c)
            }
        }

        if (currentDigit != "") {
            operations.add(currentDigit.toDouble())
        }

        return operations
    }

    private fun givePriority(operations: ArrayList<Any>): ArrayList<Any> {
        var calculations = operations

        while (calculations.contains('/') || calculations.contains('x')) {
            calculations = priorityCalculations(calculations)
        }

        return calculations
    }

    private fun priorityCalculations(operations: ArrayList<Any>): ArrayList<Any> {
        val calculations = ArrayList<Any>()
        var rebootIndex = operations.size

        for (i in operations.indices) {
            if (operations[i] is Char && i != operations.lastIndex && i < rebootIndex) {
                val operator = operations[i]
                val prevDigit = operations[i-1] as Double
                val nextDigit: Double? = if ((i+1) < operations.size) {
                                            operations[i + 1] as Double
                                        } else {
                                            null
                                        }

                when (operator) {
                    'x' -> {
                        calculations.add(prevDigit * nextDigit!!)
                        rebootIndex = i + 1
                    }
                    '/' -> {
                        calculations.add(prevDigit / nextDigit!!)
                        rebootIndex = i + 1
                    }
                    '%' -> {
                        if (nextDigit == null) {
                            calculations.add(prevDigit/100)
                        }else {
                            calculations.add((prevDigit/100) * nextDigit)
                        }
                    }
                    else -> {
                        calculations.add(prevDigit)
                        calculations.add(operator)
                    }
                }
            }
            if (i > rebootIndex) {
                calculations.add(operations[i])
            }
        }

        return calculations
    }

    private fun finalCalculations(calculations: ArrayList<Any>): Double {
        var calculate = calculations[0] as Double


        for (i in calculations.indices) {
            if (calculations[i] is Char && i != calculations.lastIndex) {
                val operator = calculations[i]
                val digit = calculations[i+1] as Double

                if (operator == '+') {
                    calculate += digit
                }

                if (operator == '-') {
                    calculate -= digit
                }
            }
        }

        return calculate
    }
}