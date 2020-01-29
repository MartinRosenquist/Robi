#ifndef LedMatrix_h
#define LedMatrix_h

#include "LedControl.h"

class LedMatrix
{
public:
	LedMatrix(int _din, int _clk, int _cs, int _amount, int _resolution);
	void ChangeFace(char _face[]);
private:
	int din;
	int clk;
	int cs;
	int amount;
	int resolution;
	LedControl lc = LedControl(din, clk, cs, amount);
};

#endif // !LEDMATRIX_H
