import { Gamepad2, Sparkles } from 'lucide-react'

export default function Intro({ onStart }) {
  return (
    <section className="flex flex-col items-center gap-6 py-16 text-center">
      <div className="rounded-full bg-indigo-500/20 p-5 text-indigo-300">
        <Gamepad2 size={48} />
      </div>
      <h1 className="text-3xl font-bold sm:text-4xl">나에게 맞는 서브컬처 게임은?</h1>
      <p className="max-w-md text-slate-300">
        10가지 질문에 답하면 취향에 가장 가까운 게임을 추천해 드려요.
      </p>
      <button
        onClick={onStart}
        className="flex items-center gap-2 rounded-xl bg-indigo-500 px-8 py-3 font-semibold transition hover:bg-indigo-400"
      >
        <Sparkles size={18} />
        테스트 시작하기
      </button>
    </section>
  )
}
