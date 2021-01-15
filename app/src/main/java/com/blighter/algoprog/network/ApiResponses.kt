package com.blighter.algoprog.network

data class BestSolution(
        val downloadTime: String,
        val quality: Int,
        val _id: String,
        val source: String,
        val language: String,
        val outcome: String,
        val problem: String,
        val user: String,
        val time: String
)

data class Cookies(val cookies: Boolean)

data class Materials(val _id: String, val title: String, val type: String, val content: String)

data class MaterialsInTaskList(val materials: Array<Materials>,
                               val force: String, val _id: String,
                               val type: String,
                               val title: String,
                               val order: String)

data class Me(val level: Level,
              val _id: String,
              val username: String,
              val informaticsId: Int,
              val informaticsUsername: String,
              val aboutme: String,
              val admin: Boolean,
              val __v: Int)

data class Level(val current: String, val start: String)

data class MyUser(val level: Level,
                  val _id: String,
                  val name: String,
                  val userList: String,
                  val rating: Int,
                  val activity: Double,
                  val ratingSort: Double,
                  val active: Boolean,
                  val achieves: ArrayList<String>)

data class Results(val compiler_output: String, val host: String, val tests: Map<Int, Test>)

data class Solution(val downloadTime: String,
                    val comments: Array<String>,
                    val force: Boolean,
                    val quality: Int,
                    val _id: String,
                    val results: Results,
                    val source: String,
                    val time: String,
                    val outcome: String,
                    val language: String)

data class Paths(val _id: String, val title: String)

data class Task(val path: List<Paths>,
                val _id: String,
                val title: String,
                val content: String)

data class Test(val time: Int,
                val status: String,
                val string_status: String,
                val real_time: Int,
                val max_memory_used: String)

data class UserData(val username: String?, val password: String?)



