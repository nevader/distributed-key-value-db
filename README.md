# 🗄️ Distributed Database System

A symmetric peer-to-peer distributed key-value database implementation in Java, developed as part of the Computer Networks and Network Programming course (SKJ) at PJATK, Winter 2022/2023.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![TCP/IP](https://img.shields.io/badge/TCP/IP-005571?style=for-the-badge&logo=cisco&logoColor=white)
![Distributed Systems](https://img.shields.io/badge/Distributed%20Systems-FF6B6B?style=for-the-badge&logo=apache&logoColor=white)

## 📋 Overview

This project implements a distributed database system where multiple nodes form a network, each storing key-value pairs. The system supports dynamic network topology changes, concurrent client operations, and various database queries across the distributed network.

### 🌟 Key Features

- **Peer-to-peer architecture** - No central server, all nodes are equal
- **Dynamic network topology** - Nodes can join and leave the network dynamically
- **Concurrent client support** - Multiple clients can operate on the database simultaneously
- **Distributed queries** - Operations span across the entire network
- **Fault tolerance** - Nodes can disconnect without breaking the entire system

## 🏗️ Architecture

### Core Components

- **`DatabaseNode`** - Network nodes that store data and handle client requests
- **`DatabaseClient`** - Client application for performing database operations
- **`Communication`** - Manages TCP connections and message routing
- **`Storage`** - Thread-safe key-value storage using `ConcurrentHashMap`
- **`Topology`** - Maintains network structure and visited nodes tracking

### Message Types

The system uses different message types for communication:
- **Client Requests** - Operations initiated by clients (get, set, find, etc.)
- **Broadcasts** - Network topology updates and node discovery
- **Special Operations** - Min/max queries and node termination

## 🚀 Getting Started

### Prerequisites

- Java 8 or higher
- Command line interface

### Compilation

Use the provided batch script or compile manually:

```bash
# Using the provided script
./compile.bat

# Manual compilation
javac -d bin src/*.java src/Requests/*.java src/Requests/Client/*.java src/Requests/Broadcast/*.java
````

### Running the System

#### 1. Start Database Nodes

```bash
# First node in the network
java DatabaseNode -tcpport 9990 -record 10:100

# Additional nodes connecting to existing network
java DatabaseNode -tcpport 9991 -record 17:256 -connect localhost:9990
java DatabaseNode -tcpport 9992 -record 25:500 -connect localhost:9990 -connect localhost:9991
```

Parameters:
- `-tcpport <port>` - TCP port for client connections
- `-record <key>:<value>` - Initial key-value pair stored on this node
- `-connect <host>:<port>` - Existing nodes to connect to (multiple allowed)

#### 2. Run Client Operations

```bash
# Get value for a key
java DatabaseClient -gateway localhost:9991 -operation get-value 17

# Set new value
java DatabaseClient -gateway localhost:9990 -operation set-value 10:200

# Find node storing a key
java DatabaseClient -gateway localhost:9992 -operation find-key 25

# Get maximum value in the database
java DatabaseClient -gateway localhost:9990 -operation get-max

# Get minimum value in the database
java DatabaseClient -gateway localhost:9991 -operation get-min
```

## 📝 Supported Operations

|Operation|Description|Example|
|---|---|---|
|`set-value <key>:<value>`|Update value for existing key|`set-value 10:200`|
|`get-value <key>`|Retrieve value for a key|`get-value 17`|
|`find-key <key>`|Find node address storing the key|`find-key 25`|
|`get-max`|Find maximum value across all nodes|`get-max`|
|`get-min`|Find minimum value across all nodes|`get-min`|
|`new-record <key>:<value>`|Replace record on gateway node|`new-record 30:600`|
|`terminate`|Gracefully disconnect node from network|`terminate`|

## 🔧 Implementation Details

### Network Communication

- **TCP Protocol** for reliable client-server and inter-node communication
- **Object Serialization** for message passing between nodes
- **Multi-threading** for handling concurrent connections

### Data Consistency

- Thread-safe storage using `ConcurrentHashMap`
- Visited nodes tracking to prevent infinite loops in queries
- Broadcast mechanisms for topology updates

### Fault Tolerance

- Graceful node disconnection with neighbor notification
- Dynamic topology updates when nodes join or leave
- Error handling for network failures

## 📊 Performance Considerations

- Efficient routing using visited nodes tracking
- Minimal network overhead with targeted broadcasts
- Concurrent request handling without blocking

## 🧪 Testing

The project includes test scripts in the `scripts` folder demonstrating various scenarios:

- Single client operations
- Multiple concurrent clients
- Node addition and removal
- Network partitioning scenarios

## 📚 Academic Context

This project was developed as part of the Computer Networks and Network Programming course (SKJ) at the Polish-Japanese Academy of Information Technology (PJATK) during the winter semester of 2022/2023.

### Learning Objectives

- Understanding distributed systems concepts
- Implementing network protocols
- Managing concurrent operations
- Handling network failures and topology changes

## 👤 Author

**Krzysztof Przybysz**  
Student ID: s24825  
Group: 23c
