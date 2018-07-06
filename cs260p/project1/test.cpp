#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include "dSelect.h"
#include "quickSelect.h"
#include "timer.h"
/*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  */
/*	!!!!! if you want to run this project in eclipse IDE, please comment blow 3 "include" statements !!!!!   */
/*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  */
#include "dSelect.cpp"
#include "quickSelect.cpp"
#include "timer.cpp"




double dshrandom( long input ) {
/*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  */
/*	Random Number Generator					*/
/*		first called with seed				*/
/*		thereafter called with 0			*/
/*	Based on code appearing in				*/
/*	"Random number generators: good ones are hard to find"	*/
/*		by Stephen Park and Keith Miller		*/
/*		CACM 31 (Oct 1988) 1192-1201.			*/
/*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  */
	static long seed = 3125;
	static long a = 16807;
	static long m = 2147483647;
	static long q = 127773;      /* m div a */
	static long r = 2836;        /* m mod a */
	long lo, hi, test;

	if (input>0) seed = input;
	hi = seed/q;
	lo = seed - (hi*q);
	test = a*lo - r*hi;
	if (test>0) seed = test;
	else        seed = test+m;
				/* seed equally in range 0...2^31 -1  */
	return( ((double)seed)/(double)m );
}

int main() {
	int length = 100000;
	int k = length - length/6;
	double result;
	double time;
	vector<double> array (length);

	//generate a array
	dshrandom(length);
	for(int i = 1; i < length; i++) {
		array[i] = dshrandom(0);
	}

	//use quick select
	start_timer();
	result = quickSelect(k, array);
	time = elapsed_time();
	cout << "Array length = " << length << ", k = " << k << endl << endl;
	cout << "Quick Select: " << endl;
	cout << "The k'th smallest element = " << result << endl;
	cout << "Elapsed_time = " << time << endl << endl;

	//use d3 select
	start_timer();
	result = dSelect(3, k, array);
	time = elapsed_time();
	cout << "DSelect (group of 3): " << endl;
	cout << "The k'th smallest element = " << result << endl;
	cout << "Elapsed_time = " << time << endl << endl;

	//use d5 select
	start_timer();
	result = dSelect(5, k, array);
	time = elapsed_time();
	cout << "DSelect (group of 5): " << endl;
	cout << "The k'th smallest element = " << result << endl;
	cout << "Elapsed_time = " << time << endl << endl;

	//use d7 select
	start_timer();
	result = dSelect(7, k, array);
	time = elapsed_time();
	cout << "DSelect (group of 7): " << endl;
	cout << "The k'th smallest element = " << result << endl;
	cout << "Elapsed_time = " << time << endl << endl;

	//use sort - select
	start_timer();
	sort(array.begin(), array.end());
	result = array[k-1];
	time = elapsed_time();
	cout << "Sort, then select: " << endl;
	cout << "The k'th smallest element = " << result << endl;
	cout << "Elapsed_time = " << time << endl;

	return 0;
}
