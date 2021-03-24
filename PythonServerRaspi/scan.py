import json
import socket
import os
from pilight import pilight
from time import time

PILIGHT_HOST = "127.0.0.1"
PILIGHT_PORT = 5000
SAFE_PATH = os.getcwd() + "/" + "tempdata.json"

rec_message = False

def handle(data):
    global rec_message
    print(data)
    with open("tempdata.json", "w") as f:
        f.write(json.dumps(data))
        rec_message = True

def scan(client_sock):
    global rec_message

    print(os.getcwd())

    pilight_receiver = pilight.Client(PILIGHT_HOST, PILIGHT_PORT)
    pilight_receiver.set_callback(handle)
    pilight_receiver.start()

    start_time = time()
    ref_time = 20
    delta_time = 0
    while not rec_message and delta_time < ref_time:
        delta_time = time() - start_time
    pilight_receiver.stop()
    rec_message = False

    try:
       with open(SAFE_PATH, "r") as f:
            data = json.load(f)
    except:
        try:
            client_sock.send("ERROR!\npilight received nothing".encode("UTF-8"))
        except:
            pass
        return

    identity = None
    unitcode = None
    protocol = None

    if "message" not in data.keys():
        try:
            client_sock.send("ERROR!\nInvalid protocol. No message section found.".encode("UTF-8"))
        except:
            pass
        os.remove(SAFE_PATH)
        return
    else:
        #try to find id oder systemcode
        if "id" not in data["message"].keys() and "systemcode" not in data["message"].keys():
            try:
                client_sock.send("ERROR!\nInvalid protocol. No id or systemcode found.".encode("UTF-8"))
            except:
                pass
            os.remove(SAFE_PATH)
            return
        elif "id" in data["message"].keys():
            identity = "-i " + data["message"]["id"]
        else:
            identity = "-s " + data["message"]["systemcode"]

        if "unit" not in data["message"].keys():
            try:
                client_sock.send("ERROR!\nInvalid protocol. No unitcode found.".encode("UTF-8"))
            except:
                pass
            os.remove(SAFE_PATH)
            return
        else:
            unitcode = "-u " + str(data["message"]["unit"])


    #try to find protocol
    if "protocol" not in data.keys():
        try:
            client_sock.send("ERROR!\nInvalid protocol. No protocol type found.".encode("UTF-8"))
        except:
            pass
        os.remove(SAFE_PATH)
        return
    else:
        protocol = "-p " + data["protocol"] 


    sendstring = f"{identity} {unitcode} {protocol}"
    try:
        client_sock.send(sendstring.encode("UTF-8"))
    except:
        pass
    os.remove(SAFE_PATH)