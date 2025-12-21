import http from 'k6/http';
import { check } from 'k6';

export let options = { vus: 1, iterations: 1 };

export default function () {
  // Register
  let reg = http.post('http://localhost:8080/api/auth/register',
    JSON.stringify({ username: 'smoke_user', password: 'secret' }),
    { headers: { 'Content-Type': 'application/json' } }
  );
  check(reg, { 'register 200': (r) => r.status === 200 });
  let userId = reg.json('id');

  // Login
  let login = http.post('http://localhost:8080/api/auth/login',
    JSON.stringify({ username: 'smoke_user', password: 'secret' }),
    { headers: { 'Content-Type': 'application/json' } }
  );
  check(login, { 'login 200': (r) => r.status === 200 });
  let token = login.json('token');
  let headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };

  // Create conversation
  let conv = http.post('http://localhost:8080/api/conversations',
    JSON.stringify({ title: 'smoke chat', participantIds: `${userId},bot` }),
    { headers }
  );
  check(conv, { 'conversation 200': (r) => r.status === 200 });
  let convId = conv.json('id');

  // Send message
  let msg = http.post('http://localhost:8080/api/messages',
    JSON.stringify({ conversationId: convId, senderId: userId, content: 'Hola smoke' }),
    { headers }
  );
  check(msg, { 'message 200': (r) => r.status === 200 });

  // List messages
  let list = http.get(`http://localhost:8080/api/messages/${convId}?limit=10`, { headers });
  check(list, {
    'list 200': (r) => r.status === 200,
    'contains content': (r) => r.body.includes('Hola smoke'),
  });
}