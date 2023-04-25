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
#include "functions.h"
using namespace std;

class Crime {
public:
	string date;
	string time;
	string crime;
	int postalCode;
	int weight;
	friend std::ostream& operator<<(std::ostream& os, const Crime& p) //https://stackoverflow.com/questions/43213902/c-output-all-members-in-a-object
	{
		return os << "("
			<< p.date << ", "
			<< p.time << ", "
			<< p.crime << ", "
			<< p.postalCode << ", "
			<< p.weight
			<< ")";
	}

};
int main()
{
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
	print(typesOfCrimes);
	cout << endl;
	cout << typesOfCrimes.size();
	cout << endl;
	for (int i = 1; i < 210000; i++) {
		if (find(typesOfCrimes.begin(), typesOfCrimes.end(), content[i][9]) != typesOfCrimes.end() && content[i][7] != ".") //https://stackoverflow.com/questions/6277646/in-c-check-if-stdvectorstring-contains-a-certain-value
		{
			validCrimes++;
		}
	}
	cout << validCrimes << endl;
	vector <Crime> toSort;
	ofstream MyFile("crimeSummary.txt");
	for (int i = 1; i < 210000; i++) {
		if (content[i][7] != "." && find(typesOfCrimes.begin(), typesOfCrimes.end(), content[i][9]) != typesOfCrimes.end()){ //if the crime has a known post code and type of crime
			/*MyFile << "Date: " << content[i][1] << " time: " << content[i][2] << endl;
			MyFile << "postal code: " << content[i][7] << " type of crime: " << getCrimeType(content[i][9]) << endl;
			if (i % 10000 == 9999) {
				cout << "finished 10 thousand" << endl;
			}*/
			Crime crime;
			crime.date = content[i][1];
			crime.time = content[i][2];
			crime.crime = getCrimeType(content[i][9]);
			crime.postalCode = stoi(content[i][7]);
			crime.weight = 0;
			toSort.push_back(crime);
		}
	}
	cout << "this is the first value in the vector that holds the class" << toSort[1] << endl;
    return 0;
}
