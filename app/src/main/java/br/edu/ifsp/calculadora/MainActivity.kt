package br.edu.ifsp.calculadora

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import br.edu.ifsp.scl.calculadorasdmkt.Calculadora
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private var cache: String = ""
    private var arrayOperands: ArrayList<Float> = ArrayList()
    private var arrayOperators: ArrayList<String> = ArrayList()
    private var lastOperand: Float = 0f
    private var firstOperation: String = ""
    private var isPercentOperation: Boolean = false

    private enum class errors(val value: String) {
        DIVIDE_BY_ZERO("Não há divisão por zero"),
        ERRO("Erro na operação")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clickEvent: View.OnClickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.btnZero -> addValueToMainField("0")
                R.id.btnOne -> addValueToMainField("1")
                R.id.btnTwo -> addValueToMainField("2")
                R.id.btnThree -> addValueToMainField("3")
                R.id.btnFour -> addValueToMainField("4")
                R.id.btnFive -> addValueToMainField("5")
                R.id.btnSix -> addValueToMainField("6")
                R.id.btnSeven -> addValueToMainField("7")
                R.id.btnEight -> addValueToMainField("8")
                R.id.btnNine -> addValueToMainField("9")
                R.id.btnC -> clearAll()
                R.id.btnCE -> clearCurrent()
                R.id.btnAddition -> addValueToMainField("+")
                R.id.btnSubtraction -> addValueToMainField("-")
                R.id.btnMultiplication -> addValueToMainField("*")
                R.id.btnDivision -> addValueToMainField("/")
                R.id.btnSquareRoot -> addValueToMainField("√")
                R.id.btnPercent -> addValueToMainField("%")
                R.id.btnPoint -> addValueToMainField(".")
                R.id.btnEquals -> solve()
            }
        }

        btnC.setOnClickListener(clickEvent)
        btnCE.setOnClickListener(clickEvent)
        btnSquareRoot.setOnClickListener(clickEvent)
        btnPercent.setOnClickListener(clickEvent)
        btnDivision.setOnClickListener(clickEvent)
        btnMultiplication.setOnClickListener(clickEvent)
        btnSubtraction.setOnClickListener(clickEvent)
        btnAddition.setOnClickListener(clickEvent)
        btnZero.setOnClickListener(clickEvent)
        btnOne.setOnClickListener(clickEvent)
        btnTwo.setOnClickListener(clickEvent)
        btnThree.setOnClickListener(clickEvent)
        btnFour.setOnClickListener(clickEvent)
        btnFive.setOnClickListener(clickEvent)
        btnSix.setOnClickListener(clickEvent)
        btnSeven.setOnClickListener(clickEvent)
        btnEight.setOnClickListener(clickEvent)
        btnNine.setOnClickListener(clickEvent)
        btnPoint.setOnClickListener(clickEvent)
        btnEquals.setOnClickListener(clickEvent)
    }

    private fun solve() {

        if (txtMain.text.isEmpty()) {
            txtMain.text = "0"; return
        }

        if (!isPercentOperation) {
            arrayOperands.add(lastOperand)
            lastOperand = 0f
        }

        while (arrayOperators.size > 0) {

            handleFirstRule()
            handleSecondRule()
            handleThirdRule()

        }


        txtAbsolute.text = cache

        arrayOperands.clear()
        arrayOperators.clear()
        isPercentOperation = false
    }

    private fun handleFirstRule() {
        var result = 0f

        try {

            if (arrayOperators.contains("√") || arrayOperators.contains("%")) {

                for (i in 0 until arrayOperators.size) {

                    if (arrayOperators[i] == "%") {
                        result = arrayOperands[i] / 100
                        cache = result.toString()

                        arrayOperators.removeAt(i)

                        arrayOperands.removeAt(i)
                        arrayOperands.add(i, result)
                        return handleFirstRule()
                    }

                    if (arrayOperators[i] == "√") {
                        result = Math.sqrt(arrayOperands[i].toDouble()).toFloat()
                        cache = result.toString()

                        arrayOperators.removeAt(i)

                        arrayOperands.removeAt(i)
                        arrayOperands.add(i, result)

                        return handleFirstRule()
                    }

                }

                return handleSecondRule()

            }
            return

        } catch (e: Exception) {
            return
        }
    }

    private fun handleSecondRule() {
        var result = 0f

        try {

            if (arrayOperators.contains("*") || arrayOperators.contains("/")) {

                for (i in 0 until arrayOperators.size) {

                    if (arrayOperators[i] == "*") {
                        result = arrayOperands[i] * arrayOperands[i + 1]
                        cache = result.toString()

                        handlerArrays(i)

                        arrayOperands.add(i, result)
                        return handleSecondRule()
                    }

                    if (arrayOperators[i] == "/") {
                        if (arrayOperands[i + 1] == 0f) {
                            addValueToMainField(errors.DIVIDE_BY_ZERO.value)
                            clearAll()
                            return
                        }
                        result = arrayOperands[i] / arrayOperands[i + 1]
                        cache = result.toString()

                        handlerArrays(i)

                        arrayOperands.add(i, result)
                        return handleSecondRule()
                    }

                }

                return handleSecondRule()

            }
            return

        } catch (e: Exception) {
            return
        }
    }

    private fun handleThirdRule() {
        try {
            var result = 0f

            if (arrayOperators.contains("+") || arrayOperators.contains("-")) {

                for (i in 0 until arrayOperators.size) {

                    if (arrayOperators[i] == "+") {
                        result = arrayOperands[i] + arrayOperands[i + 1]
                        cache = result.toString()

                        handlerArrays(i)

                        arrayOperands.add(i, result)
                        return handleThirdRule()
                    }

                    if (arrayOperators[i] == "-") {
                        result = arrayOperands[i] - arrayOperands[i + 1]
                        cache = result.toString()
                        handlerArrays(i)

                        arrayOperands.add(i, result)
                        return handleThirdRule()
                    }
                }

                return handleThirdRule()

            }

            return
        } catch (e: Exception) {
            return
        }
    }

    private fun handlerArrays(i: Int) {

        arrayOperands.removeAt(i + 1)
        arrayOperands.removeAt(i)

        arrayOperators.removeAt(i)
    }

    private fun clearCurrent() {
        txtMain.text = ""
        return
    }

    private fun clearAll() {
        clearCurrent()
        txtAbsolute.text = ""
        cache = ""

        arrayOperands.clear()
        arrayOperators.clear()
        return
    }

    private fun operation(operator: String): Boolean {
        if (txtMain.text.isEmpty()) {

            if (operator.equals("-")) {
                firstOperation = "-"
            } else if (operator.equals("√")) {
                firstOperation = "√"
            } else {
                return false
            }

        } else {
            //arrayOperands.add(cache.toFloat())
            if (cache.isNotEmpty()) {
                arrayOperands.add(cache.toFloat())
            }

            isPercentOperation = operator.equals("%")
            arrayOperators.add(operator)
            cache = ""
        }

        return true
    }

    private fun addValueToMainField(value: String) {

        when (value) {
            errors.ERRO.value -> {
                txtMain.text = errors.ERRO.value; clearAll(); return
            }
            errors.DIVIDE_BY_ZERO.value -> {
                txtMain.text = errors.DIVIDE_BY_ZERO.value; clearAll(); return
            }
        }

        if (txtMain.text.equals("0") || txtMain.text.equals("0.0"))
            txtMain.text = ""

        if (txtAbsolute.text.isNotEmpty()) {
            if (isNotAnOperation(value)) {
                txtMain.text = ""
                txtAbsolute.text = ""
                cache = ""
            } else {
                if (value != "√") {
                    txtMain.text = txtAbsolute.text
                    txtAbsolute.text = ""

                    cache = txtMain.text.toString()
                } else{
                    clearAll()
                    return
                }
            }
        }

        if (isNotAnOperation(value)) {        //is a value

            if (firstOperation.isNotEmpty()) {
                if (firstOperation.equals("-")) {
                    if(value.equals(".")){
                        cache = "-0" + value
                    } else {
                        cache = "-" + value
                    }

                    firstOperation = ""
                }

                if (firstOperation.equals("√")) {
                    if(value.equals(".")){
                        cache = "0" + value
                    } else {
                        cache = value
                    }
                    arrayOperators.add(firstOperation)
                    firstOperation = ""
                }

            } else {
                if(value.equals(".")){
                    if(cache.contains(".")) return

                    cache += "0" + value
                    if(txtMain.text.isEmpty()){
                        txtMain.text = "0"
                    } else {
                        txtMain.text = txtMain.text.toString() + "0"
                    }
                }else {
                    cache+= value
                    lastOperand = cache.toFloat()
                }
            }
        } else {
            if (!operation(value)) {
                clearAll()
                return
            }
        }


        val sb = StringBuilder()
        sb.append(txtMain.text).append(value)

        txtMain.text = sb.toString()
    }

    private fun isNotAnOperation(value: String): Boolean {
        if (value.equals("+")) {
            return false
        }
        if (value.equals("-")) {
            return false
        }
        if (value.equals("*")) {
            return false
        }
        if (value.equals("/")) {
            return false
        }
        if (value.equals("√")) {
            return false
        }
        if (value.equals("%")) {
            return false
        }

        return true
    }
}