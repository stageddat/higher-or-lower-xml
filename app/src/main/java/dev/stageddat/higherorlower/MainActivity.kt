package dev.stageddat.higherorlower

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // variables
    var currentCard = 0
    var score = 0
    var best = 0

    // baraja
    val suits = listOf("♠", "♥", "♦", "♣")
    var deckValues = mutableListOf<Int>()
    var deckSuits = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // views
        val tvScore: TextView = findViewById(R.id.tv_score)
        val tvCard: TextView = findViewById(R.id.tv_card)
        val tvMessage: TextView = findViewById(R.id.tv_message)
        val btnHigher: Button = findViewById(R.id.btn_higher)
        val btnLower: Button = findViewById(R.id.btn_lower)
        val btnRestart: Button = findViewById(R.id.btn_restart)

        // primera carta
        fillDeck()
        currentCard = drawCard(tvCard)
        tvScore.text = getString(R.string.tv_score, score, best, deckValues.size)

        // listeners
        // major listener
        btnHigher.setOnClickListener {
            val nextCard = drawCard(tvCard)

            if (nextCard == currentCard) {
                tvMessage.text = getString(R.string.msg_tie)
            } else if (nextCard > currentCard) {
                score += 1
                tvMessage.text = getString(R.string.msg_correct)
            } else {
                tvMessage.text = getString(R.string.msg_lost)
                btnHigher.isEnabled = false
                btnLower.isEnabled = false
                btnRestart.visibility = View.VISIBLE
            }

            if (score > best) {
                best = score
            }
            currentCard = nextCard

            // si no quedan cartas has ganado
            if (deckValues.isEmpty() && btnHigher.isEnabled) {
                tvMessage.text = getString(R.string.msg_deck_end)
                btnHigher.isEnabled = false
                btnLower.isEnabled = false
                btnRestart.visibility = View.VISIBLE
            }
            tvScore.text = getString(R.string.tv_score, score, best, deckValues.size)
        }

        // menor listener
        btnLower.setOnClickListener {
            val nextCard = drawCard(tvCard)

            if (nextCard == currentCard) {
                tvMessage.text = getString(R.string.msg_tie)
            } else if (nextCard < currentCard) {
                score += 1
                tvMessage.text = getString(R.string.msg_correct)
            } else {
                tvMessage.text = getString(R.string.msg_lost)
                btnHigher.isEnabled = false
                btnLower.isEnabled = false
                btnRestart.visibility = View.VISIBLE
            }

            if (score > best) {
                best = score
            }
            currentCard = nextCard

            // si no quedan cartas has ganado
            if (deckValues.isEmpty() && btnLower.isEnabled) {
                tvMessage.text = getString(R.string.msg_deck_end)
                btnHigher.isEnabled = false
                btnLower.isEnabled = false
                btnRestart.visibility = View.VISIBLE
            }
            tvScore.text = getString(R.string.tv_score, score, best, deckValues.size)
        }

        // tornar a jugar listener
        btnRestart.setOnClickListener {
            score = 0
            fillDeck()
            currentCard = drawCard(tvCard)
            tvMessage.text = getString(R.string.tv_message)
            tvScore.text = getString(R.string.tv_score, score, best, deckValues.size)
            btnHigher.isEnabled = true
            btnLower.isEnabled = true
            btnRestart.visibility = View.GONE
        }
    }

    // mete las 52 cartas (13 de cada palo)
    private fun fillDeck() {
        deckValues.clear()
        deckSuits.clear()
        for (suit in suits) {
            for (value in 1..13) {
                deckValues.add(value)
                deckSuits.add(suit)
            }
        }
    }

    // saca una carta random de la baraja, la enseña y devuelve su valor
    private fun drawCard(tvCard: TextView): Int {
        val index = (0 until deckValues.size).random()
        val value = deckValues.removeAt(index)
        val suit = deckSuits.removeAt(index)

        tvCard.text = getCardName(value) + suit
        if (suit == "♥" || suit == "♦") {
            tvCard.setTextColor(Color.RED)
        } else {
            tvCard.setTextColor(Color.BLACK)
        }
        return value
    }

    private fun getCardName(card: Int): String {
        return when (card) {
            1 -> "A"
            11 -> "J"
            12 -> "Q"
            13 -> "K"
            else -> card.toString()
        }
    }
}
