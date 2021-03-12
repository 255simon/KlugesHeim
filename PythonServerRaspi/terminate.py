import socket

HOST = ""
PORT = 2048 
EXIT_MESSAGE = "ciao".encode("UTF-8")

def main():
    client_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_sock.connect((HOST, PORT))
    client_sock.send(EXIT_MESSAGE)
    client_sock.close()


if __name__ == "__main__":
    main()