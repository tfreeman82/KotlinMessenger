package com.example.tristanfreeman.kotlinfirebasemessenger.Model

data class User (
        val uid: String?,
        val username:String?,
        val profileImageUrl: String?
){
    constructor() : this("", "", "")
}