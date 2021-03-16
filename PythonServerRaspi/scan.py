import json
import socket
import os
from pilight import pilight
from time import time

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

    try:
       with open(os.getcwd() + "/" + "tempdata.json", "r") as f:
            data = json.load(f)
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