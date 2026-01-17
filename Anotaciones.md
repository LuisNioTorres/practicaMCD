🧠 Pregunta central que vamos a responder

Si ejecuto 4 veces Nodo.class en 4 terminales (4 procesos distintos),
cómo puede “verse / compartirse” un dato entre terminales si cada proceso tiene su propia memoria?

👉 Respuesta corta y fundamental:

NO se comparte memoria real
Se comparte información mediante MENSAJES
y cada nodo mantiene una COPIA LOCAL del dato

Eso es exactamente lo que hace un DSM.

1️⃣ Qué debes asumir desde el principio (muy importante)

Cuando haces esto:

Terminal 1 → java Nodo
Terminal 2 → java Nodo
Terminal 3 → java Nodo
Terminal 4 → java Nodo


Tienes:

4 procesos independientes

4 memorias separadas

❌ NINGUNA variable Java se comparte directamente

👉 Si quieres “memoria compartida”, tú la tienes que simular

2️⃣ Qué es “el contador compartido” en realidad

En DSM (y en tu práctica):

El contador NO es una variable global real

Es:

una variable local en cada nodo

que se mantiene coherente mediante mensajes

Conceptualmente, cada nodo tiene algo así:

int contadorLocal;


Pero:

Su valor puede cambiar

No solo cuando el nodo lo modifica

Sino cuando recibe actualizaciones

3️⃣ Qué significa “ver el mismo dato” entre terminales

👉 Significa esto:

Todos los nodos tienen COPIAS coherentes del contador

No significa:

misma dirección de memoria

mismo objeto Java

Significa:

mismo valor lógico

4️⃣ Arquitectura mínima para RC write-update

Para que esto funcione, cada nodo necesita 3 ideas claras:

🧱 A. Copia local del dato

Cada nodo tiene:

contadorLocal


Este es el valor que imprime, lee, etc.

🧠 B. Estado de coherencia

Cada nodo sabe:

estadoContador = VALIDO


(en write-update casi siempre está válido porque se actualiza)

📡 C. Comunicación entre nodos

Los nodos se comunican usando:

sockets

RMI

mensajes (no importa el mecanismo)

Conceptualmente:

enviarMensaje(nodoDestino, tipo, valor)