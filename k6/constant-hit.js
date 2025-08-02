import http from 'k6/http';

export let options = {
  scenarios: {
    constant_hit: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '1m', target: 25 },  // Ramp up to 10 VUs over 30s
        { duration: '3m', target: 200 },   // Stay at 10 VUs for 1min
        { duration: '1m', target: 0 },   // Ramp down to 0 VUs
      ],
      gracefulRampDown: '10s',
      exec: 'constantHit'
    }
  },
  thresholds: {
    http_req_duration: ['p(95)<400'], // 95% of requests should be below 400ms
  }
};

export function constantHit() {
  http.get('http://app:8080/constant-hit?key=test');
  // Think time: handled by VU pacing, not sleep!
}