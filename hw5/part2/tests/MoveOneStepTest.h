//
// Created by asus on 05/04/2024.
//

#ifndef OOP5_24_MOVEONESTEPTEST_H
#define OOP5_24_MOVEONESTEPTEST_H

#include "../MoveVehicle.h"


// _ _ _ X _ _ _
void OneMoveTest() {
    typedef List< List < BoardCell< EMPTY , RIGHT , 0 >, BoardCell< EMPTY , RIGHT , 0 >, BoardCell< EMPTY , RIGHT , 0>, BoardCell< X , RIGHT , 1 >, BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>>>
    gameBoard2;

    typedef MoveOne< 0, 3, RIGHT, gameBoard2>::list b1;
    static_assert(b1::head::head::type == EMPTY, "Fail 1");
    static_assert(b1::head::next::head::type == EMPTY, "Fail 2");
    static_assert(b1::head::next::next::head::type == EMPTY, "Fail 3");
    static_assert(b1::head::next::next::next::head::type == EMPTY, "Fail 4");
    static_assert(b1::head::next::next::next::next::head::type == X, "Fail 5"); //Check that it moved
    static_assert(b1::head::next::next::next::next::head::length == 1, "Fail 5");
    static_assert(b1::head::next::next::next::next::next::head::type == EMPTY, "Fail 6");
    static_assert(b1::head::next::next::next::next::next::next::head::type == EMPTY, "Fail 7");

    typedef List<
            List < BoardCell< O , DOWN , 3>, BoardCell< EMPTY , RIGHT , 0>,      BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< H , DOWN , 3>,      BoardCell< EMPTY , RIGHT , 0> >,
            List < BoardCell< O , DOWN , 3>, BoardCell< EMPTY , RIGHT , 0>,      BoardCell< A , RIGHT , 2>,     BoardCell< A , LEFT , 2>,      BoardCell< H , DOWN , 3>,      BoardCell< EMPTY , RIGHT , 0> >,
            List < BoardCell< O , UP , 3>, BoardCell< EMPTY , RIGHT , 0>,        BoardCell< EMPTY , RIGHT , 0>, BoardCell< X , LEFT , 2>,      BoardCell< X , LEFT , 2>,      BoardCell< EMPTY , RIGHT , 0> >,
            List < BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0> >,
            List < BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< B , DOWN , 2>,      BoardCell< P , RIGHT , 3>,     BoardCell< P , RIGHT , 3>,     BoardCell< P , LEFT , 3> >,
            List < BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< B , UP , 2>,        BoardCell< EMPTY , RIGHT , 0>, BoardCell< C , RIGHT , 2>,     BoardCell< C , LEFT , 2> >
    > gameBoard3;

    typedef MoveOne<2, 4, LEFT, gameBoard3>::list b11; // Valid Move left
    typedef MoveOne<2, 3, LEFT, b11>::list b2; // Valid Move left
    static_assert(b2::next::next::head::head::type == O, "Fail 8");
    static_assert(b2::next::next::head::next::head::type == X, "Fail 9");
    static_assert(b2::next::next::head::next::head::length == 2, "Fail 9");
    static_assert(b2::next::next::head::next::next::head::type == X, "Fail 10");
    static_assert(b2::next::next::head::next::next::next::head::type == EMPTY, "Fail 11");
    static_assert(b2::next::next::head::next::next::next::next::head::type == EMPTY, "Fail 12");
    static_assert(b2::next::next::head::next::next::next::next::next::head::type == EMPTY, "Fail 13");



}

void MoveAuxTest() {
    typedef GameBoard< List<
            List < BoardCell< O , DOWN , 3>, BoardCell< EMPTY , RIGHT , 0>,      BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< H , DOWN , 3>,      BoardCell< EMPTY , RIGHT , 0> >,
            List < BoardCell< O , DOWN , 3>, BoardCell< EMPTY , RIGHT , 0>,      BoardCell< A , RIGHT , 2>,     BoardCell< A , LEFT , 2>,      BoardCell< H , DOWN , 3>,      BoardCell< EMPTY , RIGHT , 0> >,
            List < BoardCell< O , UP , 3>, BoardCell< EMPTY , RIGHT , 0>,        BoardCell< EMPTY , RIGHT , 0>, BoardCell< X , LEFT , 2>,      BoardCell< X , LEFT , 2>,      BoardCell< EMPTY , RIGHT , 0> >,
            List < BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0> >,
            List < BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< B , DOWN , 2>,      BoardCell< P , RIGHT , 3>,     BoardCell< P , RIGHT , 3>,     BoardCell< P , LEFT , 3> >,
            List < BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< B , UP , 2>,        BoardCell< EMPTY , RIGHT , 0>, BoardCell< C , RIGHT , 2>,     BoardCell< C , LEFT , 2> >
    > > gameBoard;

    typedef MoveVehicle_aux<gameBoard, 2, 4, LEFT, 2>::board b2; // Valid Move left
    static_assert(b2::next::next::head::head::type == O, "Fail 8");
    static_assert(b2::next::next::head::next::head::type == X, "Fail 9");
    static_assert(b2::next::next::head::next::head::length == 2, "Fail 9");
    static_assert(b2::next::next::head::next::next::head::type == X, "Fail 10");
    static_assert(b2::next::next::head::next::next::next::head::type == EMPTY, "Fail 11");
    static_assert(b2::next::next::head::next::next::next::next::head::type == EMPTY, "Fail 12");
    static_assert(b2::next::next::head::next::next::next::next::next::head::type == EMPTY, "Fail 13");

}

















#endif //OOP5_24_MOVEONESTEPTEST_H
