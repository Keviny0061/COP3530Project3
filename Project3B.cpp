// Project3B.cpp : This file contains the 'main' function. Program execution begins and ends there.
#include<cstdlib>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <sstream>
#include <cstdlib>
#include <algorithm>
#include <unordered_set>
#include <map>
#include <dos.h>
#include <chrono>
#include <thread>
#include "functions.h"
using namespace std::chrono;

int main()
{
    intro();

    getData("Jan_Mar_2023_Alachua_County_Sheriff's_Office_report.csv");
    getData("Oct_Dec_2022_Alachua_County_Sheriff's_Office_report.csv");
    getData("Jul_Sep_2022_Alachua_County_Sheriff's_Office_report (1).csv");
    getData("Apr_Jun_2022_Alachua_County_Sheriff's_Office_report.csv");


    int rows = 0;
    int validCrimes = 0;
    vector <string> typesOfCrimes;
    for (int i = 1; i < 57000; i++) { //215000

        if (find(typesOfCrimes.begin(), typesOfCrimes.end(), content[i][9]) != typesOfCrimes.end()) //https://stackoverflow.com/questions/6277646/in-c-check-if-stdvectorstring-contains-a-certain-value
        {

        }
        else {
            typesOfCrimes.push_back(content[i][9]);
        }
    }

    for (int i = 1; i < 20000; i++) { //210000
        if (content[i][7] != "." && find(typesOfCrimes.begin(), typesOfCrimes.end(), content[i][9]) != typesOfCrimes.end()) { //if the crime has a known post code and type of crime

            Crime crime;
            crime.date = content[i][1];
            crime.time = content[i][2];
            crime.crime = getCrimeType(content[i][9]);
            crime.postalCode = stoi(content[i][7]);
            if (crime.crime == "False calls/unknown/non crimes") {
                crime.weight = weightOfNonCrimes;
            }
            else if (crime.crime == "Sexual charges") {
                crime.weight = weightOfSexual;
            }
            else if (crime.crime == "Drug crimes") {
                crime.weight = weightOfDrug;
            }
            else if (crime.crime == "Vehicle related crimes") {
                crime.weight = weightOfVehicle;
            }
            else if (crime.crime == "Theft") {
                crime.weight = weightOfTheft;
            }
            else if (crime.crime == "Children related") {
                crime.weight = weightOfChildren;
            }
            else if (crime.crime == "Physical violence") {
                crime.weight = weightOfPhysical;
            }
            else if (crime.crime == "Mental illness/Dangerous people") {
                crime.weight = weightOfMental;
            }
            else if (crime.crime == "Trespassing/disturbing peace") {
                crime.weight = weightOfTrepassing;
            }
            else {
                crime.weight = 0;
            }
            toSort.push_back(crime);
        }
    }

    vector <Crime> toSort2 = toSort; //Copies vector before sort for Merge sort
    vector <Crime> toSort3 = toSort; //Copies vector before sort for c++ inbuilt sort algo

    //execution time of quick sort
    auto begin = high_resolution_clock::now(); //https://www.geeksforgeeks.org/measure-execution-time-function-cpp/
    quicksort(toSort, 0, toSort.size() - 1);
    auto end = high_resolution_clock::now();
    auto Qtime = duration_cast<microseconds>(end - begin);
    cout << "Quick Sort Completed in: " << (double)Qtime.count() / 1000 << " milliseconds\n\n";

    //execution time of merge sort
    auto begin1 = high_resolution_clock::now(); //https://www.geeksforgeeks.org/measure-execution-time-function-cpp/
    mergeSort(toSort2, 0, toSort2.size() - 1);
    auto end1 = high_resolution_clock::now();
    auto Mtime = duration_cast<microseconds>(end1 - begin1);
    cout << "Merge Sort Completed in: " << (double)Mtime.count()/1000 << " milliseconds\n\n";

    //execution time of C++ inbuilt Sort Algorithim
    auto begin2 = high_resolution_clock::now(); //https://www.geeksforgeeks.org/measure-execution-time-function-cpp/
    sort(toSort3.begin(), toSort3.end());
    auto end2 = high_resolution_clock::now();
    auto sortTime = duration_cast<microseconds>(end2 - begin2);
    cout << "C++ inbuilt Sort Algorithim Completed in: " << (double)sortTime.count() / 1000 << " milliseconds\n\n";

    //outputs the fastest sort and how much faster relative to the other two
    comparison((double)Mtime.count()/1000, (double)Qtime.count()/1000, (double)sortTime.count()/1000);

    //    cout << "starting merge sort";
    //    mergeSort(toSort2, 0, toSort.size()-1);
    //    for(int i = 0; i<toSort2.size();i++)
    //    {
    //        cout << toSort2[i].postalCode;
    //    }

    map<int, vector<int>> dangerOfPostalCode;

    for (int i = 0; i < toSort.size(); i++) {
        //if (i % 1000 == 999) {
        //cout << "added " << i << endl;
        //}
        if (dangerOfPostalCode.find(toSort[i].postalCode) == dangerOfPostalCode.end()) { //https://stackoverflow.com/questions/1939953/how-to-find-if-a-given-key-exists-in-a-c-stdmap
            dangerOfPostalCode.insert({ toSort[i].postalCode,{toSort[i].weight} });
        }
        else {
            dangerOfPostalCode[toSort[i].postalCode].push_back(toSort[i].weight);
        }
    }

    std::vector<std::pair<int, double>> averages;
    for (const auto& entry : dangerOfPostalCode) { //this finds the average crime weight for each zipcode and prints it out
        int postalCode = entry.first;
        const std::vector<int>& dangerLevels = entry.second;
        double sum = 0;
        for (int dangerLevel : dangerLevels) {
            sum += dangerLevel;
        }
        double average = sum / dangerLevels.size();
        averages.push_back({ postalCode, average });
    }
    std::sort(averages.begin(), averages.end(), [](const auto& a, const auto& b) {
        return a.second < b.second;
        });
    cout << "Postal Codes in terms of ascending danger level: \n";
    cout << "---------------------------------------------------\n";
    for (const auto& pair : averages) {
        std::cout << "Postal code " << pair.first << " has an average danger level of " << pair.second << std::endl;
    }
    return 0;
}
