import { useState } from 'react'
import { createPost, errorMessage, updatePost } from '../api'

const inputClass =
  'w-full rounded-lg border border-white/10 bg-white/5 px-3 py-2 outline-none transition focus:border-indigo-400'

/** post 가 있으면 수정, 없으면 새 글 작성. */
export default function PostForm({ post, onDone, onCancel }) {
  const editing = Boolean(post)
  const [form, setForm] = useState({
    title: post?.title ?? '',
    content: post?.content ?? '',
    author: post?.author ?? '',
    password: '',
  })
  const [error, setError] = useState(null)
  const [saving, setSaving] = useState(false)

  const set = (key) => (e) => setForm({ ...form, [key]: e.target.value })

  const submit = async (e) => {
    e.preventDefault()
    setError(null)
    setSaving(true)
    try {
      const saved = editing
        ? await updatePost(post.id, { title: form.title, content: form.content, password: form.password })
        : await createPost(form)
      onDone(saved)
    } catch (err) {
      setError(errorMessage(err, '저장하지 못했습니다. 입력 내용을 확인해 주세요.'))
      setSaving(false)
    }
  }

  return (
    <form onSubmit={submit} className="flex flex-col gap-4">
      <h2 className="text-2xl font-bold">{editing ? '글 수정' : '글쓰기'}</h2>

      {!editing && (
        <input
          className={inputClass}
          placeholder="작성자 (최대 30자)"
          maxLength={30}
          value={form.author}
          onChange={set('author')}
          required
        />
      )}
      <input
        className={inputClass}
        placeholder="제목 (최대 100자)"
        maxLength={100}
        value={form.title}
        onChange={set('title')}
        required
      />
      <textarea
        className={`${inputClass} min-h-48`}
        placeholder="내용 (최대 5000자)"
        maxLength={5000}
        value={form.content}
        onChange={set('content')}
        required
      />
      <input
        className={inputClass}
        type="password"
        placeholder={editing ? '글 작성 시 설정한 비밀번호' : '비밀번호 (4자 이상, 수정·삭제 시 필요)'}
        minLength={editing ? undefined : 4}
        maxLength={50}
        value={form.password}
        onChange={set('password')}
        required
      />

      {error && <p className="text-sm text-red-300">{error}</p>}

      <div className="flex justify-end gap-2">
        <button type="button" onClick={onCancel} className="rounded-lg px-4 py-2 transition hover:bg-white/10">
          취소
        </button>
        <button
          type="submit"
          disabled={saving}
          className="rounded-lg bg-indigo-500 px-5 py-2 font-semibold transition hover:bg-indigo-400 disabled:opacity-50"
        >
          {editing ? '수정하기' : '등록하기'}
        </button>
      </div>
    </form>
  )
}
