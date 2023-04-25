// Project3B.cpp : This file contains the 'main' function. Program execution begins and ends there.
//
#include<cstdlib>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <sstream>
#include <cstdlib>
#include <algorithm>
#include <unordered_set>
using namespace std;

vector<vector<string>> content; //this is where all the information is stored
vector<string> row; //this represents all the information in each row of the csv file

void print(std::vector <string> const& a) { //this is a test to print out vectors
	std::cout << "The vector elements are : ";

	for (int i = 0; i < a.size(); i++)
		std::cout << a.at(i) << endl;
}


void getData(string filename) { //push all the data from the file into our vector
	fstream file(filename, ios::in);
	string line;
	string word;
	if (file.is_open()) {
		while (getline(file, line))
		{
			row.clear();

			stringstream str(line);
			bool bruh = file.eof();

			while (getline(str, word, ',')) {
				row.push_back(word);
			}
			content.push_back(row);

		}
	}
}

string getCrimeType( string crimeCode) {
	fstream new_file;//https://www.scaler.com/topics/cpp-read-file-line-by-line/
	new_file.open("xyz.txt", ios::in);
	int counter = 0;
	if (new_file.is_open()) {
		string crimeType;
		while (getline(new_file, crimeType)) {
			if (crimeType == crimeCode) {
				cout << counter << endl;
				break;
			}
			else {
				counter++;
			}
		}

		// Close the file object.
		new_file.close();
	}
	if (counter < 40) {
		string returnInfo = "False calls / unknown / non crimes";
		return returnInfo;
	}
	else {
		string returnInfo = "Not included";
		return returnInfo;
	}
}
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

	ofstream MyFile("filename.txt");
	for (int i = 1; i < 210000; i++) {
		if (content[i][7] != "." && find(typesOfCrimes.begin(), typesOfCrimes.end(), content[i][9]) != typesOfCrimes.end()){ //if the crime has a known post code and type of crime
			MyFile << "Date: " << content[i][1] << " time: " << content[i][2] << endl;
			MyFile << "postal code: " << content[i][7] << " type of crime: " << getCrimeType(content[i][9]) << endl;
		}
	}
    return 0;
}

// Run program: Ctrl + F5 or Debug > Start Without Debugging menu
// Debug program: F5 or Debug > Start Debugging menu

// Tips for Getting Started: 
//   1. Use the Solution Explorer window to add/manage files
//   2. Use the Team Explorer window to connect to source control
//   3. Use the Output window to see build output and other messages
//   4. Use the Error List window to view errors
//   5. Go to Project > Add New Item to create new code files, or Project > Add Existing Item to add existing code files to the project
//   6. In the future, to open this project again, go to File > Open > Project and select the .sln file
