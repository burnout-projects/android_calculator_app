package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calculator.databinding.ActivityMainBinding
import java.lang.Exception
import java.util.*

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var prevValue: String = ""

    private var isEqualPressed = false

    private var mathOperators: Array<String> = arrayOf("+", "-", "*", "/", "^")
    private var symbols: Array<String> = arrayOf("+", "-", "*", "/", "^", ".")

    private var isBrace = false
    private var braceStack = Stack<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setContentView(R.layout.activity_main)
        // binding.result.text = evalExpr("2+3*4").toString()

        binding.one.setOnClickListener { displayText("1") }
        binding.two.setOnClickListener { displayText("2") }
        binding.three.setOnClickListener { displayText("3") }
        binding.four.setOnClickListener { displayText("4") }
        binding.five.setOnClickListener { displayText("5") }
        binding.six.setOnClickListener { displayText("6") }
        binding.seven.setOnClickListener { displayText("7") }
        binding.eight.setOnClickListener { displayText("8") }
        binding.nine.setOnClickListener { displayText("9") }
        binding.zero.setOnClickListener { displayText("0") }
        binding.plus.setOnClickListener { displayText("+") }
        binding.minus.setOnClickListener { displayText("-") }
        binding.multiply.setOnClickListener { displayText("*") }
        binding.divide.setOnClickListener { displayText("/") }
        binding.point.setOnClickListener { displayText(".") }
        binding.allclear.setOnClickListener { displayText("C") }
        binding.braces.setOnClickListener { displayText("()") }
        binding.power.setOnClickListener { displayText("^") }
        binding.clear.setOnClickListener { displayText("bksp") }
        binding.equals.setOnClickListener {
            isEqualPressed = true
            evalExpr(binding.result.text.toString())
        }
    }

    private fun displayText(text: String) {
        var textToDisplay: String = binding.result.text.toString()

        if (isEqualPressed) {
            if (symbols.contains(text)) {
                prevValue = text
                textToDisplay = textToDisplay.plus(text)
            }
            else if (text == "bksp" || text == "C") {
                textToDisplay = ""
                braceStack.clear()
            }
            else if (text == "()") {
                textToDisplay = "("
                isBrace = true
            }
            else {
                textToDisplay = text
            }
            isEqualPressed = false
        }
        else {
            if (symbols.contains(prevValue) && symbols.contains(text)) {
                textToDisplay = textToDisplay.dropLast(1) + text
            }
            else if (text == "C") {
                textToDisplay = ""
                braceStack.clear()
            }
            else if (text == "bksp") {
                // upon backspace if the char to be deleted is a brace, take care to adjust the braceStack
                if (textToDisplay.takeLast(1) == "(") {
                    braceStack.pop()
                }
                else if (textToDisplay.takeLast(1) == ")") {
                    braceStack.push("(")
                }
                textToDisplay = textToDisplay.dropLast(1)
            }
            else if (text == "()") {
                if (braceStack.empty()) {
                    braceStack.push("(")
                    textToDisplay = textToDisplay.plus("(")
                    prevValue = "("
                }
                else {
                    if (mathOperators.contains(prevValue) || prevValue == "(") {
                        braceStack.push("(")
                        textToDisplay = textToDisplay.plus("(")
                        prevValue = "("
                    }
                    else {
                        braceStack.pop()
                        textToDisplay = textToDisplay.plus(")")
                        prevValue = ")"
                    }
                }
            }
            else {
                prevValue = text
                textToDisplay = textToDisplay.plus(text)
            }
        }
        binding.result.text = textToDisplay
    }

    private fun evalExpr(expression: String): Unit {
        var result: String = ""
        result = try {
            val engine: ScriptEngine = ScriptEngineManager().getEngineByName("rhino")
            engine.eval(expression).toString()
        } catch (e: Exception) {
            "ERROR"
        }
        val dispTextLength: Int = result.length
        if (dispTextLength > 2 && result.substring(dispTextLength-2, dispTextLength) == ".0"){
            result = result.dropLast(2)
        }
        binding.result.text = expression + '\n' + result
    }
}

