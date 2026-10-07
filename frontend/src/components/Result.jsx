import { useState } from 'react'
import { Check, Gamepad2, RotateCcw, Trophy } from 'lucide-react'

const TRAIT_LABELS = {
  ACTION: '액션',
  STRATEGY: '전략',
  STORY: '스토리',
  EXPLORE: '탐험',
  CASUAL: '가벼움',
  DARK: '어두운 분위기',
  CHAR: '캐릭터 애착',
}

function GameImage({ src, alt, className }) {
  const [failed, setFailed] = useState(false)
  if (!src || failed) {
    return (
      <div className={`flex items-center justify-center bg-indigo-900/60 text-indigo-300 ${className}`}>
        <Gamepad2 size={40} />
      </div>
    )
  }
  return <img src={src} alt={alt} onError={() => setFailed(true)} className={`object-cover ${className}`} />
}

function TraitBars({ scores }) {
  const entries = Object.entries(scores).map(([code, v]) => [code, Math.max(0, v)])
  const max = Math.max(1, ...entries.map(([, v]) => v))
  return (
    <ul className="flex flex-col gap-2">
      {entries.map(([code, value]) => (
        <li key={code} className="flex items-center gap-3 text-sm">
          <span className="w-24 shrink-0 text-slate-300">{TRAIT_LABELS[code] ?? code}</span>
          <div className="h-2 flex-1 overflow-hidden rounded-full bg-white/10">
            <div className="h-full rounded-full bg-indigo-400" style={{ width: `${(value / max) * 100}%` }} />
          </div>
        </li>
      ))}
    </ul>
  )
}

export default function Result({ result, onRestart }) {
  const [top, ...others] = result.results

  return (
    <section className="flex flex-col gap-8">
      <div className="overflow-hidden rounded-2xl border border-indigo-400/40 bg-white/5">
        <GameImage src={top.imageUrl} alt={top.title} className="h-56 w-full" />
        <div className="flex flex-col gap-4 p-6">
          <div className="flex items-center gap-2 text-sm font-semibold text-amber-300">
            <Trophy size={18} />
            당신에게 딱 맞는 게임 · 일치도 {top.matchPercent}%
          </div>
          <div>
            <h2 className="text-3xl font-bold">{top.title}</h2>
            <p className="text-sm text-slate-400">{top.developer}</p>
          </div>
          <p className="text-slate-200">{top.description}</p>
          <ul className="flex flex-col gap-2">
            {top.recommendPoints.map((point) => (
              <li key={point} className="flex items-start gap-2 text-sm text-slate-200">
                <Check size={16} className="mt-0.5 shrink-0 text-indigo-300" />
                {point}
              </li>
            ))}
          </ul>
        </div>
      </div>

      <div>
        <h3 className="mb-3 text-lg font-semibold">나의 취향</h3>
        <TraitBars scores={result.traitScores} />
      </div>

      {others.length > 0 && (
        <div>
          <h3 className="mb-3 text-lg font-semibold">다른 추천 게임</h3>
          <ul className="flex flex-col gap-3">
            {others.map((game) => (
              <li key={game.slug} className="flex gap-4 rounded-xl border border-white/10 bg-white/5 p-3">
                <GameImage src={game.imageUrl} alt={game.title} className="h-20 w-20 shrink-0 rounded-lg" />
                <div className="min-w-0">
                  <div className="flex items-baseline gap-2">
                    <span className="text-sm text-slate-400">{game.rank}위</span>
                    <span className="truncate font-semibold">{game.title}</span>
                    <span className="ml-auto shrink-0 text-sm text-indigo-300">{game.matchPercent}%</span>
                  </div>
                  <p className="line-clamp-2 text-sm text-slate-300">{game.description}</p>
                </div>
              </li>
            ))}
          </ul>
        </div>
      )}

      <button
        onClick={onRestart}
        className="mx-auto flex items-center gap-2 rounded-xl border border-white/20 px-6 py-3 transition hover:bg-white/10"
      >
        <RotateCcw size={18} />
        다시 하기
      </button>
    </section>
  )
}
