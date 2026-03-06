import { BrowserRouter, Routes, Route } from 'react-router-dom'
import User from '@/pages/User'
import Login from '@/pages/Login'
import ProtectedRoute from '@/components/ProtectedRoute'
import Company from './pages/Company'
import Team from './pages/Team'
import { Toaster } from '@/components/ui/sonner'

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
                <Route path="/team" element={
          <ProtectedRoute>
            <Team/>
          </ProtectedRoute>

        } />

        <Route path="/login" element={<Login />} />
      </Routes>
      <Toaster />
    </BrowserRouter>
  )
}

export default App
