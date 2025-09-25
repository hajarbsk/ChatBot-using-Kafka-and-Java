🛍️ Development of an E-commerce Question-Answering Chatbot


📖 Project Description

This project focuses on developing an intelligent chatbot for an e-commerce platform. The chatbot allows users to:

Ask questions in natural language (English/French/Arabic).

Get automatic real-time answers about:

Product availability

Store hours

Current promotions

Return policy

Personalized product recommendations

The system is powered by Kafka, MongoDB, NLP, and a JavaFX interface for user interaction.

⚙️ Technologies Used

Language: Java

Messaging System: Apache Kafka

Database: MongoDB + SQL

User Interface: JavaFX

NLP: Stanford CoreNLP
Build Tool: Maven


📥 Installation & Execution
1️⃣ Requirements

Java 11+
Apache Kafka installed and configured
MongoDB running
Maven

2️⃣ Clone the project
git clone https://github.com/your-username/chatbot-ecommerce.git
cd chatbot-ecommerce

3️⃣ Start Kafka and Zookeeper
zookeeper-server-start.sh config/zookeeper.properties
kafka-server-start.sh config/server.properties

4️⃣ Run the application
mvn clean install
mvn exec:java -Dexec.mainClass="Main"
