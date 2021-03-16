import format
import scan

import socket
from pilight import pilight


HOST = ''
PORT = 2048
BUFF_SIZE = 4096



def main():
    pilight_transmitter = pilight.Client(host = "127.0.0.1", port = 5000)

    terminate = False
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.bind((HOST, PORT))
    print("server is up!")

    while not terminate:
        sock.listen(1)
        client_sock, client_addr = sock.accept()
        print(client_addr)
        
        data = client_sock.recv(BUFF_SIZE).decode("UTF-8")
        print(data)
        if(data == "ciao"):
            terminate = True
            print("terminate server")
        elif(data == "scan"):
            scan.scan(client_sock)
        else:
            pilight_transmitter.send_code(format.format_data(data))
        client_sock.close()

    sock.close()

if __name__ == '__main__':
    main()