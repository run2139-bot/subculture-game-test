import { useCallback, useEffect, useState } from 'react'
import { ChevronLeft, ChevronRight, Loader2, PenLine } from 'lucide-react'
import { fetchPosts } from '../api'
import { formatDate } from '../format'
import PostDetail from './PostDetail'
import PostForm from './PostForm'

export default function Board() {
  // view: { type: 'list' } | { type: 'detail', id } | { type: 'form', post? }
  const [view, setView] = useState({ type: 'list' })
  const [page, setPage] = useState(0)
  const [data, setData] = useState(null)
  const [error, setError] = useState(null)

  const load = useCallback(async () => {
    setError(null)
    try {
      setData(await fetchPosts(page))
    } catch {
      setError('게시글을 불러오지 못했습니다. 서버가 실행 중인지 확인해 주세요.')
    }
  }, [page])

  useEffect(() => {
    if (view.type === 'list') load()
  }, [view.type, load])

  const toList = () => setView({ type: 'list' })

  if (view.type === 'form') {
    return (
      <PostForm
        post={view.post}
        onDone={(post) => setView({ type: 'detail', id: post.id })}
        onCancel={() => (view.post ? setView({ type: 'detail', id: view.post.id }) : toList())}
      />
    )
  }
  if (view.type === 'detail') {
    return (
      <PostDetail
        id={view.id}
        onBack={toList}
        onEdit={(post) => setView({ type: 'form', post })}
        onDeleted={() => {
          setPage(0)
          toList()
        }}
      />
    )
  }

  return (
    <section className="flex flex-col gap-4">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold">자유 게시판</h2>
        <button
          onClick={() => setView({ type: 'form' })}
          className="flex items-center gap-2 rounded-lg bg-indigo-500 px-4 py-2 text-sm font-semibold transition hover:bg-indigo-400"
        >
          <PenLine size={16} />
          글쓰기
        </button>
      </div>

      {error && <p className="rounded-lg bg-red-500/10 p-3 text-sm text-red-200">{error}</p>}
      {!data && !error && <Loader2 className="mx-auto mt-10 animate-spin text-slate-300" />}

      {data && data.items.length === 0 && (
        <p className="py-16 text-center text-slate-400">아직 게시글이 없습니다. 첫 글을 남겨 보세요!</p>
      )}

      {data && data.items.length > 0 && (
        <ul className="divide-y divide-white/10 overflow-hidden rounded-xl border border-white/10 bg-white/5">
          {data.items.map((post) => (
            <li key={post.id}>
              <button
                onClick={() => setView({ type: 'detail', id: post.id })}
                className="flex w-full flex-col gap-1 px-4 py-3 text-left transition hover:bg-white/10"
              >
                <span className="truncate font-medium">{post.title}</span>
                <span className="text-xs text-slate-400">
                  {post.author} · {formatDate(post.createdAt)}
                </span>
              </button>
            </li>
          ))}
        </ul>
      )}

      {data && data.totalPages > 1 && (
        <div className="flex items-center justify-center gap-4 text-sm">
          <button
            onClick={() => setPage(page - 1)}
            disabled={page === 0}
            className="rounded-lg p-2 transition hover:bg-white/10 disabled:opacity-30"
            aria-label="이전 페이지"
          >
            <ChevronLeft size={18} />
          </button>
          <span>
            {page + 1} / {data.totalPages}
          </span>
          <button
            onClick={() => setPage(page + 1)}
            disabled={page + 1 >= data.totalPages}
            className="rounded-lg p-2 transition hover:bg-white/10 disabled:opacity-30"
            aria-label="다음 페이지"
          >
            <ChevronRight size={18} />
          </button>
        </div>
      )}
    </section>
  )
}
