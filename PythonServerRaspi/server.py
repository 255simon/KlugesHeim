import socket

HOST = ''
PORT = 2048
BUFF_SIZE = 4096

def main():
    terminate = False
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.bind((HOST, PORT))
    print("server is up!")

    while not terminate:
        sock.listen(1)
        client_sock, client_addr = sock.accept()
        
        data = client_sock.recv(BUFF_SIZE).decode("UTF-8")
        print(data)
        
        if(data == "ciao"):
            terminate = True
            print("terminate server")
        client_sock.close()

    sock.close()

if __name__ == '__main__':
    main()