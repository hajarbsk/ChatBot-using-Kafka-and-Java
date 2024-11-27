from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from transformers import pipeline
from confluent_kafka import Consumer, Producer
import databases
import re

# Initialize FastAPI
app = FastAPI()

# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS = "localhost:9092"
producer = Producer({"bootstrap.servers": KAFKA_BOOTSTRAP_SERVERS})
consumer = Consumer({
    "bootstrap.servers": KAFKA_BOOTSTRAP_SERVERS,
    "group.id": "chatbot-group",
    "auto.offset.reset": "earliest"
})

# Initialize Hugging Face Model
nlp_model = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")

# Database Configuration (adjust as needed)
DATABASE_URL = "mysql+mysqlconnector://root:@localhost/chatbot"
database = databases.Database(DATABASE_URL)

# Extract features from the query
def extract_features(query: str):
    features = {"color": None, "size": None, "category": "robe"}
    if "rouge" in query:
        features["color"] = "rouge"
    size_match = re.search(r"(taille\s?[XS|S|M|L|XL|XXL]+)", query, re.IGNORECASE)
    if size_match:
        features["size"] = size_match.group(1).split()[-1]
    return features

async def fetch_products(features: dict):
    # Simulated response for simplicity
    return [
        {"product_name": "Robe Élégante", "brand": "Marque A", "price": 49.99, "color": "rouge", "size": "M"},
        {"product_name": "Robe Cocktail", "brand": "Marque B", "price": 69.99, "color": "rouge", "size": "L"}
    ]

def process_query(user_query: str):
    features = extract_features(user_query)
    products_data = fetch_products(features)  # Simulated fetch

    if not products_data:
        response = "Aucun produit correspondant trouvé."
    else:
        product_list = "\n".join([f"{p['product_name']} ({p['brand']}) - {p['price']}€ - {p['color']} - {p['size']}" for p in products_data])
        response = f"Voici des produits qui correspondent à votre demande :\n{product_list}\nVoulez-vous plus d'informations ?"

    return response

# Kafka Consumer loop
def consume_messages():
    consumer.subscribe(["user-queries"])
    try:
        while True:
            msg = consumer.poll(timeout=1.0)
            if msg is None:
                continue
            elif msg.error():
                print(msg.error())
                continue
            else:
                user_query = msg.value().decode('utf-8')
                print(f"Received message: {user_query}")
                response = process_query(user_query)

                # Send the response to 'chatbot-responses'
                producer.produce("chatbot-responses", value=response)
                producer.flush()

    except Exception as e:
        print(f"Error: {e}")
    finally:
        consumer.close()

# Run the Kafka consumer in a separate thread or process if needed, or directly start it
if __name__ == "__main__":
    consume_messages()
