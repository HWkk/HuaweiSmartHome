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

def main():
    get_service()

if __name__ == '__main__':
    sys.exit(main())
