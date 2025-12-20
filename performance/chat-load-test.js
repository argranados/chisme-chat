import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 20,            // 20 usuarios concurrentes
  duration: '20s',    // durante 20 segundos
};

// Función para registrar y loguear usuario
function registerAndLogin(username, password) {
  // Registrar
  http.post('http://localhost:8080/api/auth/register',
    JSON.stringify({ username, password }),
    { headers: { 'Content-Type': 'application/json' } }
  );

  // Login
  let loginRes = http.post('http://localhost:8080/api/auth/login',
    JSON.stringify({ username, password }),
    { headers: { 'Content-Type': 'application/json' } }
  );

  check(loginRes, { 'login status is 200': (r) => r.status === 200 });
  let token = loginRes.json('token');
  return token;
}

// Setup: se ejecuta una sola vez antes de la carga
export function setup() {
  // No necesitamos nada global, cada VU hará su propio flujo
}

// Test principal: cada usuario virtual ejecuta esto
export default function () {
  const username = `user_${__VU}`; // nombre único por usuario virtual
  const password = 'secret';
  const token = registerAndLogin(username, password);

  const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  };

  // Crear conversación
  let convRes = http.post('http://localhost:8080/api/conversations',
    JSON.stringify({ title: 'chat', participantIds: username }),
    { headers }
  );
  check(convRes, { 'conversation created': (r) => r.status === 200 });
  let conversationId = convRes.json('id');

  // Enviar mensajes a esa conversación
  for (let i = 0; i < 5; i++) {
    let msgRes = http.post('http://localhost:8080/api/messages',
      JSON.stringify({
        conversationId: conversationId,
        senderId: username,
        content: `Hola mundo ${i} desde ${username}`
      }),
      { headers }
    );
    check(msgRes, { 'message sent': (r) => r.status === 200 });
    sleep(1);
  }
}