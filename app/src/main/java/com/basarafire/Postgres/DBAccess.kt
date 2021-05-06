package com.basarafire.Postgres

import android.content.Context
import java.sql.*

/**
 *　DB接続を管理するクラス
 */
class DBAccess {

    /**
     * Categoryテーブルの１レコードのデータを格納するデータクラス
     */
    data class Category(val id: Int, val name: String, val date: Date)

    companion object{
        /**
         * PostgSQLのDBへのコネクションを取得
         */
        @Throws(SQLException::class)
        private fun getConnection(context: Context): Connection? {
            var conn: Connection? = null
            try {
                Class.forName("org.postgresql.Driver")
                val server =
                    ("jdbc:postgresql://10.0.2.2:5432/dvdrental")
                conn = DriverManager.getConnection(server, "postgres", "akira21")
            } catch (e: ClassNotFoundException) {
                // TODO 自動生成された catch ブロック
                e.printStackTrace()
                throw SQLException()
            }
            return conn
        }

        /**
         * カテゴリテーブルのテーブル名をリストで取得
         */
        fun getCategory(context: Context): List<Category>? {
            var conn: Connection? = null
            var rs: ResultSet? = null
            var preparedStatement: PreparedStatement? = null
            val nameList = mutableListOf<Category>()
            try {
                conn = getConnection(context)
                if (conn == null){
                    return null;
                }
                preparedStatement = conn.prepareStatement("select * from category order by category_id desc")
                rs = preparedStatement.executeQuery()
                while (rs.next()) {
                    val id = rs.getInt("category_id")
                    val name = rs.getString("name")
                    val date = rs.getDate("last_update")
                    nameList.add(Category(id, name, date))
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                try {
                    rs?.close()
                    preparedStatement?.close()
                    conn?.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
            return nameList
        }

        /**
         * カテゴリテーブルにTestという名前のカテゴリを追加する
         */
        fun insertCategory(context: Context) {
            var conn: Connection? = null
            var preparedStatement: PreparedStatement? = null
            try {
                conn = getConnection(context)
                if (conn == null){
                    return
                }
                val resultSql = "insert into category (name) values(?)"
                preparedStatement = conn.prepareStatement(resultSql)
                preparedStatement.setString(1, "Test")
                preparedStatement.executeUpdate()

            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                try {
                    preparedStatement?.close()
                    conn?.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
    }


}