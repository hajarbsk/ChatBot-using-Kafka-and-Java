🛍️ Development of an E-commerce Question-Answering Chatbot

<img width="404" height="331" alt="image" src="https://github.com/user-attachments/assets/a5e906bd-b30b-4243-980f-3fc77c3b9a2c" />

📖 Project Description

This project focuses on developing an intelligent chatbot for an e-commerce platform. The chatbot allows users to:

Ask questions in natural language (English).

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


<img width="392" height="262" alt="image" src="https://github.com/user-attachments/assets/29556169-0f53-4629-b5b5-2e78fbd6c687" />
<img width="419" height="320" alt="image" src="https://github.com/user-attachments/assets/05652932-8d62-453e-93bb-fb2866b0cd5b" />
<img width="418" height="188" alt="image" src="https://github.com/user-attachments/assets/ae85669c-f612-471e-abc4-8891a31c8538" />
<img width="404" height="269" alt="image" src="https://github.com/user-attachments/assets/de71aba7-7ac6-4c5b-9275-6f199e82747b" />


