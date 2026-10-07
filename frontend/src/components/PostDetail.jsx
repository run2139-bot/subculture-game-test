import { useEffect, useState } from 'react'
import { ArrowLeft, Loader2, Pencil, Trash2 } from 'lucide-react'
import { deletePost, errorMessage, fetchPost } from '../api'
import { formatDate } from '../format'

export default function PostDetail({ id, onBack, onEdit, onDeleted }) {
  const [post, setPost] = useState(null)
  const [error, setError] = useState(null)
  const [confirming, setConfirming] = useState(false)
  const [password, setPassword] = useState('')
  const [deleteError, setDeleteError] = useState(null)

  useEffect(() => {
    fetchPost(id)
      .then(setPost)
      .catch((e) => setError(errorMessage(e, '게시글을 불러오지 못했습니다.')))
  }, [id])

  const remove = async () => {
    setDeleteError(null)
    try {
      await deletePost(id, password)
      onDeleted()
    } catch (e) {
      setDeleteError(errorMessage(e, '삭제하지 못했습니다.'))
    }
  }

  return (
    <section className="flex flex-col gap-4">
      <button onClick={onBack} className="flex w-fit items-center gap-1 text-sm text-slate-300 hover:text-white">
        <ArrowLeft size={16} />
        목록으로
      </button>

      {error && <p className="rounded-lg bg-red-500/10 p-3 text-sm text-red-200">{error}</p>}
      {!post && !error && <Loader2 className="mx-auto mt-10 animate-spin text-slate-300" />}

      {post && (
        <>
          <article className="rounded-xl border border-white/10 bg-white/5 p-5">
            <h2 className="text-2xl font-bold break-words">{post.title}</h2>
            <p className="mt-1 text-xs text-slate-400">
              {post.author} · {formatDate(post.createdAt)}
              {post.updatedAt !== post.createdAt && ' (수정됨)'}
            </p>
            <p className="mt-5 whitespace-pre-wrap break-words text-slate-100">{post.content}</p>
          </article>

          <div className="flex flex-col items-end gap-2">
            {!confirming ? (
              <div className="flex gap-2">
                <button
                  onClick={() => onEdit(post)}
                  className="flex items-center gap-1 rounded-lg border border-white/20 px-4 py-2 text-sm transition hover:bg-white/10"
                >
                  <Pencil size={14} />
                  수정
                </button>
                <button
                  onClick={() => setConfirming(true)}
                  className="flex items-center gap-1 rounded-lg border border-red-400/40 px-4 py-2 text-sm text-red-300 transition hover:bg-red-500/10"
                >
                  <Trash2 size={14} />
                  삭제
                </button>
              </div>
            ) : (
              <div className="flex w-full flex-wrap items-center justify-end gap-2">
                <input
                  type="password"
                  placeholder="비밀번호 입력"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="rounded-lg border border-white/10 bg-white/5 px-3 py-2 text-sm outline-none focus:border-red-400"
                />
                <button
                  onClick={remove}
                  disabled={!password}
                  className="rounded-lg bg-red-500 px-4 py-2 text-sm font-semibold transition hover:bg-red-400 disabled:opacity-50"
                >
                  삭제 확인
                </button>
                <button
                  onClick={() => {
                    setConfirming(false)
                    setPassword('')
                    setDeleteError(null)
                  }}
                  className="rounded-lg px-3 py-2 text-sm transition hover:bg-white/10"
                >
                  취소
                </button>
              </div>
            )}
            {deleteError && <p className="text-sm text-red-300">{deleteError}</p>}
          </div>
        </>
      )}
    </section>
  )
}
