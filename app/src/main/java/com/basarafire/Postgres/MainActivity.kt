package com.basarafire.Postgres

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var listview: ListView
    private lateinit var button : Button
    private lateinit var button2: Button
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listview = findViewById(R.id.listview)

        //更新ボタン
        button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            dbAccess()
        }

        //挿入ボタン
        button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            insertData()
        }

        dbAccess()
    }

    /**
     * DBにアクセスして、Categoryテーブルのカテゴリ名リストを取得、ListViewに表示
     */
    private fun dbAccess(){
        button.isEnabled = false
        button2.isEnabled = false
        thread {
            //DBからCategoryテーブルのカテゴリ名リストを取得
            val list = DBAccess.getCategory(this)

            //デバッグ表示
            list?.forEach {
                Log.d(TAG, it.name)
            }
            runOnUiThread {
                button.isEnabled = true
                button2.isEnabled = true
                // Adapterに渡す配列を作成します
                if (list == null) {
                    return@runOnUiThread
                }
                val data = list.toTypedArray()

                // adapterを作成します
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    data
                )

                // adapterをlistViewに紐付けます。
                listview?.adapter = adapter
            }
        }
    }

    /**
     * DBにアクセスして、Categoryテーブルのカテゴリ名リストを取得、ListViewに表示
     */
    private fun insertData(){
        button.isEnabled = false
        button2.isEnabled = false
        thread {
            //DBからCategoryテーブルのカテゴリ名リストを取得
            DBAccess.insertCategory(this)

            runOnUiThread {
                dbAccess()
            }
        }
    }
}