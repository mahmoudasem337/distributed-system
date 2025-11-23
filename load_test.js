import http from 'k6/http';
import { sleep } from 'k6';
import { check } from 'k6';

export const options = {
  scenarios: {
    ramping_load: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '10s', target: 500 },
        { duration: '20s', target: 2000 },
        { duration: '20s', target: 5000 },
        { duration: '10s', target: 0 },
      ],
      gracefulRampDown: '5s',
    },
  },

  thresholds: {
    http_req_failed: ['rate<0.02'],
    http_req_duration: ['p(95)<1000'],
  },
};

export default function () {

  const BASE_URL = 'http://localhost:9093';
  const paths = [
    '/api/final',
    '/api/final/error'
  ];

  const path = paths[Math.floor(Math.random() * paths.length)];
  const res = http.get(`${BASE_URL}${path}`);

  check(res, {
    'status OK': (r) => r.status === 200 || r.status === 201,
  });

  sleep(Math.random() * (2 - 0.3) + 0.3);
}
