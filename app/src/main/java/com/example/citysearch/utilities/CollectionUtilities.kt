package com.example.citysearch.utilities

class CollectionUtilities {

    companion object {

        fun<T1, T2> zip(first: Array<T1>, second: List<T2>): List<Pair<T1, T2>> {

            return zip(first.toList(), second)
        }

        fun<T1, T2> zip(first: List<T1>, second: Array<T2>): List<Pair<T1, T2>> {

            return zip(first, second.toList())
        }

        fun<T1, T2> zip(first: Array<T1>, second: Array<T2>): List<Pair<T1, T2>> {

            return zip(first.toList(), second.toList())
        }

        fun<T1, T2> zip(first: List<T1>, second: List<T2>): List<Pair<T1, T2>> {

            val count = Math.min(first.size, second.size)

            var result = ArrayList<Pair<T1, T2>>()
            for(index in IntRange(0, count - 1)) {

                result.add(Pair(first[index], second[index]))
            }

            return result
        }
    }
}