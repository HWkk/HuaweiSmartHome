import random
import sys
# import yaml

import json

from websocket import create_connection


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

def get_device(entity_id):

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

    for entity_dict in result["result"]:
        if entity_dict["entity_id"] == entity_id:
            print(entity_dict["attributes"])

def main():
    entity_id = sys.argv[1]
    get_device(entity_id)

if __name__ == '__main__':
    sys.exit(main())
