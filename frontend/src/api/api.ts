import axios from 'axios';
const DEV = 'http://localhost:8080/api/v1';
const HOST = 'https://k7a405.p.ssafy.io:8080/api/v1/';

const api = axios.create({
  baseURL: HOST,
});
// 요청 인터셉터
api.interceptors.request.use(
  function (config) {
    // 요청 성공 직전 호출됩니다.
    return config;
  },
  function (error) {
    // 요청 에러 직전 호출됩니다.
    window.location.href = `/error/${error.response.status}`;
    return Promise.reject(error);
  },
);

// 응답 인터셉터
api.interceptors.response.use(
  function (response) {
    return response;
  },
  function (error) {
    window.location.href = `/error/${error.response.status}`;
    return Promise.reject(error);
  },
);
export default api;
