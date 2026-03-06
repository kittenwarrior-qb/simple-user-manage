import { LoginForm } from "@/components/login-form"
import { Navbar } from "@/components/navbar"

export default function Login() {
  return (
    <div className="min-h-svh items-center justify-center max-w-[1280px] mx-auto">
        <Navbar></Navbar>
        <div className="flex items-center justify-center mt-20">
          <div className="w-full max-w-sm">
            <div className="mb-4">
            </div>
            <LoginForm />
          </div>

        </div>
    </div>
  )
}
