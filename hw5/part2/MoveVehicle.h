//
// Created by asus on 05/04/2024.
//

#ifndef OOP5_MOVEVEHICLE_H
#define OOP5_MOVEVEHICLE_H


#include "CellType.h"
#include "Direction.h"
#include "GameBoard.h"
#include "TransposeList.h"

template<CellType Type, Direction Dir, int Amount>
struct Move
{
    static_assert(Type != EMPTY, "in Move : cell is empty"); //changed the message from baaaaaaaaaaad
    static constexpr CellType type = Type;
    static constexpr Direction direction = Dir;
    static constexpr int amount = Amount;
};


////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*************  lets divide and concur, take one small step at a time  ************/

/**                      1. find the furthest left end of the car            */

//instead of searching the entire matrix, lets search a single row
//for the up and down use transpose

//finds the index at the furthest left
template<typename Row, int ColNum> //note that Row is an entire list and ColNum is just an index in that list
struct Find_Car_Left_Helper;


//in case the car is from 5 to 9 and we were given the index 7, we should return 5 because the car starts there
template<typename... T, int ColNum> //T - the type of the elements in a row
struct Find_Car_Left_Helper<List<T...> , ColNum>{
    typedef typename GetAtIndex<ColNum,List<T...>>::value currCell;
    typedef typename GetAtIndex<ColNum-1,List<T...>>::value prevCell;
    static constexpr bool startOfCar = (currCell::type != prevCell::type);
    //if this is the start of the car then this is the index we're looking for
    //otherwise check the prev index
    static constexpr int index = ConditionalInteger<(startOfCar) ,ColNum,Find_Car_Left_Helper< List<T...>, ColNum-1>::index> ::value;

};

template<typename... T>
struct Find_Car_Left_Helper<List<T...> , 0>{
    static constexpr int index = 0;
};

/**                      2. find the furthest right end of the car            */

//finds the index at the furthest right
template<typename Row, int ColNum, int Border> //note that Row is an entire list and ColNum is just an index in that list
struct Find_Car_Right_Helper;


template<typename... T, int ColNum, int Border> //T - the type of the elements in a row
struct Find_Car_Right_Helper<List<T...> , ColNum, Border>{

    typedef typename GetAtIndex<ColNum,List<T...>>::value currCell;
    typedef typename GetAtIndex<ColNum+1,List<T...>>::value nextCell;
    static constexpr bool endOfCar = (currCell::type != nextCell::type);
    //if this is the end of the car then this is the index we're looking for
    //otherwise check the next index
    static constexpr int index = ConditionalInteger<(endOfCar) ,ColNum,Find_Car_Right_Helper< List<T...>, ColNum+1, Border>::index>::value;

};

//reached the end, there is no next
template<typename... T, int Border>
struct Find_Car_Right_Helper<List<T...> , Border, Border>{
    static constexpr int index = Border;
};

/**                           3. find the ends of the car                 */
template< int ColNum, typename... T>
struct Find_Car;

template< int ColNum,typename... T>
struct Find_Car< ColNum, List<T...> >{
    static constexpr int start = Find_Car_Left_Helper<List<T...>, ColNum>::index;
    static constexpr int end = Find_Car_Right_Helper<List<T...>, ColNum, List<T...>::size -1 >::index; // bc index starts at 0
};


/**                  4. given the car and the row list, move it an entire step          */

template<typename RowList, int From, int To, Direction Dir>
struct MoveGivenCarOneStep;


template<typename RowList, int From, int To>
struct MoveGivenCarOneStep< RowList,  From,  To, RIGHT>{
    typedef BoardCell<EMPTY, RIGHT, 0> emptyCell;
    typedef typename GetAtIndex<  (To -1), RowList>::value cell;
    typedef typename SetAtIndex<To, cell, RowList>::list newRow;
    typedef typename SetAtIndex<From, emptyCell , newRow>::list list;

};

