import { useState } from 'react'
import { ArrowLeft } from 'lucide-react'

export default function Quiz({ questions, onFinish }) {
  const [index, setIndex] = useState(0)
  const [answers, setAnswers] = useState({}) // { [questionId]: choiceId }

  const question = questions[index]
  const progress = Math.round((index / questions.length) * 100)

  const select = (choiceId) => {
    const next = { ...answers, [question.id]: choiceId }
    setAnswers(next)
    if (index + 1 < questions.length) {
      setIndex(index + 1)
    } else {
      onFinish(next)
    }
  }

  return (
    <section className="flex flex-col gap-6">
      <div>
        <div className="mb-2 flex items-center justify-between text-sm text-slate-300">
          <button
            onClick={() => setIndex(index - 1)}
            disabled={index === 0}
            className="flex items-center gap-1 transition hover:text-white disabled:invisible"
          >
            <ArrowLeft size={16} />
            이전
          </button>
          <span>
            {index + 1} / {questions.length}
          </span>
        </div>
        <div className="h-2 overflow-hidden rounded-full bg-white/10">
          <div
            className="h-full rounded-full bg-indigo-400 transition-all"
            style={{ width: `${progress}%` }}
          />
        </div>
      </div>

      <h2 className="text-2xl font-bold">{question.text}</h2>

      <ul className="flex flex-col gap-3">
        {question.choices.map((choice) => {
          const selected = answers[question.id] === choice.id
          return (
            <li key={choice.id}>
              <button
                onClick={() => select(choice.id)}
                className={`w-full rounded-xl border p-4 text-left transition ${
                  selected
                    ? 'border-indigo-400 bg-indigo-500/30'
                    : 'border-white/10 bg-white/5 hover:border-indigo-400/60 hover:bg-white/10'
                }`}
              >
                {choice.text}
              </button>
            </li>
          )
        })}
      </ul>
    </section>
  )
}
