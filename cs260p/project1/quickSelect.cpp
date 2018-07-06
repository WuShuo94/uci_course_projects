#include "quickSelect.h"

double quickSelect(int k, vector<double> input) {
	if(input.size() < 1 || (k > input.size()))
		return -1;
	if(input.size() == 1)
		return input[0];
	vector<double> L;
	vector<double> E;
	vector<double> G;
	int indexM;
	indexM = rand() % input.size();
	double m = input[indexM];
	for(int i = 0; i < input.size(); i++) {
		if(input[i] < m) {
			L.push_back(input[i]);
		} else if(input[i] == m) {
			E.push_back(input[i]);
		} else {
			G.push_back(input[i]);
		}
	}

	if(k <= L.size()) {
		return quickSelect(k, L);
	} else if(k <= (L.size() + E.size())) {
		return m;
	} else {
		return quickSelect(k- (L.size() + E.size()), G);
	}
}