template<typename RowList, int From, int To>
struct MoveGivenCarOneStep< RowList,  From,  To, LEFT>{
    typedef BoardCell<EMPTY, RIGHT, 0> emptyCell;
    typedef typename GetAtIndex<  (From + 1), RowList>::value cell;
    typedef typename SetAtIndex<From, cell, RowList>::list newRow;
    typedef typename SetAtIndex<To, emptyCell , newRow>::list list;
};

template<typename RowList, int index, Direction Dir>
struct MoveCarOneStep;


template<typename RowList, int index>
struct MoveCarOneStep<RowList, index, RIGHT>{
    static constexpr int start = Find_Car< index, RowList>::start;
    static constexpr int end = Find_Car< index, RowList>::end;

    static constexpr int Border = RowList::size;
    static_assert((start>=0 && start < Border -1), "in MoveCarOneStep: car coordinates out of range");

    typedef typename GetAtIndex<end+1, RowList>::value nextCell;

    static_assert((nextCell::type == EMPTY), "in MoveCarOneStep: next cell is not empty");

    typedef typename MoveGivenCarOneStep<RowList, start, end + 1, RIGHT>::list list;

};


template<typename RowList, int index>
struct MoveCarOneStep<RowList, index, LEFT>{
    static constexpr int start = Find_Car< index, RowList>::start;
    static constexpr int end = Find_Car<index, RowList>::end;

    static constexpr int Border = RowList::size;
    static_assert((start>=0 && start < Border -1), "in MoveCarOneStep: car coordinates out of range");

    typedef typename GetAtIndex<start-1, RowList>::value nextCell;

    static_assert((nextCell::type == EMPTY), "in MoveCarOneStep: next cell is not empty");

    typedef typename MoveGivenCarOneStep<RowList, start -1 , end, LEFT>::list list;

};

//T are lists of rows
template< int Ro, int Co, Direction Dir, typename ...T>
struct MoveOne;


template<typename ...T, int Ro, int Co, Direction Dir>
struct MoveOne< Ro,Co, Dir, List<T...>>{
    typedef typename GetAtIndex<Ro, List<T...>>::value rowList;
    typedef typename MoveCarOneStep<rowList, Co, Dir>::list newRow;
    typedef typename SetAtIndex<Ro, newRow, List<T...>>::list list;
};


template<typename B, int R1, int C1, Direction Dir, int A>
struct MoveVehicle_aux;

template<typename B, int R1, int C1, Direction Dir, int A>
struct MoveVehicle_aux<GameBoard<B>,R1,C1,Dir,A>{
    typedef typename GameBoard<B>::board mainList;
    typedef typename MoveOne<R1, C1, Dir, mainList>::list newMainList;
    static constexpr int nextCol = ConditionalInteger<(Dir == RIGHT), C1+1, C1-1>::value;
    typedef typename MoveVehicle_aux<GameBoard<newMainList>, R1, nextCol, Dir, A-1>::board board;

};

template<typename B, int R1, int C1, Direction Dir>
struct MoveVehicle_aux<GameBoard<B>,R1,C1,Dir,1>{
    typedef typename MoveOne<R1, C1, Dir, B>::list board;
};


template<typename B, int R1, int C1, Direction Dir>
struct MoveVehicle_aux<GameBoard<B>,R1,C1,Dir,0>{
    typedef typename GameBoard<B>::board board;
};

template<typename B, int Ro, int Co, Direction Dir, int Amount>
struct MoveVehicle;


template<typename B, int Ro, int Co,int Amount>
struct MoveVehicle<GameBoard<B>, Ro, Co, RIGHT, Amount>{

    static constexpr int len = GameBoard<B>::length; //num of rows
    static constexpr int wid = GameBoard<B>::width; //num of cols

    static_assert(0 <= Ro, "in MoveVehicle: Ro < 0 ");
    static_assert(len > Ro, "in MoveVehicle: Ro > len ");

    static_assert(0 <= Co, "in MoveVehicle: Co < 0 ");
    static_assert(wid > Co, "in MoveVehicle: Co > wid ");

    typedef typename GetAtIndex<Ro, B>::value rowList;
    typedef typename GetAtIndex<Co, rowList>::value cell;

