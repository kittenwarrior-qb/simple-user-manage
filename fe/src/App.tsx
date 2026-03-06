import { BrowserRouter, Routes, Route } from 'react-router-dom'
import User from '@/pages/User'
import Login from '@/pages/Login'
import ProtectedRoute from '@/components/ProtectedRoute'
import Company from './pages/Company'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={
          <ProtectedRoute>
            <User />
          </ProtectedRoute>
        } />
                <Route path="/company" element={
          <ProtectedRoute>
            <Company/>
          </ProtectedRoute>
        } />
        <Route path="/login" element={<Login />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
