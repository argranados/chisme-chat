import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 1, // usuarios virtuales
  iterations: 1, // solo una pasada (smoke test)
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  let token;
  let userId;
  let conversationId;

  // --- AUTH CONTROLLER ---
  let registerRes = http.post(`${BASE_URL}/api/auth/register`, JSON.stringify({
    username: 'alice',
    password: 'secret123'
  }), { headers: { 'Content-Type': 'application/json' } });
  
  check(registerRes, {
    'register status 200': (r) => r.status === 200,
    'register has id': (r) => r.json('id') !== undefined,
  });
  userId = registerRes.json('id');
  //console.log('registerRes response:', registerRes.status, registerRes.body);

  let loginRes = http.post(`${BASE_URL}/api/auth/login`, JSON.stringify({
    username: 'alice',
    password: 'secret123'
  }), { headers: { 'Content-Type': 'application/json' } });
  //console.log('loginRes response:', loginRes.status, loginRes.body);

  check(loginRes, {
    'login status 200': (r) => r.status === 200,
    'login has token': (r) => r.json('token') !== undefined,
  });
  token = loginRes.json('token');

  // --- CONVERSATION CONTROLLER ---
  let convRes = http.post(`${BASE_URL}/api/conversations`, JSON.stringify({
    title: 'Chat de prueba', creatorId:userId
  }), { headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`} });
  console.log('convRes response:', convRes.status, convRes.body);

  check(convRes, {
    'conversation created': (r) => r.status === 200,
    'conversation has id': (r) => r.json('id') !== undefined,
  });
  conversationId = convRes.json('id');

  let listRes = http.get(`${BASE_URL}/api/conversations/user/${userId}`,
    { headers: { 'Authorization': `Bearer ${token}` } }
  );
  console.log('listRes response:', listRes.status, listRes.body);

  check(listRes, {
    'list conversations status 200': (r) => r.status === 200,
  });
  

  // --- MESSAGE CONTROLLER ---
  let msgRes = http.post(`${BASE_URL}/api/messages`, JSON.stringify({
    conversationId: conversationId,
    senderId: userId,
    content: 'Hola mundo!'
  }), { headers: { 'Content-Type': 'application/json','Authorization': `Bearer ${token}` } });
  //console.log('msgRes response:', msgRes.status, msgRes.body);

  check(msgRes, {
    'message sent': (r) => r.status === 200,
    'message has id': (r) => r.json('id') !== undefined,
  });

  // let listMsgRes = http.get(`${BASE_URL}/api/messages/${conversationId}?limit=10`);
  let listMsgRes = http.get(`${BASE_URL}/api/messages/${conversationId}/paged?page=0&size=10`, 
    { headers: { 'Authorization': `Bearer ${token}` } }
  );
  //console.log('list messages response:', listMsgRes.status, listMsgRes.body);

  check(listMsgRes, {
    'list messages status 200': (r) => r.status === 200,
    'list messages returns array': (r) => Array.isArray(r.json()),
  });

  sleep(1);
}