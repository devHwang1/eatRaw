package com.example.eatraw.data

import com.example.eatraw.model.Affirmation


class Datasource {
    fun loadAffirmation(): List<Affirmation> {  // 함수 이름 변경
        val list = mutableListOf<Affirmation>()
        for (i in 1..20) {
            list.add(Affirmation("Item $i"))  // Affirmation 객체를 생성
        }

        return list.toList()
    }
}