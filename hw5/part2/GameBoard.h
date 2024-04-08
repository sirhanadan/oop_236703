//
// Created by asus on 02/04/2024.
//

#ifndef OOP5_GAMEBOARD_H
#define OOP5_GAMEBOARD_H

#include "List.h"

template<typename T>
struct GameBoard;


template<typename... T>
struct GameBoard<List<T...>>
{
    typedef List<T...> board;
    static constexpr int width = List<T...>::head::size;
    static constexpr int length = List<T...>::size;

};


#endif //OOP5_GAMEBOARD_H
