package com.example.projektnpo

import android.location.Address

sealed class resultLocatioinRequest {
    class Sucesso(val msg: String, val adress: Address) : resultLocatioinRequest()
    class Erro(val msg: String) : resultLocatioinRequest()
}