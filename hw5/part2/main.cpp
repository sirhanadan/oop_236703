#include <iostream>
#include "tests/ListTest.h"
#include "tests/ConditionalTest.h"
#include "tests/BoardCellTest.h"
#include "tests/GameBoardTest.h"
#include "tests/MoveOneStepTest.h"
#include "tests/MoveVehicleTest.h"
#include "tests/CheckWinTest.h"
#include "tests/RushHourTest.h"
#include "tests/FailTest.h"

int main() {
    std::cout << "Hello, World!" << std::endl;
    testList();
    testConditional();
    testBoardCell();
    testGameBoard();
    MoveAuxTest();
    OneMoveTest();
    testMove();
    testMoveVehicle();
    checkWinTest();
    testRushHour();
    testFail();

    return 0;
}
