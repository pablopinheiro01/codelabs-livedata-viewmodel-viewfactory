/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        Log.i("GameFragment","Chamando ViewModel")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        //variavel definida no xml do game_fragment.xml
        binding.gameViewModel = viewModel

        //definindo a view como dono do ciclo de vida
        //esta alteração permite que o escopo do LiveData permitindo que o objeto atualize automaticamente no layout
        binding.lifecycleOwner = viewLifecycleOwner

        //atualiza o score atraves de um observer
        //este metodo substitui as functions updateScoreText() e updateWordText()
        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })


        //observer para o game se for encerrado por falta de palavras
        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer<Boolean> { hasFinished ->
            if(hasFinished) gameFinished()
        })


        return binding.root
    }

    /** Methods for updating the UI **/

    private fun updateWordText() {
        binding.wordText.text = viewModel.word.value
    }

    private fun updateScoreText() {
        binding.scoreText.text = viewModel.score.value.toString()
    }


    private fun gameFinished(){
        Toast.makeText(activity, "Game has just finished", Toast.LENGTH_LONG).show()
        viewModel.onGameFinishComplete() //finalizando o game alterando o boolean que estava sendo observado para redirecionar
        val action = GameFragmentDirections.actionGameToScore()
        action.score = viewModel.score.value?:0
        controller.navigate(action)
    }
}