    static_assert(cell::type != EMPTY, "in MoveVehicle: given cell is empty");
    static_assert(cell::direction == LEFT || cell::direction == RIGHT, "in MoveVehicle: given cell cant move in given direction ");

    typedef typename MoveVehicle_aux<GameBoard<B>,Ro, Co, RIGHT, Amount>::board mainList;
public:
    typedef GameBoard<mainList> board;

};

template<typename B, int Ro, int Co, int Amount>
struct MoveVehicle<GameBoard<B>, Ro, Co, LEFT, Amount>{
    static constexpr int len = GameBoard<B>::length; //num of rows
    static constexpr int wid = GameBoard<B>::width; //num of cols

    static_assert(0 <= Ro, "in MoveVehicle: Ro < 0 ");
    static_assert(len > Ro, "in MoveVehicle: Ro > len ");

    static_assert(0 <= Co, "in MoveVehicle: Co < 0 ");
    static_assert(wid > Co, "in MoveVehicle: Co > wid ");

    typedef typename GetAtIndex<Ro, B>::value rowList;
    typedef typename GetAtIndex<Co, rowList>::value cell;

    static_assert(cell::type != EMPTY, "in MoveVehicle: given cell is empty");
    static_assert(cell::direction == LEFT || cell::direction == RIGHT, "in MoveVehicle: given cell cant move in given direction ");

    typedef typename MoveVehicle_aux<GameBoard<B>,Ro, Co, LEFT, Amount>::board mainList;
    typedef GameBoard<mainList> board;

};





template<typename B, int Ro, int Co, int Amount>
struct MoveVehicle<GameBoard<B>, Ro, Co, UP, Amount>{
    static constexpr int len = GameBoard<B>::length; //num of rows
    static constexpr int wid = GameBoard<B>::width; //num of cols

    static_assert(0 <= Ro, "in MoveVehicle: Ro < 0 ");
    static_assert(len > Ro, "in MoveVehicle: Ro > len ");

    static_assert(0 <= Co, "in MoveVehicle: Co < 0 ");
    static_assert(wid > Co, "in MoveVehicle: Co > wid ");

    typedef typename GetAtIndex<Ro, B>::value rowList;
    typedef typename GetAtIndex<Co, rowList>::value cell;

    static_assert(cell::type != EMPTY, "in MoveVehicle: given cell is empty");
    static_assert(cell::direction == UP || cell::direction == DOWN , "in MoveVehicle: given cell cant move in given direction ");

    typedef typename Transpose<B>::matrix rightToLeftBoard;

    typedef typename MoveVehicle_aux<GameBoard<rightToLeftBoard>,Co, Ro, LEFT, Amount>::board mainList;

    typedef typename Transpose<mainList>::matrix downToUpBoard;

    typedef GameBoard<downToUpBoard> board;

};





template<typename B, int Ro, int Co, int Amount>
struct MoveVehicle<GameBoard<B>, Ro, Co, DOWN, Amount>{
    static constexpr int len = GameBoard<B>::length; //num of rows
    static constexpr int wid = GameBoard<B>::width; //num of cols

    static_assert(0 <= Ro, "in MoveVehicle: Ro < 0 ");
    static_assert(len > Ro, "in MoveVehicle: Ro > len ");

    static_assert(0 <= Co, "in MoveVehicle: Co < 0 ");
    static_assert(wid > Co, "in MoveVehicle: Co > wid ");

    typedef typename GetAtIndex<Ro, B>::value rowList;
    typedef typename GetAtIndex<Co, rowList>::value cell;

    static_assert(cell::type != EMPTY, "in MoveVehicle: given cell is empty");
    static_assert(cell::direction == UP || cell::direction == DOWN , "in MoveVehicle: given cell cant move in given direction ");

    typedef typename Transpose<B>::matrix rightToLeftBoard;

    typedef typename MoveVehicle_aux<GameBoard<rightToLeftBoard>,Co, Ro, RIGHT, Amount>::board mainList;

    typedef typename Transpose<mainList>::matrix downToUpBoard;

    typedef GameBoard<downToUpBoard> board;

};






#endif //OOP5_MOVEVEHICLE_H
