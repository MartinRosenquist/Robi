#include <wiringPi.h>
#include "LedMatrix.h"
#include "Faces.h"
#include <thread>
#include <chrono>

int main(void)
{
	wiringPiSetup();

	LedMatrix LM = LedMatrix(0, 2, 1, 4, 8);

	LM.ChangeFace(Happyface);
	std::this_thread::sleep_for(std::chrono::seconds(10));
	LM.Clear();
		
	return 0;
}