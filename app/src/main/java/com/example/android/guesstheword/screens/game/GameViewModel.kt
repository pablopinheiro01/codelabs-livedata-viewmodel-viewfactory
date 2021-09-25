package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

    //variavel Mutable para controle do tempo
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
    get() = _currentTime

    private val timer: CountDownTimer

    //versao do currentTime em String para exibicao na tela
    //apenas vinculando essa variavel na tela o LiveData definido em currentTime vincula automaticamente essa propriedade ao layout
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    val wordHint = Transformations.map(word) { word ->
        val randomPosition = (1..word.length).random()
        "PALAVRA DO MOMENTO TEM "+word.length+" LETRAS "+ " \n A letra na posicao "+ randomPosition + " É " + word.get(randomPosition -1 ).toUpperCase()
    }


    init {
        Log.i("GameViewModel","GameViewModel Created!")
        _word.value = ""
        _score.value = 0
        resetList()
        nextWord()

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                //calculo cada passo do contador divido pelo segundo.
                _currentTime.value = millisUntilFinished/ ONE_SECOND
            }

            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }

        }
        timer.start()

    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel","GameViewModel destroyed!")
        timer.cancel() //limpo o timer para evitar vazamento de memoria
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
            //metodo migrado para o contador de tempo
//            onGameFinish()
            resetList() //limpo a lista
        }
    }

    //finaliza o jogo apos exibir todas as palavras da lista
    fun onGameFinish(){
        _eventGameFinish.value = true
    }

    //apos completar o jogo removemos o valor de true setando false
    fun onGameFinishComplete(){
        _eventGameFinish.value = false
    }

    companion object {
        //Time quando o jogo acaba
        private const val DONE = 0L

        //CountDown do intervalo de tempo
        private const val ONE_SECOND = 1000L

        //Tempo total para o jogo
        private const val COUNTDOWN_TIME = 60000L
    }

}