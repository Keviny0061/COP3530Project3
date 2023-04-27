#include <string>
#include <vector> 
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

vector <Crime> toSort;
vector <Crime> toSort2 = toSort;
vector<vector<string>> content; //this is where all the information is stored
vector<string> row; //this represents all the information in each row of the csv file

std::chrono::seconds dura(1);
int weightOfNonCrimes;
int weightOfTrepassing;
int weightOfVehicle;
int weightOfTheft;
int weightOfDrug;
int weightOfPhysical;
int weightOfSexual;
int weightOfChildren;
int weightOfMental;
void intro() {
	cout << "*********************************************************************************************************" << endl;
	cout << "*               Hello! Our program is designed to help you know how safe where you live is!             *" << endl;
	cout << "* We have options for you to create a custom set of rules to judge how safe each zipcode in Alachua is! *" << endl;
	cout << "*                                                                                                       *" << endl;
	//std::this_thread::sleep_for(dura);

	cout << "*                                                                                                       *" << endl;
	cout << "*         We have sorted police reports in Alachua County for the past year and categorized them.       *" << endl;
	cout << "*                                         Here are the categories:                                      * " << endl;
	cout << "*                                     -------------------------------                                   *" << endl;
	cout << "*                       Non crimes, Trespassing/disturbing peace, Vehicle related crimes                *" << endl;
	//std::this_thread::sleep_for(dura);

	cout << "*                            Theft, Drug crimes, Physical violence, Sexual charges,                     *" << endl;;
	cout << "*                           Children related, and Mental illness / Dangerous people                     *" << endl;
	//std::this_thread::sleep_for(dura);

	cout << "*                                                                                                       *" << endl;
	cout << "*                   For example: If you weigh physical violence crimes as 4 and theft as 1              *" << endl;
	cout << "*           We will count every instance of physical violence as 4 times more weighted than theft.      *" << endl;
	cout << "*********************************************************************************************************" << endl;

	cout << endl;
	cout << "You will have the option to input a number for each crime. Based on what you believe is more important, you can weigh as you like." << endl;
	cout << "------------------------------------------------------------------------------------------------------------------------------------\n";

	cout << "What weight will you assign Non criminal related incidents?" << endl << "~";
	cin >> weightOfNonCrimes;

	cout << "What weight will you assign Trespassing related incidents?" << endl << "~";
	cin >> weightOfTrepassing;

	cout << "What weight will you assign Vehicle related incidents?" << endl << "~";
	cin >> weightOfVehicle;

	cout << "What weight will you assign Theft related incidents?" << endl << "~";
	cin >> weightOfTheft;

	cout << "What weight will you assign Drug related incidents?" << endl << "~";
	cin >> weightOfDrug;

	cout << "What weight will you assign Physical Violence related incidents?" << endl << "~";
	cin >> weightOfPhysical;

	cout << "What weight will you assign Sexual related incidents?" << endl << "~";
	cin >> weightOfSexual;

	cout << "What weight will you assign Children related incidents?" << endl << "~";
	cin >> weightOfChildren;

	cout << "What weight will you assign Mental Illness related incidents?" << endl << "~";
	cin >> weightOfVehicle;
	cout << endl;
}

int partition(vector<Crime>& values, int left, int right) {
	int pivotIndex = left + (right - left) / 2;
	int pivotValue = values[pivotIndex].postalCode;
	int i = left, j = right;
	int temp;
	while (i <= j) {
		while (values[i].postalCode < pivotValue) {
			i++;
		}
		while (values[j].postalCode > pivotValue) {
			j--;
		}
		if (i <= j) {
			temp = values[i].postalCode;
			values[i].postalCode = values[j].postalCode;
			values[j].postalCode = temp;
			i++;
			j--;
		}
	}
	return i;
}

void quicksort(vector<Crime>& values, int left, int right) {
	if (left < right) {
		int pivotIndex = partition(values, left, right);
		quicksort(values, left, pivotIndex - 1);
		quicksort(values, pivotIndex, right);
	}
}

void merge(vector<Crime>& arr, int leftIndex, int middleIndex, int rightIndex)
{
    int leftSize = middleIndex - leftIndex + 1;
    int rightSize = rightIndex - middleIndex;

    vector<Crime> leftArray(leftSize);
    vector<Crime> rightArray(rightSize);

    for (int i = 0; i < leftSize; i++)
        leftArray[i] = arr[leftIndex + i];
    for (int j = 0; j < rightSize; j++)
        rightArray[j] = arr[middleIndex + 1 + j];

    int i = 0;
    int j = 0;
    int k = leftIndex;

    while (i < leftSize && j < rightSize) {
        if (leftArray[i].postalCode <= rightArray[j].postalCode) {
            arr[k] = leftArray[i];
            i++;
        }
        else {
            arr[k] = rightArray[j];
            j++;
        }
        k++;
    }

    while (i < leftSize) {
        arr[k] = leftArray[i];
        i++;
        k++;
    }

    while (j < rightSize) {
        arr[k] = rightArray[j];
        j++;
        k++;
    }
}

void mergeSort(vector<Crime>& arr, int leftIndex, int rightIndex)
{
    if (leftIndex >= rightIndex) {
        return;
    }
    int middleIndex = leftIndex + (rightIndex - leftIndex) / 2;
    mergeSort(arr, leftIndex, middleIndex);
    mergeSort(arr, middleIndex + 1, rightIndex);
    merge(arr, leftIndex, middleIndex, rightIndex);
}

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
		string returnInfo = "False calls/unknown/non crimes";
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
		string returnInfo = "Children related";
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
