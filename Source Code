#include <iostream>
#include <cstring>
#include <thread>
#include <netinet/in.h>
#include <unistd.h>
#include <librdkafka/rdkafkacpp.h>

#define KAFKA_TOPIC "web_traffic_logs"
#define KAFKA_BROKER "localhost:9092"

class TrafficProcessingSDK {
public:
    TrafficProcessingSDK(int port) : port(port) {}

    // Starting the traffic processing server
    void startServer() {
        int serverSocket, clientSocket;
        struct sockaddr_in address;
        int opt = 1;
        int addrlen = sizeof(address);

        if ((serverSocket = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
            perror("socket failed");
            exit(EXIT_FAILURE);
        }

        if (setsockopt(serverSocket, SOL_SOCKET, SO_REUSEADDR | SO_REUSEPORT, &opt, sizeof(opt))) {
            perror("setsockopt");
            exit(EXIT_FAILURE);
        }
        address.sin_family = AF_INET;
        address.sin_addr.s_addr = INADDR_ANY;
        address.sin_port = htons(port);

        if (bind(serverSocket, (struct sockaddr *)&address, sizeof(address)) < 0) {
            perror("bind failed");
            exit(EXIT_FAILURE);
        }

        if (listen(serverSocket, 3) < 0) {
            perror("listen");
            exit(EXIT_FAILURE);
        }

        while (true) {
            if ((clientSocket = accept(serverSocket, (struct sockaddr *)&address, (socklen_t *)&addrlen)) < 0) {
                perror("accept");
                exit(EXIT_FAILURE);
            }
            std::thread(&TrafficProcessingSDK::handleRequest, this, clientSocket).detach();
        }
    }

private:
    int port;

    // Handling client requests
    void handleRequest(int clientSocket) {
        char buffer[1024] = {0};
        ssize_t valread = read(clientSocket, buffer, 1024);
        if (valread > 0) {
            std::string request(buffer);
            std::cout << "Received request: " << request << std::endl;
            sendToKafka(request);
        }
        // Echo back the received data
        send(clientSocket, buffer, valread, 0);
        close(clientSocket);
    }

    // Sending data to Kafka
    void sendToKafka(const std::string &data) {
        std::cout << "Sending data to Kafka: " << data << std::endl;

        RdKafka::Producer *producer;
        RdKafka::Conf *conf;
        std::string errstr;

        conf = RdKafka::Conf::create(RdKafka::Conf::CONF_GLOBAL);
        if (conf->set("bootstrap.servers", KAFKA_BROKER, errstr) != RdKafka::Conf::CONF_OK) {
            std::cerr << "Failed to set Kafka broker list: " << errstr << std::endl;
            delete conf;
            return;
        }

        producer = RdKafka::Producer::create(conf, errstr);
        if (!producer) {
            std::cerr << "Failed to create Kafka producer: " << errstr << std::endl;
            delete conf;
            return;
        }

        RdKafka::Topic *topic = RdKafka::Topic::create(producer, KAFKA_TOPIC, NULL, errstr);
        if (!topic) {
            std::cerr << "Failed to create Kafka topic: " << errstr << std::endl;
            delete producer;
            delete conf;
            return;
        }

        RdKafka::ErrorCode resp = producer->produce(topic, RdKafka::Topic::PARTITION_UA,
                                                    RdKafka::Producer::RK_MSG_COPY,
                                                    data.c_str(), data.size(),
                                                    NULL, NULL);
        if (resp != RdKafka::ERR_NO_ERROR) {
            std::cerr << "Failed to produce message: " << RdKafka::err2str(resp) << std::endl;
        }

        producer->poll(0);
        delete topic;
        delete producer;
        delete conf;
    }
};

// Entry point for the SDK
extern "C" {
    TrafficProcessingSDK* createTrafficProcessingSDK(int port) {
        return new TrafficProcessingSDK(port);
    }

    void startServer(TrafficProcessingSDK* sdk) {
        sdk->startServer();
    }

    void destroyTrafficProcessingSDK(TrafficProcessingSDK* sdk) {
        delete sdk;
    }
}
