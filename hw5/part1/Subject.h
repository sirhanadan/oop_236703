//
// Created by asus on 02/04/2024.
//

#ifndef OOP5_SUBJECT_H
#define OOP5_SUBJECT_H


#include <vector>
#include "Observer.h"
#include "OOP5EventException.h"

template<typename T>
class Subject{
private:
    std::vector<Observer<T>*> observers;

    int getIndex(Observer<T>& item)
    {
        size_t s = this->observers.size();

        for(unsigned int i = 0; i < s; i++)
        {
            if(this->observers[i] == &item)
                return i;
        }

        return -1;
    }

public:
    Subject() = default;
    void notify(const T& par){
        size_t s = this->observers.size();
        for(unsigned int i=0; i<s; i++){
            this->observers[i]->handleEvent(par);
        }
    }

    void addObserver(Observer<T>& obs)
    {
        unsigned int index = this->getIndex(obs);
        if(index == -1){
            this->observers.push_back(&obs);
        }else{
            throw ObserverAlreadyKnownToSubject();
        }
    }

    void removeObserver(Observer<T>& obs)
    {
        unsigned int index = this->getIndex(obs);
        if(index == -1){
            throw ObserverUnknownToSubject();
        }else{
            this->observers.erase(this->observers.begin() + index);
        }
    }

    Subject<T>& operator+=(Observer<T>& obs){
        this->addObserver(obs);
        return *this;
    }

    Subject<T>& operator-=(Observer<T>& obs){
        this->removeObserver(obs);
        return *this;
    }

    Subject<T>& operator()(const T& par){
        this->notify(par);
        return *this;
    }

};


















#endif //OOP5_SUBJECT_H
