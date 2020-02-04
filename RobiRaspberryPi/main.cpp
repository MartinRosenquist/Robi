#include <wiringPi.h>
#include "LedMatrix.h"
#include "Faces.h"
#include <thread>
#include <chrono>
#include <stdio.h>
#include <unistd.h>
#include <sys/socket.h>
#include <bluetooth/bluetooth.h>
#include <bluetooth/rfcomm.h>

int main(void)
{
	wiringPiSetup();

	LedMatrix LM = LedMatrix(0, 2, 1, 4, 8);

	LM.Clear();

    struct sockaddr_rc loc_addr = { 0 }, rem_addr = { 0 };
    char buf[1024] = { 0 };
    int s, client, bytes_read;
    socklen_t opt = sizeof(rem_addr);
    char address[18] = "XX:XX:XX:XX:XX:XX";

    // allocate socket
    s = socket(AF_BLUETOOTH, SOCK_STREAM, BTPROTO_RFCOMM);

    // bind socket to port 1 of the first available 
    // local bluetooth adapter
    loc_addr.rc_family = AF_BLUETOOTH;
    str2ba(address, &loc_addr.rc_bdaddr);
    loc_addr.rc_channel = (uint8_t)1;
    bind(s, (struct sockaddr*) & loc_addr, sizeof(loc_addr));

    // put socket into listening mode
    listen(s, 1);

    // accept one connection
    client = accept(s, (struct sockaddr*) & rem_addr, &opt);

    ba2str(&rem_addr.rc_bdaddr, buf);
    fprintf(stderr, "accepted connection from %s\n", buf);
    memset(buf, 0, sizeof(buf));
    bool running = true;

    // read data from the client
    while (running)
    {
        bytes_read = read(client, buf, sizeof(buf));
        if (bytes_read > 0) {
            printf("received [%s]\n", buf);

            if (buf[0] == '1' && buf[1] == '0')         // Move Stop (10)
            {
                printf("- Move Stop\n");
            }
            else if (buf[0] == '1' && buf[1] == '1')    // Move Forward (11)
            {
                printf("- Move Forward\n");
            }
            else if (buf[0] == '1' && buf[1] == '2')    // Move Backward (12)
            {
                printf("- Move Backward\n");
            }
            else if (buf[0] == '1' && buf[1] == '3')    // Move Left (13)
            {
                printf("- Move Left\n");
            }
            else if (buf[0] == '1' && buf[1] == '4')    // Move Right (14)
            {
                printf("- Move Right\n");
            }
            else if (buf[0] == '2' && buf[1] == '0')    // Change to neutral face (20)
            {
                printf("- Changing to neutral face\n");
                LM.ChangeFace(Happyface);
            }
            else if (buf[0] == '2' && buf[1] == '1')    // Change to happy face (21)
            {
                printf("- Changing to happy face\n");
                LM.ChangeFace(Happyface);
            }
            else if (buf[0] == '2' && buf[1] == '2')    // Change to grin face (22)
            {
                printf("- Changing to grin face\n");
                LM.ChangeFace(Happyface);
            }
            else if (buf[0] == '2' && buf[1] == '3')    // Change to sad face (23)
            {
                printf("- Changing to sad face\n");
                LM.ChangeFace(Happyface);
            }
            else if (buf[0] == '0' && buf[1] == '0')    // Shutdown (00)
            {
                printf("- Shutting down..\n");
                running = false;
            }
        }
    }

    LM.Clear();
    // close connection
    close(client);
    close(s);
    return 0;
}