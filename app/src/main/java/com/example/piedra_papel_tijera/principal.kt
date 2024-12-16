package com.example.piedra_papel_tijera

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random //importamos la libreria de random para que la comp

class principal : AppCompatActivity() {

    data class ButtonData(val word: String, val imageResourceId: Int) //creamos la data class para los botones, aqui se guarda el nombre y la imagen
    private var userScore = 0 //creamos las variables para los puntajes del jugador
    private var computerScore = 0 //creamos las variables para los puntajes de la computadora
    private var roundCount = 0 //creamos las variables para los rounds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initializeButtons() //llamamos a la funcion para inicializar los botones
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun initializeButtons() { //funcion para inicializar los botones
        val piedraButton = findViewById<Button>(R.id.piedra) //inicializamos el boton piedra
        val papelButton = findViewById<Button>(R.id.papel) //inicializamos el boton papel
        val tijeraButton = findViewById<Button>(R.id.tijera) //inicializamos el boton tijera
        val imageView = findViewById<ImageView>(R.id.imageView) //inicializamos la imagen para el jugador
        val imageView2 = findViewById<ImageView>(R.id.imageView2) //inicializamos la imagen para la computadora
        val resultTextView = findViewById<TextView>(R.id.resultTextView) //inicializamos el textview para el resultado

        piedraButton.tag = ButtonData("piedra", R.drawable.pim) //inicializamos los tags para el boton piedra
        papelButton.tag = ButtonData("papel", R.drawable.pam) //inicializamos los tags para el boton papel
        tijeraButton.tag = ButtonData("tijera", R.drawable.tim) //inicializamos los tags para el boton tijera

        val buttonClickListener = { view: android.view.View -> //funcion para el click de los botones
            val userData = view.tag as ButtonData //obtenemos el tag del boton presionado
            imageView.setImageResource(userData.imageResourceId) //cambiamos la imagen del jugador
            val computerData = computerSelection() //obtenemos la seleccion de la computadora
            imageView2.setImageResource(computerData.imageResourceId) //cambiamos la imagen de la computadora
            val result = determineWinner(userData.word, computerData.word) //obtenemos el resultado
            updateScores(result) //actualizamos los puntajes
            roundCount++ //incrementamos el round
            resultTextView.text = result //cambiamos el textview del resultado

            if (roundCount == 10) { //limitamos los rounds a 10
                displayFinalResult() //mostramos el resultado final
                resetGame() //reiniciamos el juego
            }
        }

        piedraButton.setOnClickListener(buttonClickListener) //ponemos el listener para el boton piedra
        papelButton.setOnClickListener(buttonClickListener) //ponemos el listener para el boton papel
        tijeraButton.setOnClickListener(buttonClickListener)//ponemos el listener para el boton tijera
    }

    private fun computerSelection(): ButtonData { //funcion para la seleccion de la computadora
        val options = listOf("piedra", "papel", "tijera") //
        val randomIndex = Random.nextInt(options.size) //obtenemos un numero random
        val word = options[randomIndex] //obtenemos la palabra random
        val imageResourceId = when (word) { //obtenemos la imagen random
            "piedra" -> R.drawable.pim //obtenemos la imagen random
            "papel" -> R.drawable.pam //obtenemos la imagen random
            "tijera" -> R.drawable.tim //obtenemos la imagen random
            else -> throw IllegalArgumentException("Invalid word: $word") //si no es ninguna de las anteriores, lanza un error
        }
        return ButtonData(word, imageResourceId) //retornamos la palabra y la imagen random
    }

    private fun determineWinner(userChoice: String, computerChoice: String): String { //funcion para determinar el ganador
        if (userChoice == computerChoice) { //si son iguales, es un empate
            userScore++ //incrementamos el puntaje del jugador
            println(userScore) //imprimimos el puntaje del jugador
            computerScore++ //incrementamos el puntaje de la computadora
            println(computerScore) //imprimimos el puntaje de la computadora
            return "Empate!" //retornamos el resultado
        } else if (
            (userChoice == "piedra" && computerChoice == "tijera") ||
            (userChoice == "tijera" && computerChoice == "papel") ||
            (userChoice == "papel" && computerChoice == "piedra") //si no son iguales, se compara la seleccion del jugador con la de la computadora
        ) {
            userScore++ //si gana el jugador, incrementamos el puntaje del jugador
            return "¡Ganaste!"
        } else {
            computerScore++ //si gana la computadora, incrementamos el puntaje de la computadora
            return "¡Perdiste!"
        }
    }

    private fun updateScores(result: String) { //funcion para actualizar los puntajes de la mano actual
        println("usuario: $userScore")
        println("computadora: $computerScore")
        val resultTextView = findViewById<TextView>(R.id.resultTextView) //obtenemos el textview del resultado en texto por mano
        val userScoreTextView = findViewById<TextView>(R.id.userScoreTextView) //obtenemos el textview del puntaje del jugador
        val computerScoreTextView = findViewById<TextView>(R.id.computerScoreTextView) //obtenemos el textview del puntaje de la computadora
        userScoreTextView.text = "Usuario: $userScore" //cambiamos el textview del puntaje del jugador
        computerScoreTextView.text = "Computadora: $computerScore" //cambiamos el textview del puntaje de la computadora
        println(result)

        resultTextView.text = result //cambiamos el textview del resultado en texto por mano
    }

    private fun displayFinalResult() { //funcion para mostrar el resultado final
        val resultTextView = findViewById<TextView>(R.id.resultTextView) //obtenemos el textview del resultado
        if (userScore > computerScore) { //si el jugador gana, mostramos el resultado
            resultTextView.text = "¡GANASTE!"
        } else if (userScore < computerScore) { //si la computadora gana, mostramos el resultado
            resultTextView.text = "¡PERDISTE!"
        } else {
            resultTextView.text = "¡EMPATE!" //si es un empate, mostramos el resultado
        }
    }

    private fun resetGame() { //funcion para reiniciar el juego
        val resultTextView = findViewById<TextView>(R.id.resultTextView) //obtenemos el textview del resultado
        if (userScore > computerScore) { //si el jugador gana, mostramos el resultado
            resultTextView.text = "Finalizado, $userScore - $computerScore, Ganaste! " //cambiamos el textview del resultado
        } else if (userScore < computerScore) { //si la computadora gana, mostramos el resultado
            resultTextView.text = "Finalizado, $userScore - $computerScore, Perdiste! " //cambiamos el textview del resultado
        } else { resultTextView.text = "Finalizado, $userScore - $computerScore, Empate! " } //si es un empate, mostramos el resultado
        //reiniciamos los puntajes
        userScore = 0
        computerScore = 0
        roundCount = 0
    }

}
