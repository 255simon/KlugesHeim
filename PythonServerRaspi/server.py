import socket
from pilight import pilight

HOST = ''
PORT = 2048
BUFF_SIZE = 4096

def find_protocol(data):
    data_list = data.split(" ")
    return data_list[data_list.index("-p") + 1] 

def find_id(data):
    data_list = data.split(" ")
    id = data_list[data_list.index("-i") + 1]
    if(id.isdecimal()):
        id = int(id) 
    return id

def find_unitcode(data):
    data_list = data.split(" ")
    return int(data_list[data_list.index("-u") + 1] )

def find_command(data):
    command = "off"
    if("--on" in data and not "--off" in data):     #if --on and --off are in data define off is dominating
        command = "on"
    return command


def format_data(data):
    protocol = find_protocol(data)
    id = find_id(data)
    ucode = find_unitcode(data)
    return {"protocol" :[protocol], "id" : id, "unit" : ucode, find_command(data) : 1} 
    
def main():
    pilight_connect = pilight.Client(host = "127.0.0.1", port = 5000)

    terminate = False
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.bind((HOST, PORT))
    print("server is up!")

    while not terminate:
        sock.listen(1)
        client_sock, client_addr = sock.accept()
        
        data = client_sock.recv(BUFF_SIZE).decode("UTF-8")
        if(data == "ciao"):
            terminate = True
            print("terminate server")
        else:
            pilight_connect.send_code(format_data(data))
        client_sock.close()

    sock.close()

if __name__ == '__main__':
    main()