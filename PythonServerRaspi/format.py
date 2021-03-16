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