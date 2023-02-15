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
            if (view.text == ",") {
                val char = tvShow.text.last()
                if (addDecimal && char.isDigit()) {
                    tvShow.append(view.text)
                    addDecimal = false
                    //Pulir codigo para que no explote si introduces coma al iniciar
                }

            }else {
                tvShow.append(view.text)
                addOperation = true
                /*Si se ha añadido un número permite añadir un operador*/
            }
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
    }

    fun eraseAction(view: View) {
        view.setOnClickListener{
            if (tvShow.length() > 0)
                tvShow.text = tvShow.text.dropLast(1)
        }

        view.setOnLongClickListener{
            tvShow.text = ""
            true
        }
    }

    fun equalsAction(view: View) {
       tvResult.text = result()
    }

    fun result(): String {
        val operations = sortOperations()
        if (operations.isEmpty())
            return ""

        val calculations = priorityCalculations(operations)
        if (calculations.isEmpty())
            return ""

        val result = calculate(calculations)

        return result.toString()
    }

    fun sortOperations(): ArrayList<Any> {
        val operations = ArrayList<Any>()
        var currentDigit = ""

        for (c in tvShow.text) {
            if (c.isDigit() || c == ',') {
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

    fun priorityCalculations(operations: ArrayList<Any>): ArrayList<Any> {
        val calculations = ArrayList<Any>()



        return calculations
    }

    fun calculate(calculations: ArrayList<Any>): Double {
        var calculate = calculations[0] as Double

        for (i in calculations.indices) {
            if (calculations[i] is Char && i != calculations.lastIndex) {
                
            }
        }

        return calculate
    }
}