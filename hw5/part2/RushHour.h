//
// Created by asus on 05/04/2024.
//

#ifndef OOP5_RUSHHOUR_H
#define OOP5_RUSHHOUR_H

#include "./BoardCell.h"
#include "./CellType.h"
#include "./Utilities.h"
#include "./Direction.h"
#include "./GameBoard.h"
#include "./List.h"
#include "./MoveVehicle.h"


/********************************************  CHECK WIN  **************************************************/
template<typename T, CellType Car>
struct FindCarInRow;

template<CellType Car>
struct FindCarInRow<List<>, Car>{
    static constexpr bool result = false;
    static constexpr int colIndex = -1;
};

template<typename T, typename... Rest, CellType Car>
struct FindCarInRow<List<T, Rest...>, Car>{
    static constexpr bool result = ConditionalInteger<(T::type == Car) , true, FindCarInRow<List<Rest...>, Car>::result>::value;
    static constexpr int colIndex = ConditionalInteger<(T::type == Car) , 0, 1 + FindCarInRow<List<Rest...>, Car>::colIndex>::value;
};


//template<typename... T>
//struct FindEndOfRedCar;
//
//template<typename... T>
//struct FindEndOfRedCar<List<T...>>{
//    typedef FindCarInRow<List<T...>> redCarCoors;
//
//    static constexpr int colIndex = redCarCoors::colIndex;
//
//    static constexpr int cell = ConditionalInteger<(colIndex != -1), GetAtIndex<colIndex, List<T...>>::value::length, 0>::value;
//
//    static constexpr int lastColIndex = colIndex + cell;
//
//
//};


template<typename T, CellType Car>
struct FindCar;

template<CellType Car>
struct FindCar<List<>, Car>{
    static constexpr bool result = false;
    static constexpr int rowIndex = -1;
    static constexpr int colIndex = -1;

};

template<typename T, typename... Rest, CellType Car>
struct FindCar<List<T, Rest...>, Car>{
    static constexpr bool result = ConditionalInteger<(FindCarInRow<T, Car>::result) , true, FindCar<List<Rest...>, Car>::result>::value;
    static constexpr int rowIndex = ConditionalInteger<(FindCarInRow<T, Car>::result) , 0 , 1 + FindCar<List<Rest...>, Car>::rowIndex>::value;
    static constexpr int colIndex = ConditionalInteger<(FindCarInRow<T, Car>::result) , (FindCarInRow<T, Car>::colIndex),  FindCar<List<Rest...>, Car>::colIndex>::value;
};



//finds the index at the furthest right
template<typename Row, int ColNum, int Border> //note that Row is an entire list and ColNum is just an index in that list
struct FindCarLastIndex;


template<typename... T, int ColNum, int Border> //T - the type of the elements in a row
struct FindCarLastIndex<List<T...> , ColNum, Border>{

    typedef typename GetAtIndex<ColNum,List<T...>>::value currCell;
    typedef typename GetAtIndex<ColNum+1,List<T...>>::value nextCell;
    static constexpr bool endOfCar = (currCell::type != nextCell::type);
    //if this is the end of the car then this is the index we're looking for
    //otherwise check the next index
    static constexpr int index = ConditionalInteger<(endOfCar) ,ColNum,FindCarLastIndex< List<T...>, ColNum+1, Border>::index>::value;

};

//reached the end, there is no next
template<typename... T, int Border>
struct FindCarLastIndex<List<T...> , Border, Border>{
    static constexpr int index = Border;
};




template<typename T, int From, int To>
struct isEmptyFromTo;


template<typename T, typename... Rest, int From, int To>
struct isEmptyFromTo<List<T, Rest...>, From, To> {
    static constexpr bool result = isEmptyFromTo<List<Rest...>, From -1, To -1>::result;
};

template<typename T, typename... Rest, int To>
struct isEmptyFromTo<List<T, Rest...>, 0, To> {
    static constexpr bool result = ConditionalInteger<(T::type == EMPTY), isEmptyFromTo<List<Rest...>, 0, To -1>::result, false >::value;
    //static constexpr bool result = true;
};

template<typename T, typename... Rest, int From>
struct isEmptyFromTo<List<T, Rest...>, From, 0> {
    //static constexpr bool result = ConditionalInteger<(T::type == EMPTY), isEmptyFromTo<List<Rest...>, 0, To -1>::result, false >::value;
    static constexpr bool result = true;
};

template<typename T, typename... Rest>
struct isEmptyFromTo<List<T, Rest...>, 0, 0> {
    static constexpr bool result = (T::type == EMPTY);
    //static constexpr bool result = true;
};



template<typename T>
struct isRightPathEmpty;

template<>
struct isRightPathEmpty<List<>>{
    static constexpr bool result = true;
};


template<typename... T>
struct isRightPathEmpty<List<T...>>{
    static constexpr int size = List<T...>::head::size;


    typedef FindCar<List<T...>, X> redCar;
    static constexpr int rowIndex = redCar::rowIndex;
    static constexpr int colIndex = redCar::colIndex;

    typedef typename GetAtIndex<rowIndex, List<T...>>::value rowList;

    static constexpr int lastColIndex = FindCarLastIndex<rowList, colIndex, size-1>::index;




    static constexpr bool result = isEmptyFromTo<rowList, lastColIndex+1, size-1>::result;

};


template<typename B>
struct CheckWin;


template<typename B>
struct CheckWin<GameBoard<B>>{
    static constexpr bool result = isRightPathEmpty<B>::result;

};



/********************************************  CHECK SOL  **************************************************/

template<typename B, typename M>
struct CheckSolution;


template<typename B>
struct CheckSolution<GameBoard<B>, List<>>{
    static constexpr bool result = CheckWin<GameBoard<B>>::result;
};

template<typename B, typename M, typename... Rest>
struct CheckSolution<GameBoard<B>, List<M, Rest...>>{
    typedef FindCar<B, M::type> carCoors;
    static constexpr int Ro = carCoors::rowIndex;
    static constexpr int Co = carCoors::colIndex;
    static constexpr Direction Dir = M::direction;
    static constexpr int A = M::amount;



    typedef MoveVehicle<GameBoard<B>, Ro, Co, Dir, A> movedB;
    static constexpr bool result = CheckSolution<typename movedB::board, List<Rest...>>::result;


};



#endif //OOP5_RUSHHOUR_H
