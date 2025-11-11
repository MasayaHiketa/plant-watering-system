# test_receiver.py
from flask import Flask, request
app = Flask(__name__)

@app.route("/webhooks/watering", methods=["POST"])
def receive():
    print("=== Webhook Received ===")
    print(request.get_json())
    return {"status": "ok"}, 200

app.run(port=8081)
