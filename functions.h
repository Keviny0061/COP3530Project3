#include <string>
#include <vector> 
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

string getCrimeType(string crimeCode) {
	fstream new_file;//https://www.scaler.com/topics/cpp-read-file-line-by-line/
	new_file.open("crimeClassification.txt", ios::in);
	int counter = 0;
	if (new_file.is_open()) {
		string crimeType;
		while (getline(new_file, crimeType)) {
			if (crimeType == crimeCode) {
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
	else if (counter < 50) {
		string returnInfo = "Sexual charges";
		return returnInfo;
	}
	else if (counter < 58) {
		string returnInfo = "Drug crimes";
		return returnInfo;
	}
	else if (counter < 77) {
		string returnInfo = "Vehicle related crimes";
		return returnInfo;
	}
	else if (counter < 95) {
		string returnInfo = "Theft";
		return returnInfo;

	}
	else if (counter < 106) {
		string returnInfo = "Children Related";
		return returnInfo;
	}
	else if (counter < 120) {
		string returnInfo = "Physical violence";
		return returnInfo;
	}
	else if (counter < 134) {
		string returnInfo = "Mental illness/Dangerous people";
		return returnInfo;
	}
	else {
		string returnInfo = "Trespassing/disturbing peace";
		return returnInfo;
	}
}