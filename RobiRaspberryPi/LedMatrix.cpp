#include "LedMatrix.h"

LedMatrix::LedMatrix(int _din, int _clk, int _cs, int _amount, int _resolution)
{
	din = _din;
	clk = _clk;
	cs = _cs;
	amount = _amount;
	resolution = _resolution;

	lc = LedControl(din, clk, cs, amount);

	for (int i = 0; i < amount; i++)
	{
		lc.shutdown(i, false);
		lc.setIntensity(i, 1);
		lc.clearDisplay(i);
	}
}

void LedMatrix::ChangeFace(char _face[])
{
	for (int i = 0; i < resolution; i++)
	{
		lc.setRow(0, i, _face[i]);
		lc.setRow(1, i, _face[i + resolution]);
		lc.setRow(2, i, _face[i + (resolution * 2)]);
		lc.setRow(3, i, _face[i + (resolution * 3)]);
	}
}
