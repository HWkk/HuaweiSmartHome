import random
import sys
# import yaml

import json

from websocket import create_connection

# connect to HA
def open_connection():
    ws = create_connection("ws://localhost:8123/api/websocket")

    auth = {
        "type": "auth",
        "api_password": "ASDF"
    }
    # ws.send(str(auth))
    ws.send(json.dumps(auth))

    result = ws.recv()
    # print("Received '%s'" % result)

    return ws


def call_service():
    ws = open_connection()

    request = {
        "type": "call_service",
        "domain": "automation",
        "service": "reload",
        "service_data": {},
        'id': 15
    }

    ws.send(json.dumps(request))
    # ws.send(str(reload_request))

    result = ws.recv()
    result = ws.recv()
    print("Received '%s'" % result)

    ws.close()

# get all services of devices
def get_service():
    ws = open_connection()

    get_services_request = {
        "id": random.randint(1, 100),
        "type": "get_services"
    }

    ws.send(json.dumps(get_services_request))
    # ws.send(str(reload_request))

    result = ws.recv()
    result = ws.recv()
    # print("Received '%s'" % result)

    ws.close()

    result = json.loads(result)
    print(result["result"])


# get some attributes
def get_device(entity_id, mode_attribute, attributes):

    ws = open_connection()
    get_state_request = {
        "id": random.randint(1, 100),
        "type": "get_states",
    }

    ws.send(json.dumps(get_state_request))
    # ws.send(str(reload_request))

    result = ws.recv()
    result = ws.recv()
    # print("Received '%s'" % result)

    ws.close()

    result = json.loads(result)

    mode = ""
    attrs = []
    for entity_dict in result["result"]:
        if entity_dict["entity_id"] == entity_id:
            mode = entity_dict["attributes"][mode_attribute]
            for attr in attributes:
                attrs.append(entity_dict["attributes"][attr])
            # print(entity_dict["attributes"])
    print(mode)
    # print(attributes)
    print(attrs)

# def main():
#     # get_service()
#
#     entity_id = "fan.xiaomi_air_purifier_2s"
#     # device_name = "Xiaomi Air Purifier 2S"
#     mode_attribute = "mode"
#     attributes = ["temperature", "humidity", "aqi", "motor_speed", "average_aqi", "purify_volume"]
#
#     # entity_id = "fan.xiaomi_air_humidifier"
#     # device_name = "Xiaomi Air Humidifier"
#     get_device(entity_id, mode_attribute, attributes)

def main():
    entity_id = sys.argv[1]
    mode_attribute = sys.argv[2]
    attributes = []
    for i in range(3, len(sys.argv)):
        attributes.append(sys.argv[i])
    get_device(entity_id, mode_attribute, attributes)


if __name__ == '__main__':
    sys.exit(main())
