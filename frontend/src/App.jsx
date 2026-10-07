import { useState } from 'react'
import { AlertCircle, Loader2 } from 'lucide-react'
import { fetchQuestions, submitAnswers } from './api'
import Board from './components/Board'
import Intro from './components/Intro'
import Quiz from './components/Quiz'
import Result from './components/Result'

export default function App() {
  const [tab, setTab] = useState('test') // test | board
  const [phase, setPhase] = useState('intro') // intro | quiz | loading | result
  const [questions, setQuestions] = useState([])
  const [result, setResult] = useState(null)
  const [error, setError] = useState(null)

  const start = async () => {
    setError(null)
    setPhase('loading')
    try {
      setQuestions(await fetchQuestions())
      setPhase('quiz')
    } catch {
      setError('질문을 불러오지 못했습니다. 서버가 실행 중인지 확인해 주세요.')
      setPhase('intro')
    }
  }

  const finish = async (answers) => {
    setError(null)
    setPhase('loading')
    try {
      setResult(await submitAnswers(answers))
      setPhase('result')
    } catch {
      setError('결과를 계산하지 못했습니다. 잠시 후 다시 시도해 주세요.')
      setPhase('quiz')
    }
  }

  const restart = () => {
    setResult(null)
    setError(null)
    setPhase('intro')
  }

  return (
    <main className="min-h-screen bg-gradient-to-b from-slate-950 via-indigo-950 to-slate-900 px-4 py-10 text-slate-100">
      <div className="mx-auto w-full max-w-2xl">
        <nav className="mb-8 flex justify-center gap-2 text-sm">
          {[
            ['test', '추천 테스트'],
            ['board', '게시판'],
          ].map(([key, label]) => (
            <button
              key={key}
              onClick={() => setTab(key)}
              className={`rounded-full px-5 py-2 transition ${
                tab === key ? 'bg-indigo-500 font-semibold' : 'bg-white/5 hover:bg-white/10'
              }`}
            >
              {label}
            </button>
          ))}
        </nav>
        {tab === 'board' ? (
          <Board />
        ) : (
          <>
        {error && (
          <div className="mb-4 flex items-center gap-2 rounded-lg border border-red-400/40 bg-red-500/10 p-3 text-sm text-red-200">
            <AlertCircle size={18} className="shrink-0" />
            {error}
          </div>
        )}
        {phase === 'intro' && <Intro onStart={start} />}
        {phase === 'loading' && (
          <div className="flex flex-col items-center gap-3 py-24 text-slate-300">
            <Loader2 className="animate-spin" size={36} />
            잠시만 기다려 주세요…
          </div>
        )}
        {phase === 'quiz' && <Quiz questions={questions} onFinish={finish} />}
        {phase === 'result' && <Result result={result} onRestart={restart} />}
          </>
        )}
      </div>
    </main>
  )
}
