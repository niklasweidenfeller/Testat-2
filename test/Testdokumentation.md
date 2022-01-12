# Testdokumentation

Starten des Servers
```
java Server
```
```
Server running on Port 7777
```
Starten eines Clients
```
java Client
```
```
========================
Enter a command:
```
## `SAVE`-Request
`SAVE Dies ist ein Test.`

Ausgabe auf Client-Seite:
```
========================
Enter a command:
SAVE Dies ist ein Test.
Connected to Server: localhost:7777
========================
Response from Server: KEY 341efd8d-dcf5-4a31-84f0-daabde9647e7
========================


========================
Enter a command:
```
Ausgabe auf Server-Seite:
```
========================
Client-request received
========================
Client port: 63558
Command: SAVE
Request Body:
Dies ist ein Test.
========================
Sending response: KEY 341efd8d-dcf5-4a31-84f0-daabde9647e7
========================
```
Inhalt des `Messages`-Ordner:
```
\Desktop\Messages> ls

Mode                 LastWriteTime         Length Name
----                 -------------         ------ ----
-a----        11.01.2022     17:46             18 341efd8d-dcf5-4a31-84f0-daabde9647e7.txt
```

## `GET`-Request: gespeicherte Nachricht anfragen
`GET 341efd8d-dcf5-4a31-84f0-daabde9647e7`

Ausgabe auf Client-Seite:
```
========================
Enter a command:
GET 341efd8d-dcf5-4a31-84f0-daabde9647e7
Connected to Server: localhost:7777
========================
Response from Server: OK Dies ist ein Test.
========================


========================
Enter a command:
```

Ausgabe auf Server-Seite:
```
========================
Client-request received
========================
Client port: 63593
Command: GET
Request Body:
341efd8d-dcf5-4a31-84f0-daabde9647e7
========================
Sending response: OK Dies ist ein Test.
========================
```

## Speichern weiterer Nachrichten:
`SAVE test1`

`SAVE test2`

Client-Ausgabe:
 ```
 ========================
Enter a command:
save test1
Connected to Server: localhost:7777
========================
Response from Server: KEY f07f9fa9-7f31-4229-945d-f1c933ecf728
========================


========================
Enter a command:
save test2
Connected to Server: localhost:7777
========================
Response from Server: KEY 38fbeffb-fc91-46fd-96c4-762b913315b5
========================
 ```

Server:
```
========================
Client-request received
========================
Client port: 56189
Command: SAVE
Request Body:
test1
========================
Sending response: KEY f07f9fa9-7f31-4229-945d-f1c933ecf728
========================


========================
Client-request received
========================
Client port: 56195
Command: SAVE
Request Body:
test2
========================
Sending response: KEY 38fbeffb-fc91-46fd-96c4-762b913315b5
========================
```

Messages-Verzeichnis:
```
Desktop\Messages> ls

Mode                 LastWriteTime         Length Name
----                 -------------         ------ ----
-a----        11.01.2022     17:46             18 341efd8d-dcf5-4a31-84f0-daabde9647e7.txt
-a----        11.01.2022     18:01              5 38fbeffb-fc91-46fd-96c4-762b913315b5.txt
-a----        11.01.2022     18:01              5 f07f9fa9-7f31-4229-945d-f1c933ecf728.txt
```

## Lesen der Nachrichten:
`GET f07f9fa9-7f31-4229-945d-f1c933ecf728`
```
========================
Enter a command:
GET f07f9fa9-7f31-4229-945d-f1c933ecf728
Connected to Server: localhost:7777
========================
Response from Server: OK test1
========================
```
```
========================
Client-request received
========================
Client port: 58189
Command: GET
Request Body:
f07f9fa9-7f31-4229-945d-f1c933ecf728
========================
Sending response: OK test1
========================
```
`GET 38fbeffb-fc91-46fd-96c4-762b913315b5`
```
========================
Enter a command:
GET 38fbeffb-fc91-46fd-96c4-762b913315b5
Connected to Server: localhost:7777
========================
Response from Server: OK test2
========================
```
```
========================
Client-request received
========================
Client port: 58196
Command: GET
Request Body:
38fbeffb-fc91-46fd-96c4-762b913315b5
========================
Sending response: OK test2
========================
```

## Verhalten im Fehlerfall
### Nicht unterstützte Methode (nicht SAVE/GET)
```
========================
Enter a command:
PRINT test
Connected to Server: localhost:7777
========================
Response from Server: BAD REQUEST
========================


========================
Enter a command:
```
Ausgabe auf Server-Seite:
```
========================
Client-request received
========================
Client port: 56126
Command: PRINT
Request Body:
test
========================
Sending response: BAD REQUEST
========================
```

### GET mit falschem Key
`
GET 341efd8d-dcf5-4a31-84f0-falsch`

Client-Ausgabe:
```
========================
Enter a command:
GET 341efd8d-dcf5-4a31-84f0-falsch
Connected to Server: localhost:7777
========================
Response from Server: FAILED
========================


========================
Enter a command:
```
Server-Ausgabe:
```
========================
Client-request received
========================
Client port: 56145
Command: GET
Request Body:
341efd8d-dcf5-4a31-84f0-falsch
========================
Sending response: FAILED
========================
```

### GET ohne Key
`GET`

Client-Ausgabe:
```
========================
Enter a command:
GET
Connected to Server: localhost:7777
========================
Response from Server: FAILED
========================


========================
Enter a command:
```
Server-Ausgabe:
```
========================
Client-request received
========================
Client port: 56162
Command: GET
Request Body:

========================
Sending response: FAILED
========================
```

### Client-Request bei nicht gestartetem Server
```
========================
Enter a command:
SAVE test
Could not connect to the Server: Connection refused: connect: localhost:7777
```

### `userIn.readLine()` ergibt `null`
```
========================
Enter a command:
a
Invalid input

========================
Enter a command:
```

### `clientRequest` / `networkIn.readLine()` ergibt `null`
```
========================
Client-request received
========================
Client port: 63674
Command:
Request Body:

========================
Sending response: BAD REQUEST
========================
```