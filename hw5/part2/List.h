//
// Created by asus on 02/04/2024.
//

#ifndef OOP5_LIST_H
#define OOP5_LIST_H



template<typename... Rest>
struct List;

template<>
struct List<>{
    static constexpr int size = 0;
};

template<typename T, typename... Rest>
struct List<T, Rest...>{
    typedef T head;
    typedef List<Rest...> next;
    static constexpr int size = sizeof...(Rest) + 1;
};

/* changes made to List.h:
 * removed struct List<Rest...>
 * */


template <typename T, typename Rest>
struct PrependList;

template <typename T, typename... Rest>
struct PrependList<T, List<Rest...>>
{
    typedef List<T, Rest...> list;
};


template <int N, typename T>
struct GetAtIndex;

template <typename T, typename... Rest>
struct GetAtIndex< 0, List<T, Rest...>>
{
    typedef T value;
};

template <int N, typename T, typename... Rest>
struct GetAtIndex< N, List<T, Rest...>>
{
    typedef typename GetAtIndex< N-1, List<Rest...>>::value value;
};


template <int N, typename newT, typename T>
struct SetAtIndex;

template<typename newT, typename T, typename... Rest>
struct SetAtIndex<0, newT, List<T, Rest...>>
{

    typedef List<newT, Rest...> list;
};


template<int N, typename newT, typename T, typename... Rest>
struct SetAtIndex<N, newT, List<T, Rest...> >
{
    typedef typename
    PrependList<
            T,
            typename SetAtIndex<N-1, newT, List<Rest...>>::list
    >::list list;
};











#endif //OOP5LIST_H
