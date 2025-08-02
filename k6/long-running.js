import http from 'k6/http';

export let options = {
  scenarios: {
    long_running: {
      executor: 'ramping-arrival-rate',
      startRate: 1,
      timeUnit: '10s', // 1 iteration every 10s
      preAllocatedVUs: 2,
      stages: [
        { target: 2, duration: '30s' },    // Ramp up to 2 iters/10s
        { target: 2, duration: '1m' },     // Sustain for 1 min
        { target: 0, duration: '30s' },    // Ramp down
      ],
      exec: 'longRunning'
    }
  },
  thresholds: {
    http_req_duration: ['p(95)<20000'], // 95% of requests below 20s (endpoint simulates long work)
  }
};

export function longRunning() {
  http.get('http://app:8080/long-running?batchSize=50');
}