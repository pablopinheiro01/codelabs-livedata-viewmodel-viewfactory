package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
    get() = _word

    // The current score
    //encapsulado o score para ser alterado somente pelo livedata
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> //permitido a consulta via get do Score
    get() = _score

    // The list of words - the front of the list is the next word to guess
    lateinit var wordList: MutableList<String>

    //MutableLiveData para monitorar quando o jogo for finalizado
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
    get()  = _eventGameFinish


    init {
        Log.i("GameViewModel","GameViewModel Created!")
        _word.value = ""
        _score.value = 0
        resetList()
        nextWord()

    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel","GameViewModel destroyed!")
    }


    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
//        score.value =+ 1
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (!wordList.isEmpty()) {
            //Select and remove a word from the list
            _word.value = wordList.removeAt(0)
        }else{
            onGameFinish()
        }
    }

    fun onGameFinish(){
        _eventGameFinish.value = true
    }

}