import axios from 'axios'

const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080',
  timeout: 10000,
})

export const fetchQuestions = () => client.get('/api/questions').then((res) => res.data)

export const fetchPosts = (page = 0, size = 10) =>
  client.get('/api/posts', { params: { page, size } }).then((res) => res.data)

export const fetchPost = (id) => client.get(`/api/posts/${id}`).then((res) => res.data)

export const createPost = (post) => client.post('/api/posts', post).then((res) => res.data)

export const updatePost = (id, post) => client.put(`/api/posts/${id}`, post).then((res) => res.data)

export const deletePost = (id, password) =>
  client.delete(`/api/posts/${id}`, { headers: { 'X-Post-Password': password } })

/** 서버 오류 응답에서 사용자에게 보여 줄 메시지를 뽑는다. */
export const errorMessage = (e, fallback) =>
  e.response?.data?.message || (e.response?.status === 403 ? '비밀번호가 일치하지 않습니다.' : fallback)

/** answers: { [questionId]: choiceId } */
export const submitAnswers = (answers) =>
  client
    .post('/api/results', {
      answers: Object.entries(answers).map(([questionId, choiceId]) => ({
        questionId: Number(questionId),
        choiceId,
      })),
    })
    .then((res) => res.data)
