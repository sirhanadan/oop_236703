//
// Created by asus on 02/04/2024.
//

#ifndef OOP5_BOARDCELL_H
#define OOP5_BOARDCELL_H


#include "CellType.h"
#include "Direction.h"

template<CellType Type, Direction Dir, int Len>
struct BoardCell{
    constexpr static CellType type = Type;
    constexpr static Direction direction = Dir;
    constexpr static int length = Len;
};


#endif //OOP5_BOARDCELL_H
