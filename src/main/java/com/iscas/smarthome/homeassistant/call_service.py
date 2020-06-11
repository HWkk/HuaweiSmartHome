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


# call device operation
def call_service(entity_id, service_name):
    ws = open_connection()

    request = {
        "type": "call_service",
        "domain": "fan",
        "service": "turn_on",
        "service_data": {
            "entity_id": entity_id,
            "speed": service_name
        },
        "id": random.randint(1, 100)
    }

    ws.send(json.dumps(request))
    # ws.send(str(reload_request))

    result = ws.recv()
    result = ws.recv()
    # print("Received '%s'" % result)

    ws.close()


def main():
    call_service(sys.argv[1], sys.argv[2])


if __name__ == '__main__':
    sys.exit(main())
