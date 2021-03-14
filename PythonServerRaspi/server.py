import socket
import json
import os
from pilight import pilight
from time import time


HOST = ''
PORT = 2048
BUFF_SIZE = 4096

rec_message = False
client_sock = None

def find_protocol(data):
    data_list = data.split(" ")
    return data_list[data_list.index("-p") + 1] 

def find_id(data):
    data_list = data.split(" ")
    id = data_list[data_list.index("-i") + 1]
    if(id.isdecimal()):
        id = int(id) 
    return id

def find_syscode(data):
    data_list = data.split(" ")
    syscode = data_list[data_list.index("-s") + 1]
    if(syscode.isdecimal()):
        syscode = int(syscode) 
    return syscode

def find_unitcode(data):
    data_list = data.split(" ")
    return int(data_list[data_list.index("-u") + 1] )

def find_command(data):
    command = "off"
    if("--on" in data and not "--off" in data):     #if --on and --off are in data define off is dominating
        command = "on"
    return command


def format_data(data):
    return_dict = {} 
    return_dict["protocol"] = [find_protocol(data)] 

    try:
        return_dict["id"] = find_id(data)
    except ValueError:
        return_dict["systemcode"] = find_syscode(data)

    return_dict["unit"] = find_unitcode(data)
    return_dict[find_command(data)] = 1 

    return return_dict

def handle(data):
    global rec_message
    print(data)
    with open("tempdata.json", "w") as f:
        f.write(json.dumps(data))
        rec_message = True

def scan(client_sock):

    print("bin in scan")
    global rec_message

    print(os.getcwd())

    pilight_receiver = pilight.Client(host = "127.0.0.1", port = 5000)
    pilight_receiver.set_callback(handle)
    pilight_receiver.start()

    start_time = time()
    ref_time = 20
    delta_time = 0
    while not rec_message and delta_time < ref_time:
        delta_time = time() - start_time
    pilight_receiver.stop()
    rec_message = False
    print("received message")

    with open(os.getcwd() + "/" + "tempdata.json", "r") as f:
        try:
            data = json.load(f)
            print("loaded data")
        except:
            try:
                client_sock.send("pilight received nothing".encode("UTF-8"))
            except:
                pass
            return

    identity = None
    unitcode = None
    protocol = None

    #try to find id or systemcode
    try:
        identity = "-i " + str(data["message"]["id"])
    except KeyError:
        try:
            identity = "-s " + str(data["message"]["systemcode"])
        except KeyError:
            #errorhandling if no id and no systemcode was found
            try:
                client_sock.send("no id or systemcode found".encode("UTF-8"))
            except:
                pass
            os.remove(os.getcwd() + "/" + "tempdata.json")
            return

    #try to find unitcode
    try:
        unitcode = "-u " + str(data["message"]["unit"])
    except KeyError:
        #errorhandling if no unitcode was found
        try:
            client_sock.send("no unitcode found".encode("UTF-8"))
        except:
            pass
        os.remove(os.getcwd() + "/" + "tempdata.json")
        return

    #try to find protocol
    try:
        protocol = "-p " + data["protocol"]
    except KeyError:
        #errorhandling if no protocol was found
        try:
            client_sock.send("no protocol found".encode("UTF-8"))
        except:
            pass
        os.remove(os.getcwd() + "/" + "tempdata.json")
        return

    sendstring = f"{identity} {unitcode} {protocol}"
    try:
        client_sock.send(sendstring.encode("UTF-8"))
    except:
        pass
    os.remove(os.getcwd() + "/" + "tempdata.json")



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
            scan(client_sock)
        else:
            pilight_transmitter.send_code(format_data(data))
        client_sock.close()

    sock.close()

if __name__ == '__main__':
    main()