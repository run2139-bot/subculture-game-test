import axios from 'axios'

const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080',
  timeout: 10000,
})

export const fetchQuestions = () => client.get('/api/questions').then((res) => res.data)

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
