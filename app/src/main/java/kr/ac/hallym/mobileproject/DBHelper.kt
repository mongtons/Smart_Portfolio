package kr.ac.hallym.mobileproject

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, "PortfolioDB", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE if not exists Project ("+
            "id integer primary key autoincrement not null,"+
            "title text not null,"+
            "summary text not null,"+
            "category text"+
            ");"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE if exists Project")
        onCreate(p0)
    }
}