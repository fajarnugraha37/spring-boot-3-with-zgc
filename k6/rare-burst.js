import http from 'k6/http';

export let options = {
  scenarios: {
    rare_burst: {
      executor: 'per-vu-iterations',
      vus: 3,
      iterations: 2, // Each VU will hit it twice
      maxDuration: '2m',
      exec: 'rareBurst'
    }
  },
  thresholds: {
    http_req_duration: ['p(95)<5000'], // 95% below 5s
  }
};

export function rareBurst() {
  http.get('http://app:8080/rare-burst?mb=200&batchSize=20');
}