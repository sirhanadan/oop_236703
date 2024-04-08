//
// Created by asus on 02/04/2024.
//

#ifndef OOP5_UTILITIES_H
#define OOP5_UTILITIES_H




template<bool B, typename Tr, typename Fl>
struct Conditional;

template<typename Tr, typename Fl>
struct Conditional<true, Tr, Fl>
{
    typedef Tr value;
};

template<typename Tr, typename Fl>
struct Conditional<false, Tr, Fl>
{
    typedef Fl value;
};


template<bool B, int Tr, int Fl>
struct ConditionalInteger;

template<int Tr, int Fl>
struct ConditionalInteger<true, Tr, Fl>
{
    static constexpr int value = Tr;
};

template<int Tr, int Fl>
struct ConditionalInteger<false, Tr, Fl>
{
    static constexpr int value = Fl;
};








#endif //OOP5_UTILITIES_H
