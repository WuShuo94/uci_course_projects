#include "dSelect.h"

double dSelect(int d, int k, vector<double> &input) {
	if(input.size() < 1 || (k > input.size()))
		return -1;
	if(input.size() == 1)
		return input[0];

	int basis;

	if(d == 3) {
		basis = 20;
	} else if(d == 5){
		basis = 50;
	} else if(d == 7){
		basis = 30;
	} else {
		return -1;
	}


	if(input.size() < 1 || (k > input.size()))
		return -1;
	if(input.size() < basis) {
		sort(input.begin(), input.end());
		return input[k-1];
	}

	vector< double > medians;
	vector< double > temp(d);
	double m;
	for(int i = 0; i < (input.size()/d); i++) {
		for(int j = 0; j < d; j++) {
			temp[j] = input[i*d+j];
		}
		sort(temp.begin(), temp.end());
		medians.push_back(temp.at(d/2));
	}
	m = dSelect(d, medians.size()/2, medians);

	vector<double> L;
	vector<double> E;
	vector<double> G;
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
		return dSelect(d, k, L);
	} else if(k <= (L.size() + E.size())) {
		return E[0];
	} else {
		return dSelect(d, k- (L.size() + E.size()), G);
	}

}
